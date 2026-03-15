# Blog - Module Specification

> **Version:** v1.0.0
> **Date:** 2026-03-15
> **Module:** Blog
> **nwidart Module:** Modules/Blog

---

## 1. Traceability

### 1.1 User Stories

| ID | Description | Version |
|----|-------------|---------|
| USL000021 | As Public I want to be able to see a list of blog articles, so that I can stay updated with the latest news and insights about the company and its products and services. | v1.0.0 |
| USL000024 | As Public I want to be able to view the blog article detail page, so that I can read the full content of the blog article and understand the insights and information shared by the company. | v1.0.0 |

### 1.2 Non-Functional Requirements

| ID | Description | Version |
|----|-------------|---------|
| NFRL00087 | Section title: "Our Blog" | v1.0.0 |
| NFRL00090 | The blog list will be a vertical card layout with the blog thumbnail image on the left and the title and short description on the right on desktop, and a vertical card layout with the thumbnail image on top and the title and short description below on mobile devices. | v1.0.0 |
| NFRL00093 | The image size will be medium image with a resolution of at least 800x600 pixels to ensure that it looks good on all screen sizes. | v1.0.0 |
| NFRL00096 | The title will be displayed in a bold font with a size of at least 18px to ensure that it stands out from the description text. | v1.0.0 |
| NFRL00099 | The short description will be displayed in a regular font with a size of at least 16px to ensure that it is easy to read on all screen sizes. | v1.0.0 |
| NFRL00102 | The blog article detail page will have a white background, and the content will be displayed in a single column layout with a maximum width of 800px to ensure that it is easy to read on all screen sizes. | v1.0.0 |

### 1.3 Constraints

| ID | Description | Version |
|----|-------------|---------|
| CONSL0009 | The list will be paginated with 10 articles per page, and there will be a pagination navigation at the bottom of the blog directory page to allow users to navigate through the pages of blog articles. | v1.0.0 |

---

## 2. Eloquent Models

### 2.1 BlogCategory

**File:** `Modules/Blog/app/Models/BlogCategory.php`

**Table:** `blog_categories` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\Blog\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;

class BlogCategory extends Model
{
    protected $table = 'blog_categories';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
    ];

    /**
     * A category has many blog posts.
     */
    public function posts(): HasMany
    {
        return $this->hasMany(BlogPost::class, 'category_id');
    }
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| name | name | string(100) | Category name, unique |

### 2.2 BlogPost

**File:** `Modules/Blog/app/Models/BlogPost.php`

**Table:** `blog_posts` (shared with Admin Portal, read-only)

```php
<?php

namespace Modules\Blog\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class BlogPost extends Model
{
    protected $table = 'blog_posts';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'category_id' => 'string',
        'effective_date' => 'datetime',
    ];

    /**
     * A blog post belongs to a category.
     */
    public function category(): BelongsTo
    {
        return $this->belongsTo(BlogCategory::class, 'category_id');
    }

    /**
     * Scope: only ACTIVE records.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query->where('status', 'ACTIVE');
    }

    /**
     * Scope: order by effective_date descending (most recent first).
     */
    public function scopeLatest(Builder $query): Builder
    {
        return $query->orderBy('effective_date', 'desc');
    }

    /**
     * Get the SEO-friendly URL for this blog post.
     */
    public function getUrlAttribute(): string
    {
        return '/' . $this->slug;
    }
}
```

**Column mapping:**

| Model Attribute | DB Column | Type | Notes |
|----------------|-----------|------|-------|
| id | id | UUID | PK |
| category_id | category_id | UUID | FK to blog_categories |
| title | title | string(100) | Blog post title |
| slug | slug | string(255) | Unique, SEO-friendly URL segment |
| summary | summary | string(300) | Short description for listing cards |
| content | content | text | Full HTML content for detail page |
| image_path | image_path | string(500) | Blog post image (1600x500 for detail, 800x600 thumbnail) |
| effective_date | effective_date | timestamp | Publication date, used for ordering |
| status | status | string (enum) | DRAFT, READY, ACTIVE, EXPIRED |

---

## 3. Service

### 3.1 BlogService

**File:** `Modules/Blog/app/Services/BlogService.php`

