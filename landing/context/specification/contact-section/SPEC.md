# Contact Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000018 | As Public I want to be able to see contact information and a contact form, so that I can easily get in touch with the company for any inquiries or support. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00072 | Section title: "Contact Us" | v1.0.0 |
| NFRL00075 | The contact information will be displayed in a list layout with phone icon, email icon, and location icon (each at least 16px). | v1.0.0 |
| NFRL00078 | The contact form will be displayed in a vertical layout with name field, email field, message textarea (max 500 chars), and submit button. | v1.0.0 |
| NFRL00081 | White background for the section, and the contact information and contact form will have a shadow effect. | v1.0.0 |
| NFRL00084 | The contact form will be protected with a CAPTCHA to prevent spam submissions. | v1.0.0 |

### Constraints
_None for this module._

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Tables | `cts_contact_info` (read), `cts_contact_message` (write) |
| Mockup Screen | `mockup/pages/home.html` (Contact Section) |
| Admin Model | `admin/context/model/contact-section/model.md` |

## 2. Service Contract

```php
namespace Modules\ContactSection\Contracts;

use Modules\ContactSection\DTOs\ContactInfoData;
use Modules\ContactSection\DTOs\ContactMessageData;

interface ContactSectionServiceInterface
{
    /**
     * Get the company contact information (single record).
     */
    public function getContactInfo(): ?ContactInfoData;

    /**
     * Submit a contact form message.
     * This is the ONLY write operation in the entire Landing Page application.
     */
    public function submitMessage(array $data): ContactMessageData;
}
```

## 3. DTOs

### ContactInfoData

```php
namespace Modules\ContactSection\DTOs;

use Spatie\LaravelData\Data;

class ContactInfoData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $phone_number,
        public readonly string $email_address,
        public readonly string $physical_address,
        public readonly string $linkedin_url,
    ) {}
}
```

### ContactMessageData

```php
namespace Modules\ContactSection\DTOs;

use Spatie\LaravelData\Data;

class ContactMessageData extends Data
{
    public function __construct(
        public readonly ?string $id,
        public readonly string $sender_name,
        public readonly string $sender_email,
        public readonly string $message_content,
        public readonly ?string $submitted_at,
    ) {}
}
```

## 4. Exception

```php
namespace Modules\ContactSection\Exceptions;

use App\Exceptions\WebApplicationException;

class ContactSectionException extends WebApplicationException
{
    public static function noContactInfo(): self
    {
        return new self('No contact information configured', 404);
    }

    public static function submissionFailed(): self
    {
        return new self('Failed to submit contact message', 500, 'Your message could not be sent. Please try again later.');
    }
}
```

## 5. Eloquent Models

### ContactInfo (read-only)

```php
namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactInfo extends Model
{
    use HasUuids;

    protected $table = 'cts_contact_info';

    protected $fillable = [
        'phone_number', 'email_address', 'physical_address', 'linkedin_url',
    ];
}
```

### ContactMessage (write)

```php
namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactMessage extends Model
{
    use HasUuids;

    protected $table = 'cts_contact_message';

    protected $fillable = [
        'sender_name', 'sender_email', 'message_content', 'submitted_at',
    ];

    protected $casts = [
        'submitted_at' => 'datetime',
    ];
}
```

## 6. Migration

**Not applicable.** Admin Portal owns the schema.

## 7. Service Implementation

```php
namespace Modules\ContactSection\Services;

use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\DTOs\ContactInfoData;
use Modules\ContactSection\DTOs\ContactMessageData;
use Modules\ContactSection\Events\ContactMessageReceivedEvent;
use Modules\ContactSection\Models\ContactInfo;
use Modules\ContactSection\Models\ContactMessage;

class ContactSectionService implements ContactSectionServiceInterface
{
    public function getContactInfo(): ?ContactInfoData
    {
        $info = ContactInfo::first();
        return $info ? ContactInfoData::from($info) : null;
    }

    public function submitMessage(array $data): ContactMessageData
    {
        $message = ContactMessage::create([
            'sender_name' => $data['sender_name'],
            'sender_email' => $data['sender_email'],
            'message_content' => $data['message_content'],
            'submitted_at' => now(),
            'created_by' => 'public',
            'updated_by' => 'public',
        ]);

        event(new ContactMessageReceivedEvent(
            messageId: $message->id,
            senderName: $message->sender_name,
            senderEmail: $message->sender_email,
            occurredAt: new \DateTimeImmutable(),
        ));

        return ContactMessageData::from($message);
    }
}
```

## 8. Form Request

