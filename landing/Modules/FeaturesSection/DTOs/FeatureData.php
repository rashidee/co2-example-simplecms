<?php

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
