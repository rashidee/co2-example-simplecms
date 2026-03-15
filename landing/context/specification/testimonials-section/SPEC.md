# Testimonials Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Testimonials Section
> **nwidart Module:** Modules/TestimonialsSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000012 | As Public I want to be able to see list of customer reviews and ratings, so that I can understand the customer satisfaction and feedback about the products and services offered by the company. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00033 | Section title: "What Our Customers Say" | v1.0.0 |
| NFRL00036 | The testimonials will be displayed in carousel layout with 1 testimonial per slide on desktop and mobile devices. | v1.0.0 |
| NFRL00039 | The customer name will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the review text. | v1.0.0 |
| NFRL00042 | The customer review will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes. | v1.0.0 |
| NFRL00045 | The customer rating will be displayed as a star icon with a size of at least 24px to ensure that it is easy to understand the rating at a glance. | v1.0.0 |
| NFRL00048 | White background for the section, and the card will have a shadow effect to make it stand out from the background. | v1.0.0 |

### 1.3 Constraints

_No constraints specific to this module._

---

## 2. Eloquent Model

### 2.1 Testimonial

**File:** `Modules/TestimonialsSection/app/Models/Testimonial.php`

**Table:** `testimonials` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\TestimonialsSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class Testimonial extends Model
{
    protected $table = 'testimonials';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'customer_rating' => 'integer',
        'display_order' => 'integer',
    ];

    /**
     * Scope: only ACTIVE records.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query->where('status', 'ACTIVE');
    }

    /**
     * Scope: order by display_order ascending.
     */
    public function scopeOrdered(Builder $query): Builder
    {
        return $query->orderBy('display_order', 'asc');
    }
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| customer_name | customer_name | string(100) | Customer full name |
| customer_review | customer_review | string(1000) | Review text |
| customer_rating | customer_rating | integer | 1-5 star rating |
| display_order | display_order | integer | Display sorting |
| status | status | string (enum) | DRAFT, INACTIVE, ACTIVE |

---

## 3. Service

### 3.1 TestimonialService

**File:** `Modules/TestimonialsSection/app/Services/TestimonialService.php`

```php
<?php

namespace Modules\TestimonialsSection\Services;

use Illuminate\Support\Collection;
use Modules\TestimonialsSection\Models\Testimonial;

class TestimonialService
{
    /**
     * Get all active testimonials ordered by display_order.
     *
     * @return Collection<int, Testimonial>
     */
    public function getActiveTestimonials(): Collection
    {
        return Testimonial::query()
            ->active()
            ->ordered()
            ->get();
    }
}
```

---

## 4. Data Transfer Object

### 4.1 TestimonialData

**File:** `Modules/TestimonialsSection/app/Data/TestimonialData.php`

```php
<?php

namespace Modules\TestimonialsSection\Data;

use Spatie\LaravelData\Data;

class TestimonialData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $customer_name,
        public readonly string $customer_review,
        public readonly int $customer_rating,
    ) {}
}
```

---

## 5. ViewModel

### 5.1 TestimonialViewModel

**File:** `Modules/TestimonialsSection/app/ViewModels/TestimonialViewModel.php`

```php
<?php

namespace Modules\TestimonialsSection\ViewModels;

use Illuminate\Support\Collection;
use Spatie\ViewModels\ViewModel;
use Modules\TestimonialsSection\Data\TestimonialData;

class TestimonialViewModel extends ViewModel
{
    public function __construct(
        private readonly Collection $testimonials,
    ) {}

    /**
     * @return Collection<int, TestimonialData>
     */
    public function testimonials(): Collection
    {
        return $this->testimonials->map(fn ($testimonial) => new TestimonialData(
            id: $testimonial->id,
            customer_name: $testimonial->customer_name,
            customer_review: $testimonial->customer_review,
            customer_rating: $testimonial->customer_rating,
        ));
    }
}
```

---

## 6. Controller

### 6.1 TestimonialController

**File:** `Modules/TestimonialsSection/app/Http/Controllers/TestimonialController.php`

```php
<?php

namespace Modules\TestimonialsSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\TestimonialsSection\Services\TestimonialService;

class TestimonialController extends Controller
{
    public function __construct(
        private readonly TestimonialService $testimonialService,
    ) {}

    /**
     * Get active testimonials for embedding in the landing page.
     * Called from HomeController, not directly routed.
     */
    public function getActiveTestimonials()
    {
        return $this->testimonialService->getActiveTestimonials();
    }
}
```

