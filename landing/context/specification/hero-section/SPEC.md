# Hero Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000003 | As Public I want to be able to see list of hero content, so that I can understand the main message and value proposition of the landing page. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00003 | The carousel will slide every 5 seconds and it will have navigation arrows to allow users to manually slide the carousel. | v1.0.0 |
| NFRL00006 | The image size will be large image with a resolution of at least 1600x500 pixels to ensure that it looks good on all screen sizes. | v1.0.0 |

### Constraints
| ID | Description | Version |
|----|-------------|---------|
| CONSL0006 | No header and footer in the hero section, only the content mentioned in the user story. | v1.0.0 |

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Table | `hrs_hero_section` |
| Mockup Screen | `mockup/pages/home.html` (Hero Section) |
| Admin Model | `admin/context/model/hero-section/model.md` |

## 2. Service Contract

```php
namespace Modules\HeroSection\Contracts;

use Illuminate\Support\Collection;

interface HeroSectionServiceInterface
{
    /**
     * Get all active hero slides (status=ACTIVE, within date range).
     * Ordered by effective_date ascending.
     *
     * @return Collection<HeroSectionData>
     */
    public function getActiveSlides(): Collection;
}
```

## 3. DTO (spatie/laravel-data)

```php
namespace Modules\HeroSection\DTOs;

use Spatie\LaravelData\Data;

class HeroSectionData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $headline,
        public readonly string $subheadline,
        public readonly string $cta_url,
        public readonly string $cta_text,
        public readonly string $effective_date,
        public readonly string $expiration_date,
        public readonly string $status,
        public readonly ?string $image_url,
    ) {}
}
```

**Note:** `image_url` is a computed field — the route URL for the BLOB image endpoint (e.g., `/images/hero-section/{id}/image_data`), NOT the raw binary data.

## 4. Exception

```php
namespace Modules\HeroSection\Exceptions;

use App\Exceptions\WebApplicationException;

class HeroSectionException extends WebApplicationException
{
    public static function noActiveSlides(): self
    {
        return new self('No active hero slides available', 404);
    }
}
```

## 5. Eloquent Model

```php
namespace Modules\HeroSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class HeroSection extends Model
{
    use HasUuids;

    protected $table = 'hrs_hero_section';

    public $timestamps = true;

    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    protected $fillable = [
        'image_path',
        'image_data',
        'thumbnail_path',
        'thumbnail_data',
        'headline',
        'subheadline',
        'cta_url',
        'cta_text',
        'effective_date',
        'expiration_date',
        'status',
    ];

    protected $casts = [
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    /**
     * Scope: active slides — status is auto-computed from dates in v1.0.4.
     * Active means: effective_date <= now AND expiration_date >= now.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>=', now());
    }
}
```

## 6. Migration

**Not applicable.** The `hrs_hero_section` table is owned and migrated by the Admin Portal. The Landing Page only reads from it.

## 7. Service Implementation

```php
namespace Modules\HeroSection\Services;

use Illuminate\Support\Collection;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\HeroSection\DTOs\HeroSectionData;
use Modules\HeroSection\Models\HeroSection;

class HeroSectionService implements HeroSectionServiceInterface
{
    public function getActiveSlides(): Collection
    {
        $slides = HeroSection::active()
            ->orderBy('effective_date')
            ->get();

        return $slides->map(function (HeroSection $slide) {
            return new HeroSectionData(
                id: $slide->id,
                headline: $slide->headline,
                subheadline: $slide->subheadline,
                cta_url: $slide->cta_url,
                cta_text: $slide->cta_text,
                effective_date: $slide->effective_date->toDateString(),
                expiration_date: $slide->expiration_date->toDateString(),
                status: $slide->status,
                image_url: $slide->image_data
                    ? route('image.show', ['table' => 'hero-section', 'id' => $slide->id, 'column' => 'image_data'])
                    : null,
            );
        });
    }
}
```

## 8. View Model

```php
namespace Modules\HeroSection\ViewModels;

use Illuminate\Support\Collection;

class HeroSectionViewModel
{
    public function __construct(
        public readonly Collection $slides,
    ) {}
}
```

## 9. Blade Template (Section in Home Page)

The hero section renders as part of the home page (`resources/views/pages/home.blade.php`):

```blade
{{-- Hero Section — USL000003, NFRL00003, NFRL00006, CONSL0006 --}}
{{-- No header/footer within hero per CONSL0006 --}}
<section id="hero" x-data="{
    current: 0,
    slides: @js($heroSlides->map(fn($s) => [
        'img' => $s->image_url,
        'headline' => $s->headline,
        'sub' => $s->subheadline,
        'cta' => $s->cta_text,
        'url' => $s->cta_url,
    ])->values()),
    next() { this.current = (this.current + 1) % this.slides.length },
    prev() { this.current = (this.current - 1 + this.slides.length) % this.slides.length },
    autoSlide: null,
    startAuto() { this.autoSlide = setInterval(() => this.next(), 5000) },
    stopAuto() { clearInterval(this.autoSlide) }
}" x-init="startAuto()" class="relative w-full overflow-hidden">
    {{-- Slides --}}
    <template x-for="(slide, idx) in slides" :key="idx">
        <div x-show="current === idx"
             x-transition:enter="transition ease-out duration-500"
             x-transition:enter-start="opacity-0"
             x-transition:enter-end="opacity-100"
             x-transition:leave="transition ease-in duration-300"
             x-transition:leave-start="opacity-100"
             x-transition:leave-end="opacity-0"
             class="relative w-full" style="min-height:500px;">
            <img :src="slide.img" :alt="slide.headline" class="w-full h-[500px] object-cover">
            <div class="absolute inset-0 bg-black/40 flex flex-col items-center justify-center text-center px-4">
                <h1 class="text-white text-3xl md:text-5xl font-bold mb-4" x-text="slide.headline"></h1>
                <p class="text-white/90 text-lg md:text-xl mb-6 max-w-2xl" x-text="slide.sub"></p>
                <a :href="slide.url" target="_blank" rel="noopener"
                   class="inline-block bg-primary hover:bg-primary-dark text-white font-semibold py-3 px-8 rounded-btn transition"
                   x-text="slide.cta"></a>
            </div>
        </div>
    </template>

    {{-- Navigation arrows --}}
    <button @click="prev(); stopAuto(); startAuto()"
            class="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Previous slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
        </svg>
    </button>
    <button @click="next(); stopAuto(); startAuto()"
            class="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white text-text-primary rounded-full p-2 shadow"
            aria-label="Next slide">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
        </svg>
    </button>

    {{-- Dot indicators --}}
    <div class="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
        <template x-for="(slide, idx) in slides" :key="'dot-'+idx">
            <button @click="current = idx; stopAuto(); startAuto()"
                    :class="current === idx ? 'bg-white' : 'bg-white/50'"
                    class="w-3 h-3 rounded-full transition"
                    :aria-label="'Go to slide '+(idx+1)"></button>
        </template>
    </div>
</section>
```

## 10. Module Routes

No dedicated routes — the hero section is rendered as part of the home page via `HomeController::index()`.

## 11. Module Service Provider

```php
namespace Modules\HeroSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\HeroSection\Services\HeroSectionService;

class HeroSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(HeroSectionServiceInterface::class, HeroSectionService::class);
    }

    public function boot(): void
    {
        // No migrations — Admin Portal owns the schema
        // No views — rendered as section in home page
        // No routes — data served via HomeController
    }
}
```