```php
<?php

namespace Modules\Blog\Services;

use Illuminate\Pagination\LengthAwarePaginator;
use Modules\Blog\Models\BlogPost;

class BlogService
{
    private const POSTS_PER_PAGE = 10; // CONSL0009

    /**
     * Get paginated list of active blog posts, ordered by effective_date DESC.
     *
     * @param int $page
     * @return LengthAwarePaginator
     */
    public function getActivePosts(int $page = 1): LengthAwarePaginator
    {
        return BlogPost::query()
            ->active()
            ->latest()
            ->with('category')
            ->paginate(self::POSTS_PER_PAGE, ['*'], 'page', $page);
    }

    /**
     * Get a single active blog post by its slug.
     * Returns null if not found or not ACTIVE.
     *
     * @param string $slug
     * @return BlogPost|null
     */
    public function getPostBySlug(string $slug): ?BlogPost
    {
        return BlogPost::query()
            ->active()
            ->where('slug', $slug)
            ->with('category')
            ->first();
    }
}
```

---

## 4. Data Transfer Objects

### 4.1 BlogPostListData

**File:** `Modules/Blog/app/Data/BlogPostListData.php`

```php
<?php

namespace Modules\Blog\Data;

use Spatie\LaravelData\Data;

class BlogPostListData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $title,
        public readonly string $slug,
        public readonly string $summary,
        public readonly string $image_path,
        public readonly string $effective_date,
        public readonly ?string $category_name,
    ) {}
}
```

### 4.2 BlogPostDetailData

**File:** `Modules/Blog/app/Data/BlogPostDetailData.php`

```php
<?php

namespace Modules\Blog\Data;

use Spatie\LaravelData\Data;

class BlogPostDetailData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $title,
        public readonly string $slug,
        public readonly string $content,
        public readonly string $image_path,
        public readonly string $effective_date,
        public readonly ?string $category_name,
    ) {}
}
```

---

## 5. ViewModels

### 5.1 BlogDirectoryViewModel

**File:** `Modules/Blog/app/ViewModels/BlogDirectoryViewModel.php`

```php
<?php

namespace Modules\Blog\ViewModels;

use Illuminate\Pagination\LengthAwarePaginator;
use Spatie\ViewModels\ViewModel;
use Modules\Blog\Data\BlogPostListData;

class BlogDirectoryViewModel extends ViewModel
{
    public function __construct(
        private readonly LengthAwarePaginator $posts,
    ) {}

    /**
     * Transform paginated posts into DTOs.
     */
    public function posts(): LengthAwarePaginator
    {
        $transformed = $this->posts->getCollection()->map(
            fn ($post) => new BlogPostListData(
                id: $post->id,
                title: $post->title,
                slug: $post->slug,
                summary: $post->summary,
                image_path: $post->image_path,
                effective_date: $post->effective_date->format('F j, Y'),
                category_name: $post->category?->name,
            )
        );

        return $this->posts->setCollection($transformed);
    }
}
```

### 5.2 BlogDetailViewModel

**File:** `Modules/Blog/app/ViewModels/BlogDetailViewModel.php`

```php
<?php

namespace Modules\Blog\ViewModels;

use Spatie\ViewModels\ViewModel;
use Modules\Blog\Data\BlogPostDetailData;
use Modules\Blog\Models\BlogPost;

class BlogDetailViewModel extends ViewModel
{
    public function __construct(
        private readonly BlogPost $post,
    ) {}

    public function post(): BlogPostDetailData
    {
        return new BlogPostDetailData(
            id: $this->post->id,
            title: $this->post->title,
            slug: $this->post->slug,
            content: $this->post->content,
            image_path: $this->post->image_path,
            effective_date: $this->post->effective_date->format('F j, Y'),
            category_name: $this->post->category?->name,
        );
    }
}
```

---

## 6. Controller

### 6.1 BlogController

**File:** `Modules/Blog/app/Http/Controllers/BlogController.php`

