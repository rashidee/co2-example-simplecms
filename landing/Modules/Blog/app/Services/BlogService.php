<?php

namespace Modules\Blog\Services;

use Illuminate\Pagination\LengthAwarePaginator;
use Modules\Blog\Models\BlogPost;

class BlogService
{
    public function getActivePosts(int $perPage = 10): LengthAwarePaginator
    {
        return BlogPost::query()
            ->active()
            ->with('category')
            ->orderBy('effective_date', 'desc')
            ->paginate($perPage);
    }

    public function getPostBySlug(string $slug): ?BlogPost
    {
        return BlogPost::query()
            ->active()
            ->with('category')
            ->where('slug', $slug)
            ->first();
    }
}
