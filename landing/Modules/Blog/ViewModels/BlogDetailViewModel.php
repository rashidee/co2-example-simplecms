<?php

namespace Modules\Blog\ViewModels;

use Modules\Blog\DTOs\BlogPostData;
use Spatie\ViewModels\ViewModel;

class BlogDetailViewModel extends ViewModel
{
    public function __construct(
        public readonly BlogPostData $post,
    ) {}
}
