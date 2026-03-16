<?php

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
