# Features Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000009 | As Public I want to be able to see list of features and benefits, so that I can understand the key features and benefits of the products and services offered by the company. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00021 | Section title: "Key Features and Benefits" | v1.0.0 |
| NFRL00024 | The features will be displayed in a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00027 | The icons size will be a font icon with a size of at least 48px. | v1.0.0 |
| NFRL00030 | Light gray background for the section, and the feature item will have a shadow effect. | v1.0.0 |

### Constraints
_None for this module._

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Table | `fts_feature` |
| Mockup Screen | `mockup/pages/home.html` (Features Section) |
| Admin Model | `admin/context/model/features-section/model.md` |

## 2. Service Contract

```php
namespace Modules\FeaturesSection\Contracts;

use Illuminate\Support\Collection;

interface FeatureServiceInterface
{
    /**
     * Get all visible features.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending.
     *
     * @return Collection<FeatureData>
     */
    public function getAllVisible(): Collection;
}
```

## 3. DTO

```php
namespace Modules\FeaturesSection\DTOs;

use Spatie\LaravelData\Data;

class FeatureData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $icon,
        public readonly string $title,
        public readonly string $description,
        public readonly int $display_order,
    ) {}
}
```

## 4. Exception

```php
namespace Modules\FeaturesSection\Exceptions;

use App\Exceptions\WebApplicationException;

class FeatureException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Feature not found: {$id}", 404);
    }
}
```

## 5. Eloquent Model

```php
namespace Modules\FeaturesSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class Feature extends Model
{
    use HasUuids;

    protected $table = 'fts_feature';

    protected $fillable = [
        'icon', 'title', 'description', 'display_order',
    ];

    protected $casts = [
        'display_order' => 'integer',
    ];
}
```

## 6. Migration

**Not applicable.** Admin Portal owns the schema.

## 7. Service Implementation

```php
namespace Modules\FeaturesSection\Services;

use Illuminate\Support\Collection;
use Modules\FeaturesSection\Contracts\FeatureServiceInterface;
use Modules\FeaturesSection\DTOs\FeatureData;
use Modules\FeaturesSection\Models\Feature;

class FeatureService implements FeatureServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed
        return Feature::orderBy('display_order')
            ->get()
            ->map(fn (Feature $f) => new FeatureData(
                id: $f->id,
                icon: $f->icon,
                title: $f->title,
                description: $f->description,
                display_order: $f->display_order,
            ));
    }
}
```

## 8. Blade Template (Section in Home Page)

```blade
{{-- Features Section — USL000009, NFRL00021, NFRL00024, NFRL00027, NFRL00030 --}}
<section id="features" class="bg-page-bg py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Key Features and Benefits</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($features as $feature)
            <div class="bg-surface border border-border rounded-card shadow p-8 text-center hover:shadow-lg transition">
                <div class="inline-flex items-center justify-center mb-4">
                    {{-- FontAwesome icon class from database --}}
                    <i class="{{ $feature->icon }} text-primary" style="font-size: 48px;"></i>
                </div>
                <h3 class="text-lg font-bold mb-2">{{ $feature->title }}</h3>
                <p class="text-text-secondary text-sm">{{ $feature->description }}</p>
            </div>
            @endforeach
        </div>
    </div>
</section>
```

**Note:** The `icon` field stores a FontAwesome class name (e.g., `fa-solid fa-globe`). Include FontAwesome CSS in the layout or via CDN.

## 9. Module Service Provider

```php
namespace Modules\FeaturesSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\FeaturesSection\Contracts\FeatureServiceInterface;
use Modules\FeaturesSection\Services\FeatureService;

class FeaturesSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(FeatureServiceInterface::class, FeatureService::class);
    }

    public function boot(): void {}
}
```
