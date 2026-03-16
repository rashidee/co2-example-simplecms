<?php

namespace Modules\Blog\Services;

use Illuminate\Pagination\LengthAwarePaginator;
use Modules\Blog\Contracts\BlogServiceInterface;
use Modules\Blog\DTOs\BlogPostData;
use Modules\Blog\Exceptions\BlogException;
use Modules\Blog\Models\BlogPost;

class BlogService implements BlogServiceInterface
{
    public function listActivePosts(int $page = 1, int $perPage = 10): LengthAwarePaginator
    {
        $paginator = BlogPost::active()
            ->with('category')
            ->orderByDesc('effective_date')
            ->paginate(perPage: $perPage, page: $page);

        $paginator->through(function (BlogPost $post) {
            return new BlogPostData(
                id: $post->id,
                title: $post->title,
                slug: $post->slug,
                summary: $post->summary,
                content: null,
                effective_date: $post->effective_date->toDateString(),
                category_name: $post->category?->name,
                image_url: $post->image_data
                    ? route('image.show', ['table' => 'blog-post', 'id' => $post->id, 'column' => 'image_data'])
                    : null,
                thumbnail_url: $post->thumbnail_data
                    ? route('image.show', ['table' => 'blog-post', 'id' => $post->id, 'column' => 'thumbnail_data'])
                    : null,
            );
        });

        return $paginator;
    }

    public function getBySlug(string $slug): BlogPostData
    {
        $post = BlogPost::active()
            ->with('category')
            ->where('slug', $slug)
            ->first();

        if (!$post) {
            throw BlogException::postNotFound($slug);
        }

        return new BlogPostData(
            id: $post->id,
            title: $post->title,
            slug: $post->slug,
            summary: $post->summary,
            content: $post->content,
            effective_date: $post->effective_date->toDateString(),
            category_name: $post->category?->name,
            image_url: $post->image_data
                ? route('image.show', ['table' => 'blog-post', 'id' => $post->id, 'column' => 'image_data'])
                : null,
            thumbnail_url: $post->thumbnail_data
                ? route('image.show', ['table' => 'blog-post', 'id' => $post->id, 'column' => 'thumbnail_data'])
                : null,
        );
    }
}
