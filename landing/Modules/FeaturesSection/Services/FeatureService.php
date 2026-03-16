<?php

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
