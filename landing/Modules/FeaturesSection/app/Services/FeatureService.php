<?php

namespace Modules\FeaturesSection\Services;

use Illuminate\Support\Collection;
use Modules\FeaturesSection\Models\Feature;

class FeatureService
{
    public function getActiveFeatures(): Collection
    {
        return Feature::query()
            ->active()
            ->orderBy('display_order')
            ->get();
    }
}
