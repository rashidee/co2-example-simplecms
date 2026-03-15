# Contact Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Contact Section
> **nwidart Module:** Modules/ContactSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000018 | As Public I want to be able to see contact information and a contact form, so that I can easily get in touch with the company for any inquiries or support. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00072 | Section title: "Contact Us" | v1.0.0 |
| NFRL00075 | The contact information will be displayed in a list layout with phone icon, email icon, and location icon, each at 16px font size. | v1.0.0 |
| NFRL00078 | The contact form will be displayed in a vertical layout with name, email, message (max 500 chars) fields and a submit button, all at 16px font size. | v1.0.0 |
| NFRL00081 | White background for the section, and the contact information and contact form will have a shadow effect. | v1.0.0 |
| NFRL00084 | The contact form will be protected with a CAPTCHA to prevent spam submissions. | v1.0.0 |

### 1.3 Constraints

_No constraints specific to this module._

---

## 2. Eloquent Models

### 2.1 ContactInfo (Read-Only)

**File:** `Modules/ContactSection/app/Models/ContactInfo.php`

**Table:** `contact_info` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Model;

class ContactInfo extends Model
{
    protected $table = 'contact_info';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
    ];
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| phone_number | phone_number | string(50) | Company phone number |
| email_address | email_address | string(255) | Company email |
| physical_address | physical_address | string(500) | Company physical address |

### 2.2 ContactMessage (Write)

**File:** `Modules/ContactSection/app/Models/ContactMessage.php`

**Table:** `contact_messages` (shared with Admin Portal, write from Landing Page)

```php
<?php

namespace Modules\ContactSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ContactMessage extends Model
{
    use HasUuids;

    protected $table = 'contact_messages';

    protected $keyType = 'string';
    public $incrementing = false;

    /**
     * Only created_at is tracked. No updated_at column.
     */
    const CREATED_AT = 'created_at';
    const UPDATED_AT = null;

    protected $fillable = [
        'sender_name',
        'sender_email',
        'message_content',
        'submitted_at',
    ];

    protected $casts = [
        'id' => 'string',
        'submitted_at' => 'datetime',
        'created_at' => 'datetime',
    ];
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK, auto-generated (HasUuids) |
| sender_name | sender_name | string(255) | Visitor name |
| sender_email | sender_email | string(255) | Visitor email |
| message_content | message_content | text | Message body |
| submitted_at | submitted_at | timestamp | Set when form is submitted |
| created_at | created_at | timestamp | Auto-set by Eloquent |

---

## 3. Services

### 3.1 ContactInfoService

**File:** `Modules/ContactSection/app/Services/ContactInfoService.php`

```php
<?php

namespace Modules\ContactSection\Services;

use Modules\ContactSection\Models\ContactInfo;

class ContactInfoService
{
    /**
     * Get the single contact info record.
     * Returns null if no record exists.
     */
    public function getContactInfo(): ?ContactInfo
    {
        return ContactInfo::query()->first();
    }
}
```

### 3.2 ContactMessageService

**File:** `Modules/ContactSection/app/Services/ContactMessageService.php`

```php
<?php

namespace Modules\ContactSection\Services;

use Modules\ContactSection\Models\ContactMessage;

class ContactMessageService
{
    /**
     * Create a new contact message from form submission.
     *
     * @param array{sender_name: string, sender_email: string, message_content: string} $data
     * @return ContactMessage
     */
    public function createMessage(array $data): ContactMessage
    {
        return ContactMessage::create([
            'sender_name' => $data['sender_name'],
            'sender_email' => $data['sender_email'],
            'message_content' => $data['message_content'],
            'submitted_at' => now(),
        ]);
    }
}
```

---

## 4. FormRequest

### 4.1 StoreContactMessageRequest

**File:** `Modules/ContactSection/app/Http/Requests/StoreContactMessageRequest.php`

```php
<?php

namespace Modules\ContactSection\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreContactMessageRequest extends FormRequest
{
    /**
     * No authentication required (public form).
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Validation rules for the contact form.
     *
     * @return array<string, mixed>
     */
    public function rules(): array
    {
        return [
            'sender_name' => ['required', 'string', 'max:255'],
            'sender_email' => ['required', 'email', 'max:255'],
            'message_content' => ['required', 'string', 'max:500'],
            'g-recaptcha-response' => ['required'], // NFRL00084 - CAPTCHA validation
        ];
    }

    /**
     * Custom validation messages.
     *
     * @return array<string, string>
     */
    public function messages(): array
    {
        return [
            'sender_name.required' => 'Please enter your name.',
            'sender_name.max' => 'Name cannot exceed 255 characters.',
            'sender_email.required' => 'Please enter your email address.',
            'sender_email.email' => 'Please enter a valid email address.',
            'message_content.required' => 'Please enter your message.',
            'message_content.max' => 'Message cannot exceed 500 characters.',
            'g-recaptcha-response.required' => 'Please complete the CAPTCHA verification.',
        ];
    }
}
```

---

## 5. Data Transfer Objects

### 5.1 ContactInfoData

**File:** `Modules/ContactSection/app/Data/ContactInfoData.php`

```php
<?php

