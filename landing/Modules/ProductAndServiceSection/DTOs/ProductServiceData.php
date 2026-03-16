<?php

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
