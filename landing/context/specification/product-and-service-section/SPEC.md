# Product and Service Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Product and Service Section
> **nwidart Module:** Modules/ProductAndServiceSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000006 | As Public I want to be able to see list of products and services, so that I can understand what products and services are offered by the company. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00009 | Section title: "Our Products and Services" | v1.0.0 |
| NFRL00012 | The card layout will be a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00015 | The image size will be medium image with a resolution of at least 400x400 pixels to ensure that it looks good on all screen sizes. | v1.0.0 |
| NFRL00018 | White background for the section, and the card will have a shadow effect to make it stand out from the background. | v1.0.0 |

### 1.3 Constraints

_No constraints specific to this module._

---

## 2. Eloquent Model

### 2.1 ProductService

**File:** `Modules/ProductAndServiceSection/app/Models/ProductService.php`

**Table:** `product_services` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\ProductAndServiceSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class ProductService extends Model
{
    protected $table = 'product_services';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
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
| image_path | image_path | string(500) | Product image (400x400) |
| title | title | string(100) | Product/service title |
| description | description | string(500) | Short description |
| cta_url | cta_url | string(500) | Nullable, link to detail |
| cta_text | cta_text | string(50) | Nullable, CTA button label |
| display_order | display_order | integer | Display sorting |
| status | status | string (enum) | DRAFT, INACTIVE, ACTIVE |

---

## 3. Service

### 3.1 ProductServiceService

**File:** `Modules/ProductAndServiceSection/app/Services/ProductServiceService.php`

```php
<?php

namespace Modules\ProductAndServiceSection\Services;

use Illuminate\Support\Collection;
use Modules\ProductAndServiceSection\Models\ProductService;

class ProductServiceService
{
    /**
     * Get all active products/services ordered by display_order.
     *
     * @return Collection<int, ProductService>
     */
    public function getActiveProducts(): Collection
    {
        return ProductService::query()
            ->active()
            ->ordered()
            ->get();
    }
}
```

---

## 4. Data Transfer Object

### 4.1 ProductServiceData

**File:** `Modules/ProductAndServiceSection/app/Data/ProductServiceData.php`

```php
<?php

namespace Modules\ProductAndServiceSection\Data;

use Spatie\LaravelData\Data;

class ProductServiceData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $image_path,
        public readonly string $title,
        public readonly string $description,
        public readonly ?string $cta_url,
        public readonly ?string $cta_text,
    ) {}
}
```

---

## 5. ViewModel

### 5.1 ProductServiceViewModel

**File:** `Modules/ProductAndServiceSection/app/ViewModels/ProductServiceViewModel.php`

```php
<?php

namespace Modules\ProductAndServiceSection\ViewModels;

use Illuminate\Support\Collection;
use Spatie\ViewModels\ViewModel;
use Modules\ProductAndServiceSection\Data\ProductServiceData;

class ProductServiceViewModel extends ViewModel
{
    public function __construct(
        private readonly Collection $products,
    ) {}

    /**
     * @return Collection<int, ProductServiceData>
     */
    public function products(): Collection
    {
        return $this->products->map(fn ($product) => new ProductServiceData(
            id: $product->id,
            image_path: $product->image_path,
            title: $product->title,
            description: $product->description,
            cta_url: $product->cta_url,
            cta_text: $product->cta_text,
        ));
    }
}
```

---

## 6. Controller

### 6.1 ProductServiceController

**File:** `Modules/ProductAndServiceSection/app/Http/Controllers/ProductServiceController.php`

```php
<?php

namespace Modules\ProductAndServiceSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\ProductAndServiceSection\Services\ProductServiceService;

class ProductServiceController extends Controller
{
    public function __construct(
        private readonly ProductServiceService $productServiceService,
    ) {}

    /**
     * Get active products for embedding in the landing page.
     * Called from HomeController, not directly routed.
     */
    public function getActiveProducts()
    {
        return $this->productServiceService->getActiveProducts();
    }
}
```

**Note:** The product section is rendered as part of the home page via `HomeController`. No dedicated route is needed for this module.

---

## 7. Blade Templates

### 7.1 Product and Service Section Component

**File:** `Modules/ProductAndServiceSection/resources/views/components/product-service-grid.blade.php`

```blade
{{-- USL000006 [v1.0.0] - Products and services listing --}}
{{-- NFRL00009 [v1.0.0] - Section title: "Our Products and Services" --}}
{{-- NFRL00012 [v1.0.0] - 3-col desktop, 1-col mobile --}}
{{-- NFRL00015 [v1.0.0] - Image 400x400 --}}
{{-- NFRL00018 [v1.0.0] - White bg, shadow cards --}}

<section id="products" class="bg-white py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Our Products and Services</h2>

        @if($products->isNotEmpty())
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($products as $product)
            <div class="bg-surface border border-border rounded-card shadow hover:shadow-lg transition overflow-hidden">
                <img src="{{ asset($product->image_path) }}"
                     alt="{{ $product->title }}"
                     class="w-full h-48 object-cover">
                <div class="p-6">
                    <h3 class="text-lg font-bold mb-2">{{ $product->title }}</h3>
                    <p class="text-text-secondary text-sm mb-4">{{ $product->description }}</p>
                    @if($product->cta_url)
                    <a href="{{ $product->cta_url }}"
                       target="_blank"
                       rel="noopener"
                       class="text-primary font-semibold text-sm hover:text-primary-dark transition">
                        {{ $product->cta_text ?? 'Learn More' }} &rarr;
                    </a>
                    @endif
                </div>
            </div>
            @endforeach
        </div>
        @endif
    </div>
</section>
```

---

## 8. Module Registration

### 8.1 module.json

**File:** `Modules/ProductAndServiceSection/module.json`

```json
{
    "name": "ProductAndServiceSection",
    "alias": "product-and-service-section",
    "description": "Product and service grid for the landing page",
    "keywords": ["product", "service", "grid", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\ProductAndServiceSection\\Providers\\ProductAndServiceSectionServiceProvider"
    ],
    "files": []
}
```

### 8.2 Routes

**File:** `Modules/ProductAndServiceSection/routes/web.php`

```php
<?php

// No dedicated routes for the Product and Service Section module.
// The section is rendered as part of the home page via HomeController.
```