```php
<?php

namespace Modules\Blog\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Modules\Blog\Services\BlogService;
use Modules\Blog\ViewModels\BlogDirectoryViewModel;
use Modules\Blog\ViewModels\BlogDetailViewModel;

class BlogController extends Controller
{
    public function __construct(
        private readonly BlogService $blogService,
    ) {}

    /**
     * Display the blog directory page with paginated posts.
     *
     * Route: GET /blog
     */
    public function directory(Request $request)
    {
        $page = (int) $request->query('page', 1);
        $posts = $this->blogService->getActivePosts($page);

        $viewModel = new BlogDirectoryViewModel($posts);

        // If htmx request (pagination), return just the blog list fragment
        if ($request->header('HX-Request')) {
            return view('blog::partials.blog-list', $viewModel);
        }

        return view('blog::directory', $viewModel);
    }

    /**
     * Display a single blog post detail page.
     *
     * Route: GET /{slug}
     */
    public function detail(string $slug)
    {
        $post = $this->blogService->getPostBySlug($slug);

        if ($post === null) {
            abort(404);
        }

        $viewModel = new BlogDetailViewModel($post);

        return view('blog::detail', $viewModel);
    }
}
```

---

## 7. Blade Templates

### 7.1 Blog Directory Page

**File:** `Modules/Blog/resources/views/directory.blade.php`

```blade
{{-- USL000021 [v1.0.0] - Blog directory page --}}
{{-- NFRL00087 [v1.0.0] - Title "Our Blog" --}}
{{-- CONSL0009 [v1.0.0] - Pagination 10 per page --}}

@extends('layouts.blog')

@section('title', 'Our Blog - Simple CMS')
@section('meta_description', 'Read the latest articles, tips and insights from Simple CMS.')

@section('content')
<section class="py-12 md:py-16">
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-10">Our Blog</h1>

        <div id="blog-list">
            @include('blog::partials.blog-list')
        </div>
    </div>
</section>
@endsection
```

### 7.2 Blog List Partial (htmx target)

**File:** `Modules/Blog/resources/views/partials/blog-list.blade.php`

```blade
{{-- NFRL00090 [v1.0.0] - Vertical card layout: image left (desktop), image top (mobile) --}}
{{-- NFRL00093 [v1.0.0] - Thumbnail image 800x600 --}}
{{-- NFRL00096 [v1.0.0] - Title bold 18px --}}
{{-- NFRL00099 [v1.0.0] - Description 16px --}}

<div class="space-y-8">
    @forelse($posts as $post)
    <a href="/{{ $post->slug }}"
       class="block bg-surface border border-border rounded-card shadow hover:shadow-lg transition overflow-hidden md:flex">
        <img src="{{ asset($post->image_path) }}"
             alt="{{ $post->title }}"
             class="w-full md:w-72 h-48 md:h-auto object-cover flex-shrink-0">
        <div class="p-6">
            @if($post->category_name)
            <span class="text-xs font-medium text-primary uppercase tracking-wide">{{ $post->category_name }}</span>
            @endif
            <h2 class="text-lg font-bold mb-2 mt-1">{{ $post->title }}</h2>
            <p class="text-text-secondary text-base mb-2">{{ $post->summary }}</p>
            <p class="text-xs text-text-secondary">{{ $post->effective_date }}</p>
        </div>
    </a>
    @empty
    <div class="text-center py-12">
        <p class="text-text-secondary text-lg">No blog posts available at this time.</p>
    </div>
    @endforelse
</div>

{{-- Pagination (CONSL0009) --}}
@if($posts->hasPages())
<nav class="mt-12 flex justify-center items-center space-x-2" aria-label="Blog pagination">
    {{-- Previous --}}
    @if($posts->onFirstPage())
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">&laquo; Previous</span>
    @else
        <a href="{{ $posts->previousPageUrl() }}"
           hx-get="{{ $posts->previousPageUrl() }}"
           hx-target="#blog-list"
           hx-swap="innerHTML"
           hx-push-url="true"
           class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">&laquo; Previous</a>
    @endif

    {{-- Page numbers --}}
    @foreach($posts->getUrlRange(1, $posts->lastPage()) as $page => $url)
        @if($page == $posts->currentPage())
            <span class="px-3 py-2 text-sm font-bold text-white bg-primary rounded-btn">{{ $page }}</span>
        @else
            <a href="{{ $url }}"
               hx-get="{{ $url }}"
               hx-target="#blog-list"
               hx-swap="innerHTML"
               hx-push-url="true"
               class="px-3 py-2 text-sm text-text-secondary hover:text-primary border border-border rounded-btn transition">{{ $page }}</a>
        @endif
    @endforeach

    {{-- Next --}}
    @if($posts->hasMorePages())
        <a href="{{ $posts->nextPageUrl() }}"
           hx-get="{{ $posts->nextPageUrl() }}"
           hx-target="#blog-list"
           hx-swap="innerHTML"
           hx-push-url="true"
           class="px-3 py-2 text-sm text-text-secondary hover:text-primary transition">Next &raquo;</a>
    @else
        <span class="px-3 py-2 text-sm text-text-secondary cursor-not-allowed">Next &raquo;</span>
    @endif
</nav>
@endif
```