namespace Modules\ContactSection\Data;

use Spatie\LaravelData\Data;

class ContactInfoData extends Data
{
    public function __construct(
        public readonly string $phone_number,
        public readonly string $email_address,
        public readonly string $physical_address,
    ) {}
}
```

### 5.2 ContactMessageData

**File:** `Modules/ContactSection/app/Data/ContactMessageData.php`

```php
<?php

namespace Modules\ContactSection\Data;

use Spatie\LaravelData\Data;

class ContactMessageData extends Data
{
    public function __construct(
        public readonly string $sender_name,
        public readonly string $sender_email,
        public readonly string $message_content,
    ) {}
}
```

---

## 6. ViewModel

### 6.1 ContactSectionViewModel

**File:** `Modules/ContactSection/app/ViewModels/ContactSectionViewModel.php`

```php
<?php

namespace Modules\ContactSection\ViewModels;

use Spatie\ViewModels\ViewModel;
use Modules\ContactSection\Data\ContactInfoData;
use Modules\ContactSection\Models\ContactInfo;

class ContactSectionViewModel extends ViewModel
{
    public function __construct(
        private readonly ?ContactInfo $contactInfo,
    ) {}

    public function contactInfo(): ?ContactInfoData
    {
        if ($this->contactInfo === null) {
            return null;
        }

        return new ContactInfoData(
            phone_number: $this->contactInfo->phone_number,
            email_address: $this->contactInfo->email_address,
            physical_address: $this->contactInfo->physical_address,
        );
    }
}
```

---

## 7. Controller

### 7.1 ContactSectionController

**File:** `Modules/ContactSection/app/Http/Controllers/ContactSectionController.php`

```php
<?php

namespace Modules\ContactSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Response;
use Modules\ContactSection\Http\Requests\StoreContactMessageRequest;
use Modules\ContactSection\Services\ContactInfoService;
use Modules\ContactSection\Services\ContactMessageService;

class ContactSectionController extends Controller
{
    public function __construct(
        private readonly ContactInfoService $contactInfoService,
        private readonly ContactMessageService $contactMessageService,
    ) {}

    /**
     * Handle contact form submission via htmx POST.
     * Returns an HTML fragment for htmx to swap into the page.
     *
     * Route: POST /contact
     */
    public function store(StoreContactMessageRequest $request): Response
    {
        $validated = $request->validated();

        $this->contactMessageService->createMessage([
            'sender_name' => $validated['sender_name'],
            'sender_email' => $validated['sender_email'],
            'message_content' => $validated['message_content'],
        ]);

        // Return success HTML fragment for htmx swap
        return response(
            view('contactsection::components.contact-form-success')->render(),
            200
        );
    }
}
```

---

## 8. Blade Templates

### 8.1 Contact Section Component

**File:** `Modules/ContactSection/resources/views/components/contact-section.blade.php`

```blade
{{-- USL000018 [v1.0.0] - Contact information and form --}}
{{-- NFRL00072 [v1.0.0] - Section title: "Contact Us" --}}
{{-- NFRL00075 [v1.0.0] - Contact info with icons --}}
{{-- NFRL00078 [v1.0.0] - Contact form vertical layout --}}
{{-- NFRL00081 [v1.0.0] - White bg, shadow --}}
{{-- NFRL00084 [v1.0.0] - CAPTCHA --}}

