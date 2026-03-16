# Testimonials Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000012 | As Public I want to be able to see list of customer reviews and ratings, so that I can understand the customer satisfaction and feedback about the products and services offered by the company. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00033 | Section title: "What Our Customers Say" | v1.0.0 |
| NFRL00036 | The testimonials will be displayed in carousel layout with 1 testimonial per slide on desktop and mobile devices. | v1.0.0 |
| NFRL00039 | The customer name will be displayed in a bold font with a size of at least 18px. | v1.0.0 |
| NFRL00042 | The customer review will be displayed in a regular font with a size of at least 16px. | v1.0.0 |
| NFRL00045 | The customer rating will be displayed as a star icon with a size of at least 24px. | v1.0.0 |
| NFRL00048 | White background for the section, and the card will have a shadow effect. | v1.0.0 |

### Constraints
_None for this module._

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Table | `tst_testimonial` |
| Mockup Screen | `mockup/pages/home.html` (Testimonials Section) |
| Admin Model | `admin/context/model/testimonials-section/model.md` |

## 2. Service Contract

```php
namespace Modules\TestimonialsSection\Contracts;

use Illuminate\Support\Collection;

interface TestimonialServiceInterface
{
    /**
     * Get all visible testimonials.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending.
     *
     * @return Collection<TestimonialData>
     */
    public function getAllVisible(): Collection;
}
```

## 3. DTO

```php
namespace Modules\TestimonialsSection\DTOs;

use Spatie\LaravelData\Data;

class TestimonialData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $customer_name,
        public readonly string $customer_review,
        public readonly int $customer_rating,
        public readonly int $display_order,
    ) {}
}
```

## 4. Exception

```php
namespace Modules\TestimonialsSection\Exceptions;

use App\Exceptions\WebApplicationException;

class TestimonialException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Testimonial not found: {$id}", 404);
    }
}
```

## 5. Eloquent Model

```php
namespace Modules\TestimonialsSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class Testimonial extends Model
{
    use HasUuids;

    protected $table = 'tst_testimonial';

    protected $fillable = [
        'customer_name', 'customer_review', 'customer_rating', 'display_order',
    ];

    protected $casts = [
        'customer_rating' => 'integer',
        'display_order' => 'integer',
    ];
}
```

## 6. Migration

**Not applicable.** Admin Portal owns the schema.

## 7. Service Implementation

```php
namespace Modules\TestimonialsSection\Services;

use Illuminate\Support\Collection;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TestimonialsSection\DTOs\TestimonialData;
use Modules\TestimonialsSection\Models\Testimonial;

class TestimonialService implements TestimonialServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed
        return Testimonial::orderBy('display_order')
            ->get()
            ->map(fn (Testimonial $t) => new TestimonialData(
                id: $t->id,
                customer_name: $t->customer_name,
                customer_review: $t->customer_review,
                customer_rating: $t->customer_rating,
                display_order: $t->display_order,
            ));
    }
}
```

## 8. Blade Template (Section in Home Page)

```blade
{{-- Testimonials Section — USL000012, NFRL00033-NFRL00048 --}}
<section id="testimonials" class="bg-white py-16 md:py-24" x-data="{
    current: 0,
    testimonials: @js($testimonials->map(fn($t) => [
        'name' => $t->customer_name,
        'text' => $t->customer_review,
        'stars' => $t->customer_rating,
    ])->values()),
    next() { this.current = (this.current + 1) % this.testimonials.length },
    prev() { this.current = (this.current - 1 + this.testimonials.length) % this.testimonials.length }
}">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl md:text-4xl font-bold mb-12">What Our Customers Say</h2>

        <div class="relative">
            <template x-for="(t, idx) in testimonials" :key="idx">
                <div x-show="current === idx"
                     x-transition:enter="transition ease-out duration-500"
                     x-transition:enter-start="opacity-0 translate-y-4"
                     x-transition:enter-end="opacity-100 translate-y-0"
                     x-transition:leave="transition ease-in duration-300"
                     x-transition:leave-start="opacity-100"
                     x-transition:leave-end="opacity-0"
                     class="px-4 md:px-16">
                    {{-- Star rating (NFRL00045 — 24px stars) --}}
                    <div class="flex justify-center mb-4 space-x-1">
                        <template x-for="s in 5" :key="'star-'+idx+'-'+s">
                            <svg class="w-6 h-6" :class="s <= t.stars ? 'text-warning' : 'text-gray-300'" fill="currentColor" viewBox="0 0 24 24">
                                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                            </svg>
                        </template>
                    </div>
                    {{-- Review text (NFRL00042 — 16px) --}}
                    <p class="text-base md:text-lg text-text-secondary italic mb-6 leading-relaxed" x-text="'&quot;' + t.text + '&quot;'"></p>
                    {{-- Customer name (NFRL00039 — bold 18px) --}}
                    <p class="text-lg font-bold text-text-primary" x-text="t.name"></p>
                </div>
            </template>

            {{-- Navigation arrows --}}
            <button @click="prev()" class="absolute left-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow" aria-label="Previous testimonial">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
            </button>
            <button @click="next()" class="absolute right-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow" aria-label="Next testimonial">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
            </button>
        </div>

        {{-- Dot indicators --}}
        <div class="flex justify-center mt-8 space-x-2">
            <template x-for="(t, idx) in testimonials" :key="'tdot-'+idx">
                <button @click="current = idx" :class="current === idx ? 'bg-primary' : 'bg-gray-300'" class="w-3 h-3 rounded-full transition" :aria-label="'Go to testimonial '+(idx+1)"></button>
            </template>
        </div>
    </div>
</section>
```

## 9. Module Service Provider

```php
namespace Modules\TestimonialsSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TestimonialsSection\Services\TestimonialService;

class TestimonialsSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(TestimonialServiceInterface::class, TestimonialService::class);
    }

    public function boot(): void {}
}
```