### 7.3 Blog Detail Page

**File:** `Modules/Blog/resources/views/detail.blade.php`

```blade
{{-- USL000024 [v1.0.0] - Blog article detail page --}}
{{-- NFRL00102 [v1.0.0] - White bg, single column max-width 800px --}}

@extends('layouts.blog')

@section('title', $post->title . ' - Blog - Simple CMS')
@section('meta_description', Str::limit(strip_tags($post->content), 160))

@section('content')
{{-- Hero image with title overlay --}}
<section class="relative w-full">
    <img src="{{ asset($post->image_path) }}"
         alt="{{ $post->title }}"
         class="w-full h-[400px] md:h-[500px] object-cover">
    <div class="absolute inset-0 bg-black/50 flex flex-col items-center justify-center text-center px-4">
        <h1 class="text-white text-3xl md:text-5xl font-bold mb-4">{{ $post->title }}</h1>
        <p class="text-white/80 text-sm">
            Published on {{ $post->effective_date }}
            @if($post->category_name)
                &middot; {{ $post->category_name }}
            @endif
        </p>
    </div>
</section>

{{-- Article content (NFRL00102 - max-width 800px) --}}
<article class="bg-white py-12 md:py-16">
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8" style="max-width: 800px;">

        {{-- Back to blog --}}
        <a href="/blog"
           class="inline-flex items-center text-primary hover:text-primary-dark text-sm font-medium mb-8 transition">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
            </svg>
            Back to Blog
        </a>

        {{-- Article body (HTML content from DB) --}}
        <div class="prose prose-lg max-w-none text-text-primary">
            {!! $post->content !!}
        </div>

        {{-- Back to blog (bottom) --}}
        <div class="mt-10 border-t border-border pt-8">
            <a href="/blog"
               class="inline-flex items-center text-primary hover:text-primary-dark text-sm font-medium transition">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Blog
            </a>
        </div>
    </div>
</article>
@endsection
```

---

## 8. Routes

### 8.1 Module Routes

**File:** `Modules/Blog/routes/web.php`

```php
<?php

use Illuminate\Support\Facades\Route;
use Modules\Blog\Http\Controllers\BlogController;

// Blog directory page
Route::get('/blog', [BlogController::class, 'directory'])
    ->name('blog.directory');

// Blog detail page with SEO-friendly slug URL
// IMPORTANT: This route MUST be registered last to avoid catching other routes.
// The regex constraint ensures only valid slug patterns are matched.
Route::get('/{slug}', [BlogController::class, 'detail'])
    ->where('slug', '[a-z0-9]+(?:-[a-z0-9]+)*')
    ->name('blog.detail');
```

**Route priority note:** The `/{slug}` route uses a regex constraint `[a-z0-9]+(?:-[a-z0-9]+)*` to match only lowercase alphanumeric slugs with hyphens. This prevents it from catching routes like `/blog`, `/contact`, etc. The Blog module should have a lower priority in `module.json` to ensure its wildcard route is registered after all other module routes.

---

## 9. Module Registration

### 9.1 module.json

**File:** `Modules/Blog/module.json`

```json
{
    "name": "Blog",
    "alias": "blog",
    "description": "Blog directory and detail pages for the landing page",
    "keywords": ["blog", "articles", "posts", "landing"],
    "priority": 99,
    "providers": [
        "Modules\\Blog\\Providers\\BlogServiceProvider"
    ],
    "files": []
}
```

**Note:** The `priority` is set to 99 (loaded last) to ensure the `/{slug}` wildcard route does not intercept routes from other modules.

---

## 10. SEO Considerations

- Blog detail pages use SEO-friendly slug URLs: `/{slug}` (e.g., `/getting-started-with-simple-cms`).
- The `<title>` tag includes the blog post title: `{title} - Blog - Simple CMS`.
- The `<meta name="description">` tag is auto-generated from the first 160 characters of the blog content.
- Slugs are unique and managed by the Admin Portal. The Landing Page looks up posts by slug.
- If a slug is not found or belongs to a non-ACTIVE post, a 404 error is returned.
