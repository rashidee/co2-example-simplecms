<?php

namespace Modules\Blog\ViewModels;

use Illuminate\Pagination\LengthAwarePaginator;
use Spatie\ViewModels\ViewModel;

class BlogListViewModel extends ViewModel
{
    public function __construct(
        public readonly LengthAwarePaginator $posts,
    ) {}
}