<section id="contact" class="bg-white py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Contact Us</h2>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
            {{-- Left: Contact information (NFRL00075) --}}
            @if($contactInfo)
            <div class="space-y-8">
                {{-- Phone --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor"
                         stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M2.25 6.75c0 8.284 6.716 15 15 15h2.25a2.25 2.25 0 002.25-2.25v-1.372c0-.516-.351-.966-.852-1.091l-4.423-1.106c-.44-.11-.902.055-1.173.417l-.97 1.293c-.282.376-.769.542-1.21.38a12.035 12.035 0 01-7.143-7.143c-.162-.441.004-.928.38-1.21l1.293-.97c.363-.271.527-.734.417-1.173L6.963 3.102a1.125 1.125 0 00-1.091-.852H4.5A2.25 2.25 0 002.25 4.5v2.25z"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Phone</h3>
                        <p class="text-text-secondary">{{ $contactInfo->phone_number }}</p>
                    </div>
                </div>

                {{-- Email --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor"
                         stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M21.75 6.75v10.5a2.25 2.25 0 01-2.25 2.25h-15a2.25 2.25 0 01-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0019.5 4.5h-15a2.25 2.25 0 00-2.25 2.25m19.5 0v.243a2.25 2.25 0 01-1.07 1.916l-7.5 4.615a2.25 2.25 0 01-2.36 0L3.32 8.91a2.25 2.25 0 01-1.07-1.916V6.75"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Email</h3>
                        <p class="text-text-secondary">{{ $contactInfo->email_address }}</p>
                    </div>
                </div>

                {{-- Address --}}
                <div class="flex items-start space-x-4">
                    <svg class="w-6 h-6 text-primary flex-shrink-0 mt-1" fill="none" stroke="currentColor"
                         stroke-width="1.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M15 10.5a3 3 0 11-6 0 3 3 0 016 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round"
                              d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1115 0z"/>
                    </svg>
                    <div>
                        <h3 class="font-bold text-lg mb-1">Address</h3>
                        <p class="text-text-secondary">{!! nl2br(e($contactInfo->physical_address)) !!}</p>
                    </div>
                </div>
            </div>
            @endif

            {{-- Right: Contact form (NFRL00078) --}}
            <div>
                <form hx-post="{{ route('contact.store') }}"
                      hx-target="#contact-form-result"
                      hx-swap="innerHTML"
                      hx-indicator="#contact-spinner"
                      class="space-y-6">
                    @csrf

                    <div>
                        <label for="contact-name" class="block text-sm font-medium mb-1">Name</label>
                        <input type="text"
                               id="contact-name"
                               name="sender_name"
                               placeholder="Your full name"
                               required
                               class="w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>

                    <div>
                        <label for="contact-email" class="block text-sm font-medium mb-1">Email</label>
                        <input type="email"
                               id="contact-email"
                               name="sender_email"
                               placeholder="you@example.com"
                               required
                               class="w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary">
                    </div>

                    <div x-data="{ charCount: 0 }">
                        <label for="contact-message" class="block text-sm font-medium mb-1">Message</label>
                        <textarea id="contact-message"
                                  name="message_content"
                                  rows="5"
                                  maxlength="500"
                                  placeholder="How can we help you? (max 500 characters)"
                                  required
                                  x-on:input="charCount = $el.value.length"
                                  class="w-full border border-border rounded-btn px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-primary resize-none"></textarea>
                        <p class="text-xs text-text-secondary mt-1">
                            <span x-text="charCount">0</span>/500 characters
                        </p>
                    </div>

                    {{-- CAPTCHA placeholder (NFRL00084) --}}
                    <div class="bg-page-bg border border-border rounded-btn p-4 text-center text-text-secondary text-sm">
                        {{-- Google reCAPTCHA v3 widget will be rendered here --}}
                        <div class="g-recaptcha" data-sitekey="{{ config('services.recaptcha.site_key', '') }}"></div>
                    </div>

                    <button type="submit"
                            class="w-full bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-6 rounded-btn transition">
                        Send Message
                    </button>

                    {{-- Loading spinner (hidden by default, shown during htmx request) --}}
                    <div id="contact-spinner" class="htmx-indicator text-center">
                        <span class="text-text-secondary text-sm">Sending...</span>
                    </div>
                </form>

                {{-- Result placeholder for htmx swap --}}
                <div id="contact-form-result" class="mt-4"></div>
            </div>
        </div>
    </div>
</section>
```

### 8.2 Contact Form Success Fragment

**File:** `Modules/ContactSection/resources/views/components/contact-form-success.blade.php`

```blade
{{-- HTML fragment returned after successful form submission via htmx --}}
<div class="bg-success/10 border border-success rounded-btn p-4 text-center">
    <p class="text-success font-semibold">Thank you for your message! We will get back to you soon.</p>
</div>
```

### 8.3 Contact Form Error Fragment

**File:** `Modules/ContactSection/resources/views/components/contact-form-error.blade.php`

```blade
{{-- HTML fragment returned on validation error via htmx --}}
<div class="bg-error/10 border border-error rounded-btn p-4">
    <p class="text-error font-semibold mb-2">Please correct the following errors:</p>
    <ul class="list-disc list-inside text-error text-sm">
        @foreach($errors as $error)
            <li>{{ $error }}</li>
        @endforeach
    </ul>
</div>
```

---

## 9. Routes

### 9.1 Module Routes

**File:** `Modules/ContactSection/routes/web.php`

```php
<?php

use Illuminate\Support\Facades\Route;
use Modules\ContactSection\Http\Controllers\ContactSectionController;

Route::post('/contact', [ContactSectionController::class, 'store'])
    ->name('contact.store');
```

---

## 10. Module Registration

### 10.1 module.json

**File:** `Modules/ContactSection/module.json`

```json
{
    "name": "ContactSection",
    "alias": "contact-section",
    "description": "Contact information display and contact form for the landing page",
    "keywords": ["contact", "form", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\ContactSection\\Providers\\ContactSectionServiceProvider"
    ],
    "files": []
}
```

---

## 11. CAPTCHA Implementation Notes

Per NFRL00084, the contact form requires CAPTCHA protection. The recommended approach is:

1. **Google reCAPTCHA v3** - Invisible CAPTCHA that scores user behavior.
2. Add the reCAPTCHA site key and secret key to `.env`:
   ```dotenv
   RECAPTCHA_SITE_KEY=your-site-key
   RECAPTCHA_SECRET_KEY=your-secret-key
   ```
3. Add a `config/services.php` entry:
   ```php
   'recaptcha' => [
       'site_key' => env('RECAPTCHA_SITE_KEY'),
       'secret_key' => env('RECAPTCHA_SECRET_KEY'),
   ],
   ```
4. Server-side validation in `StoreContactMessageRequest` verifies the token with Google's API.