```php
namespace Modules\ContactSection\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreContactMessageRequest extends FormRequest
{
    public function authorize(): bool { return true; }

    public function rules(): array
    {
        return [
            'sender_name' => ['required', 'string', 'max:255'],
            'sender_email' => ['required', 'email', 'max:255'],
            'message_content' => ['required', 'string', 'max:500'],
            // NFRL00084: CAPTCHA validation
            // 'g-recaptcha-response' => ['required', 'captcha'],
        ];
    }

    public function messages(): array
    {
        return [
            'message_content.max' => 'Your message must not exceed 500 characters.',
        ];
    }
}
```

## 9. Fragment Controller

The contact form submits via htmx and returns a toast notification fragment:

```php
namespace Modules\ContactSection\Http\Controllers;

use Illuminate\Http\Response;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\Http\Requests\StoreContactMessageRequest;

class ContactFragmentController extends Controller
{
    public function __construct(
        private readonly ContactSectionServiceInterface $service,
    ) {}

    /**
     * Handle contact form submission (htmx POST).
     * Returns empty response with HX-Trigger for toast notification.
     */
    public function store(StoreContactMessageRequest $request): Response
    {
        $this->service->submitMessage($request->validated());

        return response('', 200)
            ->header('HX-Trigger', json_encode([
                'showToast' => [
                    'type' => 'success',
                    'message' => 'Thank you! Your message has been sent successfully.',
                ],
                'resetContactForm' => true,
            ]));
    }
}
```

## 10. Blade Template (Section in Home Page)

```blade
{{-- Contact Section — USL000018, NFRL00072-NFRL00084 --}}
<section id="contact" class="bg-white py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Contact Us</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
            {{-- Left: Contact info --}}
            @if($contactInfo)
            <div class="space-y-8">
                {{-- Phone (NFRL00075) --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 002.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 01-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 00-1.091-.852H4.5A2.25 2.25 0 002.25 4.5v2.25z"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Phone</h3>
                        <p class="text-text-secondary">{{ $contactInfo->phone_number }}</p>
                    </div>
                </div>

                {{-- Email --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.32 8.91a2.25 2.25 0 01-1.07-1.916V6.75"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Email</h3>
                        <p class="text-text-secondary">{{ $contactInfo->email_address }}</p>
                    </div>
                </div>

                {{-- Address --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Address</h3>
                        <p class="text-text-secondary">{!! nl2br(e($contactInfo->physical_address)) !!}</p>
                    </div>
                </div>
            </div>
            @endif

            {{-- Right: Contact form --}}
            <div>
                <form hx-post="{{ route('contact.store') }}"
                      hx-swap="none"
                      @htmx:after-request.camel="if(event.detail.successful) $el.reset()"
                      class="space-y-6">
                    @csrf

                    <x-form-control label="Name" name="sender_name" :required="true">
                        <x-input name="sender_name" placeholder="Your full name" required />
                    </x-form-control>

                    <x-form-control label="Email" name="sender_email" :required="true">
                        <x-input name="sender_email" type="email" placeholder="you@example.com" required />
                    </x-form-control>

                    <x-form-control label="Message" name="message_content" :required="true">
                        <x-textarea name="message_content" rows="5" maxlength="500"
                                    placeholder="How can we help you? (max 500 characters)" required />
                    </x-form-control>

                    {{-- CAPTCHA placeholder (NFRL00084) --}}
                    <div class="bg-page-bg border border-border rounded-btn p-4 text-center text-text-secondary text-sm">
                        CAPTCHA verification will be integrated here
                    </div>

                    <x-button type="submit" tone="primary" class="w-full">Send Message</x-button>
                </form>
            </div>
        </div>
    </div>
</section>
```

## 11. Module Routes

```php
// Modules/ContactSection/routes/web.php
use Illuminate\Support\Facades\Route;
use Modules\ContactSection\Http\Controllers\ContactFragmentController;

// Contact form submission — the only write endpoint
Route::post('/contact', [ContactFragmentController::class, 'store'])->name('contact.store');
```

## 12. Module Service Provider

```php
namespace Modules\ContactSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;
use Modules\ContactSection\Services\ContactSectionService;

class ContactSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(ContactSectionServiceInterface::class, ContactSectionService::class);
    }

    public function boot(): void
    {
        $this->loadRoutesFrom(module_path('ContactSection', 'routes/web.php'));
    }
}
```

## 13. Event

```php
namespace Modules\ContactSection\Events;

use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ContactMessageReceivedEvent
{
    use Dispatchable, SerializesModels;

    public function __construct(
        public readonly string $messageId,
        public readonly string $senderName,
        public readonly string $senderEmail,
        public readonly \DateTimeImmutable $occurredAt,
    ) {}
}
```
