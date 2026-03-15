# Hero Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Hero Section
> **nwidart Module:** Modules/HeroSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000003 | As Public I want to be able to see list of hero content, so that I can understand the main message and value proposition of the landing page. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00003 | The carousel will slide every 5 seconds and it will have navigation arrows to allow users to manually slide the carousel. | v1.0.0 |
| NFRL00006 | The image size will be large image with a resolution of at least 1600x500 pixels to ensure that it looks good on all screen sizes. | v1.0.0 |

### 1.3 Constraints

| ID | Description | Version |
|----|-------------|---------|
| CONSL0006 | No header and footer in the hero section, only the content mentioned in the user story. | v1.0.0 |

---

## 2. Eloquent Model

### 2.1 HeroSection

**File:** `Modules/HeroSection/app/Models/HeroSection.php`

**Table:** `hero_sections` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\HeroSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class HeroSection extends Model
{
    protected $table = 'hero_sections';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    /**
     * Scope: only ACTIVE records within the current date range.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('status', 'ACTIVE')
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>', now());
    }
}
```

**Column mapping (snake_case in DB):**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| image_path | image_path | string(500) | Hero banner image path (1600x500) |
| headline | headline | string(100) | Headline text |
| subheadline | subheadline | string(200) | Sub-headline text |
| cta_url | cta_url | string(500) | CTA button link URL |
| cta_text | cta_text | string(50) | CTA button label |
| effective_date | effective_date | timestamp | Start date for display |
| expiration_date | expiration_date | timestamp | End date for display |
| status | status | string (enum) | DRAFT, READY, ACTIVE, EXPIRED |

---

## 3. Service

### 3.1 HeroSectionService

**File:** `Modules/HeroSection/app/Services/HeroSectionService.php`

```php
<?php

namespace Modules\HeroSection\Services;

use Illuminate\Support\Collection;
use Modules\HeroSection\Models\HeroSection;

class HeroSectionService
{
    /**
     * Get all active hero sections within the current date range,
     * ordered by effective_date descending (most recent first).
     *
     * @return Collection<int, HeroSection>
     */
    public function getActiveHeroes(): Collection
    {
        return HeroSection::query()
            ->active()
            ->orderBy('effective_date', 'desc')
            ->get();
    }
}
```

---

## 4. Data Transfer Object

### 4.1 HeroSectionData

**File:** `Modules/HeroSection/app/Data/HeroSectionData.php`

```php
<?php

namespace Modules\HeroSection\Data;

use Spatie\LaravelData\Data;

class HeroSectionData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $image_path,
        public readonly string $headline,
        public readonly string $subheadline,
        public readonly string $cta_url,
        public readonly string $cta_text,
    ) {}
}
```

---

## 5. ViewModel

### 5.1 HeroSectionViewModel

**File:** `Modules/HeroSection/app/ViewModels/HeroSectionViewModel.php`

```php
<?php

namespace Modules\HeroSection\ViewModels;

use Illuminate\Support\Collection;
use Spatie\ViewModels\ViewModel;
use Modules\HeroSection\Data\HeroSectionData;

class HeroSectionViewModel extends ViewModel
{
    public function __construct(
        private readonly Collection $heroes,
    ) {}

    /**
     * Transform hero models into DTOs for the view.
     *
     * @return Collection<int, HeroSectionData>
     */
    public function heroes(): Collection
    {
        return $this->heroes->map(fn ($hero) => new HeroSectionData(
            id: $hero->id,
            image_path: $hero->image_path,
            headline: $hero->headline,
            subheadline: $hero->subheadline,
            cta_url: $hero->cta_url,
            cta_text: $hero->cta_text,
        ));
    }
}
```

---

## 6. Controller

### 6.1 HeroSectionController

**File:** `Modules/HeroSection/app/Http/Controllers/HeroSectionController.php`

```php
<?php

namespace Modules\HeroSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\HeroSection\Services\HeroSectionService;

