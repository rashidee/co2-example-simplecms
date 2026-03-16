# Product and Service Section — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000006 | As Public I want to be able to see list of products and services, so that I can understand what products and services are offered by the company. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00009 | Section title: "Our Products and Services" | v1.0.0 |
| NFRL00012 | The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00015 | The image size will be medium image with a resolution of at least 400x400 pixels. | v1.0.0 |
| NFRL00018 | White background for the section, and the card will have a shadow effect. | v1.0.0 |

### Constraints
_None for this module._

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Table | `pas_product_service` |
| Mockup Screen | `mockup/pages/home.html` (Products & Services Section) |
| Admin Model | `admin/context/model/product-and-service-section/model.md` |

## 2. Service Contract

```php
namespace Modules\ProductAndServiceSection\Contracts;

use Illuminate\Support\Collection;

interface ProductServiceServiceInterface
{
    /**
     * Get all visible product/service items.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending, then created_at ascending.
     *
     * @return Collection<ProductServiceData>
     */
    public function getAllVisible(): Collection;
}
```

## 3. DTO

```php
namespace Modules\ProductAndServiceSection\DTOs;

use Spatie\LaravelData\Data;

class ProductServiceData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $title,
        public readonly string $description,
        public readonly ?string $cta_url,
        public readonly ?string $cta_text,
        public readonly int $display_order,
        public readonly ?string $image_url,
    ) {}
}
```

## 4. Exception

```php
namespace Modules\ProductAndServiceSection\Exceptions;

use App\Exceptions\WebApplicationException;

class ProductServiceException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Product/Service not found: {$id}", 404);
    }
}
```

## 5. Eloquent Model

```php
namespace Modules\ProductAndServiceSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ProductService extends Model
{
    use HasUuids;

    protected $table = 'pas_product_service';

    protected $fillable = [
        'image_path', 'image_data', 'thumbnail_path', 'thumbnail_data',
        'title', 'description', 'cta_url', 'cta_text', 'display_order',
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
namespace Modules\ProductAndServiceSection\Services;

use Illuminate\Support\Collection;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\ProductAndServiceSection\DTOs\ProductServiceData;
use Modules\ProductAndServiceSection\Models\ProductService;

class ProductServiceService implements ProductServiceServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed, all items visible
        $items = ProductService::orderBy('display_order')
            ->orderBy('created_at')
            ->get();

        return $items->map(fn (ProductService $item) => new ProductServiceData(
            id: $item->id,
            title: $item->title,
            description: $item->description,
            cta_url: $item->cta_url,
            cta_text: $item->cta_text,
            display_order: $item->display_order,
            image_url: $item->image_data
                ? route('image.show', ['table' => 'product-service', 'id' => $item->id, 'column' => 'image_data'])
                : null,
        ));
    }
}
```

## 8. Blade Template (Section in Home Page)

```blade
{{-- Products & Services Section — USL000006, NFRL00009, NFRL00012, NFRL00015, NFRL00018 --}}
<section id="products" class="bg-white py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Our Products and Services</h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($products as $product)
            <x-card>
                @if($product->image_url)
                    <img src="{{ $product->image_url }}" alt="{{ $product->title }}" class="w-full h-48 object-cover">
                @endif
                <div class="p-6">
                    <h3 class="text-lg font-bold mb-2">{{ $product->title }}</h3>
                    <p class="text-text-secondary text-sm mb-4">{{ $product->description }}</p>
                    @if($product->cta_url)
                        <a href="{{ $product->cta_url }}" target="_blank" rel="noopener"
                           class="text-primary font-semibold text-sm hover:text-primary-dark transition">
                            {{ $product->cta_text ?? 'Learn More' }} &rarr;
                        </a>
                    @endif
                </div>
            </x-card>
            @endforeach
        </div>
    </div>
</section>
```

## 9. Module Service Provider

```php
namespace Modules\ProductAndServiceSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\ProductAndServiceSection\Services\ProductServiceService;

class ProductAndServiceSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(ProductServiceServiceInterface::class, ProductServiceService::class);
    }

    public function boot(): void
    {
        // No migrations, no views, no routes — section rendered via HomeController
    }
}
```
