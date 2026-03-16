<?php

namespace Modules\Blog\DTOs;

use Spatie\LaravelData\Data;

class BlogCategoryData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $name,
        public readonly ?string $description,
    ) {}
}
