<?php

namespace Modules\Blog\Contracts;

use Illuminate\Pagination\LengthAwarePaginator;
use Modules\Blog\DTOs\BlogPostData;

interface BlogServiceInterface
{
    public function listActivePosts(int $page = 1, int $perPage = 10): LengthAwarePaginator;

    public function getBySlug(string $slug): BlogPostData;
}
