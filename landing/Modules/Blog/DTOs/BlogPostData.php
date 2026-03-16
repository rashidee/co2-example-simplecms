<?php

namespace Modules\Blog\DTOs;

use Spatie\LaravelData\Data;

class BlogPostData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $title,
        public readonly string $slug,
        public readonly string $summary,
        public readonly ?string $content,
        public readonly string $effective_date,
        public readonly ?string $category_name,
        public readonly ?string $image_url,
        public readonly ?string $thumbnail_url,
    ) {}
}
