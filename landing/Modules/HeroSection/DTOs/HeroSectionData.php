<?php

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