class HeroSectionController extends Controller
{
    public function __construct(
        private readonly HeroSectionService $heroSectionService,
    ) {}

    /**
     * Get active heroes for embedding in the landing page.
     * Called from HomeController, not directly routed.
     */
    public function getActiveHeroes()
    {
        return $this->heroSectionService->getActiveHeroes();
    }
}
```

**Note:** The hero section is rendered as part of the home page via `HomeController`. No dedicated route is needed for this module.

---

## 7. Blade Templates

### 7.1 Hero Carousel Component

**File:** `Modules/HeroSection/resources/views/components/hero-carousel.blade.php`

```blade
{{-- USL000003 [v1.0.0] - Hero content carousel --}}
{{-- NFRL00003 [v1.0.0] - Auto-slide every 5s, navigation arrows --}}
{{-- NFRL00006 [v1.0.0] - Image 1600x500 --}}
{{-- CONSL0006 [v1.0.0] - No header/footer in hero section --}}

@if($heroes->isNotEmpty())
<section id="hero" x-data="{
    current: 0,
    total: {{ $heroes->count() }},
    next() { this.current = (this.current + 1) % this.total },
    prev() { this.current = (this.current - 1 + this.total) % this.total },
    autoSlide: null,
    startAuto() { this.autoSlide = setInterval(() => this.next(), 5000) },
    stopAuto() { clearInterval(this.autoSlide) }
}" x-init="startAuto()" class="relative w-full overflow-hidden">

    {{-- Slides --}}
    @foreach($heroes as $index => $hero)
    <div x-show="current === {{ $index }}"
         x-transition:enter="transition ease-out duration-500"
         x-transition:enter-start="opacity-0"
         x-transition:enter-end="opacity-100"
         x-transition:leave="transition ease-in duration-300"
         x-transition:leave-start="opacity-100"
         x-transition:leave-end="opacity-0"
         class="relative w-full" style="min-height:500px;">
        <img src="{{ asset($hero->image_path) }}"
             alt="{{ $hero->headline }}"
             class="w-full h-[500px] object-cover">
        <div class="absolute inset-0 bg-black/40 flex flex-col items-center justify-center text-center px-4">
            <h1 class="text-white text-3xl md:text-5xl font-bold mb-4">{{ $hero->headline }}</h1>
            <p class="text-white/90 text-lg md:text-xl mb-6 max-w-2xl">{{ $hero->subheadline }}</p>
            <a href="{{ $hero->cta_url }}"
               target="_blank"
               rel="noopener"
               class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition">
                {{ $hero->cta_text }}
            </a>
        </div>
    </div>
    @endforeach

    {{-- Left arrow --}}
    <button @click="prev(); stopAuto(); startAuto()"
            class="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Previous slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
        </svg>
    </button>

    {{-- Right arrow --}}
    <button @click="next(); stopAuto(); startAuto()"
            class="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Next slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
        </svg>
    </button>

    {{-- Dot indicators --}}
    <div class="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
        @foreach($heroes as $index => $hero)
        <button @click="current = {{ $index }}; stopAuto(); startAuto()"
                :class="current === {{ $index }} ? 'bg-white' : 'bg-white/50'"
                class="w-3 h-3 rounded-full transition"
                aria-label="Go to slide {{ $index + 1 }}"></button>
        @endforeach
    </div>
</section>
@endif
```

---

## 8. Module Registration

### 8.1 module.json

**File:** `Modules/HeroSection/module.json`

```json
{
    "name": "HeroSection",
    "alias": "hero-section",
    "description": "Hero section carousel for the landing page",
    "keywords": ["hero", "carousel", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\HeroSection\\Providers\\HeroSectionServiceProvider"
    ],
    "files": []
}
```

### 8.2 Routes

**File:** `Modules/HeroSection/routes/web.php`

```php
<?php

// No dedicated routes for the Hero Section module.
// The hero section is rendered as part of the home page via HomeController.
```