---

## 7. Blade Templates

### 7.1 Testimonials Carousel Component

**File:** `Modules/TestimonialsSection/resources/views/components/testimonials-carousel.blade.php`

```blade
{{-- USL000012 [v1.0.0] - Customer testimonials carousel --}}
{{-- NFRL00033 [v1.0.0] - Section title: "What Our Customers Say" --}}
{{-- NFRL00036 [v1.0.0] - Carousel, 1 per slide --}}
{{-- NFRL00039 [v1.0.0] - Customer name bold 18px --}}
{{-- NFRL00042 [v1.0.0] - Review text 16px --}}
{{-- NFRL00045 [v1.0.0] - Star rating 24px --}}
{{-- NFRL00048 [v1.0.0] - White bg, shadow --}}

@if($testimonials->isNotEmpty())
<section id="testimonials" class="bg-white py-16 md:py-24" x-data="{
    current: 0,
    total: {{ $testimonials->count() }},
    next() { this.current = (this.current + 1) % this.total },
    prev() { this.current = (this.current - 1 + this.total) % this.total }
}">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
        <h2 class="text-3xl md:text-4xl font-bold mb-12">What Our Customers Say</h2>

        <div class="relative">
            @foreach($testimonials as $index => $testimonial)
            <div x-show="current === {{ $index }}"
                 x-transition:enter="transition ease-out duration-500"
                 x-transition:enter-start="opacity-0 translate-y-4"
                 x-transition:enter-end="opacity-100 translate-y-0"
                 x-transition:leave="transition ease-in duration-300"
                 x-transition:leave-start="opacity-100"
                 x-transition:leave-end="opacity-0"
                 class="px-4 md:px-16">

                {{-- Star rating (NFRL00045) --}}
                <div class="flex justify-center mb-4 space-x-1">
                    @for ($star = 1; $star <= 5; $star++)
                    <svg class="w-6 h-6 {{ $star <= $testimonial->customer_rating ? 'text-warning' : 'text-gray-300' }}"
                         fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                    </svg>
                    @endfor
                </div>

                {{-- Review text (NFRL00042) --}}
                <p class="text-base md:text-lg text-text-secondary italic mb-6 leading-relaxed">
                    &ldquo;{{ $testimonial->customer_review }}&rdquo;
                </p>

                {{-- Customer name (NFRL00039) --}}
                <p class="text-lg font-bold text-text-primary">{{ $testimonial->customer_name }}</p>
            </div>
            @endforeach

            {{-- Navigation arrows --}}
            <button @click="prev()"
                    class="absolute left-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow"
                    aria-label="Previous testimonial">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
            </button>
            <button @click="next()"
                    class="absolute right-0 top-1/2 -translate-y-1/2 bg-page-bg hover:bg-gray-200 text-text-primary rounded-full p-2 shadow"
                    aria-label="Next testimonial">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                </svg>
            </button>
        </div>

        {{-- Dot indicators --}}
        <div class="flex justify-center mt-8 space-x-2">
            @foreach($testimonials as $index => $testimonial)
            <button @click="current = {{ $index }}"
                    :class="current === {{ $index }} ? 'bg-primary' : 'bg-gray-300'"
                    class="w-3 h-3 rounded-full transition"
                    aria-label="Go to testimonial {{ $index + 1 }}"></button>
            @endforeach
        </div>
    </div>
</section>
@endif
```

---

## 8. Module Registration

### 8.1 module.json

**File:** `Modules/TestimonialsSection/module.json`

```json
{
    "name": "TestimonialsSection",
    "alias": "testimonials-section",
    "description": "Customer testimonials carousel for the landing page",
    "keywords": ["testimonials", "reviews", "carousel", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\TestimonialsSection\\Providers\\TestimonialsSectionServiceProvider"
    ],
    "files": []
}
```

### 8.2 Routes

**File:** `Modules/TestimonialsSection/routes/web.php`

```php
<?php

// No dedicated routes for the Testimonials Section module.
// The section is rendered as part of the home page via HomeController.
```
