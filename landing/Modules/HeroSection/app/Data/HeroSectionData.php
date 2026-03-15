<?php

namespace Modules\HeroSection\Data;

use Spatie\LaravelData\Data;

class HeroSectionData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $image_path,
        public readonly string $headline,
        public readonly string $subheadline,
        public readonly string $cta_url,
        public readonly string $cta_text,
    ) {}
}
