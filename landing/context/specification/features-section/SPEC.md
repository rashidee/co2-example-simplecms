# Features Section - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Features Section
> **nwidart Module:** Modules/FeaturesSection

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000009 | As Public I want to be able to see list of features and benefits, so that I can understand the key features and benefits of the products and services offered by the company. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00021 | Section title: "Key Features and Benefits" | v1.0.0 |
| NFRL00024 | The features will be displayed in a grid layout with 3 columns on desktop and 1 column on mobile devices. | v1.0.0 |
| NFRL00027 | The icons size will be a font icon with a size of at least 48px to ensure that it looks good on all screen sizes. | v1.0.0 |
| NFRL00030 | Light gray background for the section, and the feature item will have a shadow effect to make it stand out from the background. | v1.0.0 |

### 1.3 Constraints

_No constraints specific to this module._

---

## 2. Eloquent Model

### 2.1 Feature

**File:** `Modules/FeaturesSection/app/Models/Feature.php`

**Table:** `features` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\FeaturesSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class Feature extends Model
{
    protected $table = 'features';

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
| icon | icon | string(100) | FontAwesome icon class name |
| title | title | string(100) | Feature title |
| description | description | string(500) | Short description |
| display_order | display_order | integer | Display sorting |
| status | status | string (enum) | DRAFT, INACTIVE, ACTIVE |

---

## 3. Service

### 3.1 FeatureService

**File:** `Modules/FeaturesSection/app/Services/FeatureService.php`

```php
<?php

namespace Modules\FeaturesSection\Services;

use Illuminate\Support\Collection;
use Modules\FeaturesSection\Models\Feature;

class FeatureService
{
    /**
     * Get all active features ordered by display_order.
     *
     * @return Collection<int, Feature>
     */
    public function getActiveFeatures(): Collection
    {
        return Feature::query()
            ->active()
            ->ordered()
            ->get();
    }
}
```

---

## 4. Data Transfer Object

### 4.1 FeatureData

**File:** `Modules/FeaturesSection/app/Data/FeatureData.php`

```php
<?php

namespace Modules\FeaturesSection\Data;

use Spatie\LaravelData\Data;

class FeatureData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $icon,
        public readonly string $title,
        public readonly string $description,
    ) {}
}
```

---

## 5. ViewModel

### 5.1 FeatureViewModel

**File:** `Modules/FeaturesSection/app/ViewModels/FeatureViewModel.php`

```php
<?php

namespace Modules\FeaturesSection\ViewModels;

use Illuminate\Support\Collection;
use Spatie\ViewModels\ViewModel;
use Modules\FeaturesSection\Data\FeatureData;

class FeatureViewModel extends ViewModel
{
    public function __construct(
        private readonly Collection $features,
    ) {}

    /**
     * @return Collection<int, FeatureData>
     */
    public function features(): Collection
    {
        return $this->features->map(fn ($feature) => new FeatureData(
            id: $feature->id,
            icon: $feature->icon,
            title: $feature->title,
            description: $feature->description,
        ));
    }
}
```

---

## 6. Controller

### 6.1 FeatureController

**File:** `Modules/FeaturesSection/app/Http/Controllers/FeatureController.php`

```php
<?php

namespace Modules\FeaturesSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\FeaturesSection\Services\FeatureService;

class FeatureController extends Controller
{
    public function __construct(
        private readonly FeatureService $featureService,
    ) {}

    /**
     * Get active features for embedding in the landing page.
     * Called from HomeController, not directly routed.
     */
    public function getActiveFeatures()
    {
        return $this->featureService->getActiveFeatures();
    }
}
```

**Note:** The features section is rendered as part of the home page via `HomeController`. No dedicated route is needed.

---

## 7. Blade Templates

### 7.1 Features Section Component

**File:** `Modules/FeaturesSection/resources/views/components/features-grid.blade.php`

```blade
{{-- USL000009 [v1.0.0] - Features and benefits listing --}}
{{-- NFRL00021 [v1.0.0] - Section title: "Key Features and Benefits" --}}
{{-- NFRL00024 [v1.0.0] - 3-col desktop, 1-col mobile --}}
{{-- NFRL00027 [v1.0.0] - Icon 48px --}}
{{-- NFRL00030 [v1.0.0] - Light gray bg, shadow cards --}}

<section id="features" class="bg-page-bg py-16 md:py-24">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="text-3xl md:text-4xl font-bold text-center mb-12">Key Features and Benefits</h2>

        @if($features->isNotEmpty())
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            @foreach($features as $feature)
            <div class="bg-surface border border-border rounded-card shadow p-8 text-center hover:shadow-lg transition">
                <div class="inline-flex items-center justify-center mb-4">
                    {{-- Render FontAwesome icon class from DB --}}
                    <i class="{{ $feature->icon }} text-primary" style="font-size: 48px;"></i>
                </div>
                <h3 class="text-lg font-bold mb-2">{{ $feature->title }}</h3>
                <p class="text-text-secondary text-sm">{{ $feature->description }}</p>
            </div>
            @endforeach
        </div>
        @endif
    </div>
</section>
```

**Icon rendering note:** The `icon` field stores a FontAwesome class name (e.g., `fa-solid fa-globe`). The Blade template renders it as an `<i>` element with the class. A FontAwesome CDN link should be included in the layout `<head>`, or SVG icon alternatives can be used if the Admin Portal stores SVG markup instead.

---

## 8. Module Registration

### 8.1 module.json

**File:** `Modules/FeaturesSection/module.json`

```json
{
    "name": "FeaturesSection",
    "alias": "features-section",
    "description": "Features and benefits grid for the landing page",
    "keywords": ["features", "benefits", "grid", "landing"],
    "priority": 0,
    "providers": [
        "Modules\\FeaturesSection\\Providers\\FeaturesSectionServiceProvider"
    ],
    "files": []
}
```

### 8.2 Routes

**File:** `Modules/FeaturesSection/routes/web.php`

```php
<?php

// No dedicated routes for the Features Section module.
// The section is rendered as part of the home page via HomeController.
```
