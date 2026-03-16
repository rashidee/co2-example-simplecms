# Blog — Module Specification

> Back to [SPECIFICATION.md](../SPECIFICATION.md)

## 1. Traceability

### User Stories
| ID | Description | Version |
|----|-------------|---------|
| USL000021 | As Public I want to be able to see a list of blog articles, so that I can stay updated with the latest news and insights about the company and its products and services. | v1.0.0 |
| USL000024 | As Public I want to be able to view the blog article detail page, so that I can read the full content of the blog article and understand the insights and information shared by the company. | v1.0.0 |

### Non-Functional Requirements
| ID | Description | Version |
|----|-------------|---------|
| NFRL00087 | Section title: "Our Blog" | v1.0.0 |
| NFRL00090 | The blog list will be a vertical card layout with the blog thumbnail image on the left and the title and short description on the right on desktop, and a vertical card layout with the thumbnail image on top and the title and short description below on mobile devices. | v1.0.0 |
| NFRL00093 | The image size will be medium image with a resolution of at least 800x600 pixels. | v1.0.0 |
| NFRL00096 | The title will be displayed in a bold font with a size of at least 18px. | v1.0.0 |
| NFRL00099 | The short description will be displayed in a regular font with a size of at least 16px. | v1.0.0 |
| NFRL00102 | The blog article detail page will have a white background, and the content will be displayed in a single column layout with a maximum width of 800px. | v1.0.0 |

### Constraints
| ID | Description | Version |
|----|-------------|---------|
| CONSL0009 | The list will be paginated with 10 articles per page, and there will be a pagination navigation at the bottom of the blog directory page to allow users to navigate through the pages of blog articles. | v1.0.0 |

### Removed / Replaced
_None._

### Data Sources
| Artifact | Reference |
|----------|-----------|
| Tables | `blg_blog_post`, `blg_blog_category` |
| Mockup Screens | `mockup/pages/blog.html`, `mockup/pages/blog_detail.html` |
| Admin Model | `admin/context/model/blog/model.md` |

### SEO-Friendly URLs

Per PRD.md Coding Standard: Blog article URLs use the format `/{blog-slug}` where `blog-slug` is the auto-generated slug from the blog post title (lowercase, hyphen-separated, unique).

- Blog directory: `/blog`
- Blog detail: `/blog/{slug}` (e.g., `/blog/getting-started-with-simple-cms`)

## 2. Service Contract

```php
namespace Modules\Blog\Contracts;

use Illuminate\Pagination\LengthAwarePaginator;
use Modules\Blog\DTOs\BlogPostData;

interface BlogServiceInterface
{
    /**
     * Get paginated list of active blog posts.
     * Active means: status=ACTIVE, effective_date <= now, expiration_date >= now.
     * Ordered by effective_date descending.
     * Paginated at 10 per page (CONSL0009).
     */
    public function listActivePosts(int $page = 1, int $perPage = 10): LengthAwarePaginator;

    /**
     * Get a single blog post by its SEO-friendly slug.
     * Only returns ACTIVE posts within their date range.
     */
    public function getBySlug(string $slug): BlogPostData;
}
```

## 3. DTOs

### BlogPostData

```php
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
```

### BlogCategoryData

```php
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
```

## 4. Exception

```php
namespace Modules\Blog\Exceptions;

use App\Exceptions\WebApplicationException;

class BlogException extends WebApplicationException
{
    public static function postNotFound(string $slug): self
    {
        return new self("Blog post not found: {$slug}", 404, 'The blog post you are looking for does not exist.');
    }
}
```

## 5. Eloquent Models

### BlogPost

```php
namespace Modules\Blog\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class BlogPost extends Model
{
    use HasUuids;

    protected $table = 'blg_blog_post';

    protected $fillable = [
        'category_id', 'author_id', 'title', 'slug', 'summary', 'content',
        'image_path', 'image_data', 'thumbnail_path', 'thumbnail_data',
        'effective_date', 'expiration_date', 'status',
    ];

    protected $casts = [
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    /**
     * Scope: active posts — status=ACTIVE AND within date range.
     */
    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('status', 'ACTIVE')
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>=', now());
    }

    public function category(): BelongsTo
    {
        return $this->belongsTo(BlogCategory::class, 'category_id');
    }
}
```

### BlogCategory

```php
namespace Modules\Blog\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class BlogCategory extends Model
{
    use HasUuids;

    protected $table = 'blg_blog_category';

    protected $fillable = ['name', 'description'];
}
```

## 6. Migration

**Not applicable.** Admin Portal owns the schema.

## 7. Service Implementation

```php
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
        return BlogPost::active()
            ->with('category')
            ->orderByDesc('effective_date')
            ->paginate($perPage, ['*'], 'page', $page);
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
```

## 8. Page Controller

```php
namespace Modules\Blog\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\View\View;
use Modules\Blog\Contracts\BlogServiceInterface;
use Modules\Blog\ViewModels\BlogListViewModel;
use Modules\Blog\ViewModels\BlogDetailViewModel;

class BlogPageController extends Controller
{
    public function __construct(
        private readonly BlogServiceInterface $blogService,
    ) {}

    /**
     * Blog directory — paginated list (USL000021, CONSL0009).
     */
    public function index(Request $request): View
    {
        $posts = $this->blogService->listActivePosts(
            page: $request->integer('page', 1),
            perPage: 10,
        );

        return (new BlogListViewModel(posts: $posts))
            ->view('blog::pages.index');
    }

    /**
     * Blog detail — single post by slug (USL000024).
     * SEO-friendly URL: /blog/{slug}
     */
    public function show(string $slug): View
    {
        $post = $this->blogService->getBySlug($slug);

        return (new BlogDetailViewModel(post: $post))
            ->view('blog::pages.show');
    }
}
```

## 9. View Models

### BlogListViewModel

```php
namespace Modules\Blog\ViewModels;

use Illuminate\Pagination\LengthAwarePaginator;
use Spatie\ViewModels\ViewModel;

class BlogListViewModel extends ViewModel
{
    public function __construct(
        public readonly LengthAwarePaginator $posts,
    ) {}
}
```

### BlogDetailViewModel

```php
namespace Modules\Blog\ViewModels;

use Modules\Blog\DTOs\BlogPostData;
use Spatie\ViewModels\ViewModel;

class BlogDetailViewModel extends ViewModel
{
    public function __construct(
        public readonly BlogPostData $post,
    ) {}
}
```

## 10. Blade Templates

### pages/index.blade.php (Blog Directory)

```blade
{{-- Blog Directory — USL000021, NFRL00087-NFRL00099, CONSL0009 --}}
<x-landing-layout title="Our Blog - Simple CMS">

<section class="py-12 md:py-16">
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-3xl md:text-4xl font-bold mb-10">Our Blog</h1>

        <div class="space-y-8">
            @foreach($posts as $post)
            {{-- NFRL00090: horizontal card on desktop, vertical on mobile --}}
            <a href="{{ route('blog.show', $post->slug) }}"
               class="block bg-surface border border-border rounded-card shadow hover:shadow-lg transition overflow-hidden md:flex">
                @if($post->thumbnail_url ?? $post->image_url)
                    <img src="{{ $post->thumbnail_url ?? $post->image_url }}"
                         alt="{{ $post->title }}"
                         class="w-full md:w-72 h-48 md:h-auto object-cover flex-shrink-0">
                @endif
                <div class="p-6">
                    {{-- NFRL00096: bold 18px title --}}
                    <h2 class="text-lg font-bold mb-2">{{ $post->title }}</h2>
                    {{-- NFRL00099: 16px description --}}
                    <p class="text-text-secondary text-base">{{ $post->summary }}</p>
                </div>
            </a>
            @endforeach
        </div>

        {{-- Pagination (CONSL0009 — 10 per page) --}}
        <x-pagination :paginator="$posts" />
    </div>
</section>

</x-landing-layout>
```

### pages/show.blade.php (Blog Detail)

```blade
{{-- Blog Detail — USL000024, NFRL00102 --}}
<x-landing-layout title="{{ $post->title }} - Blog - Simple CMS">

{{-- Hero image with title overlay --}}
<section class="relative w-full">
    @if($post->image_url)
        <img src="{{ $post->image_url }}" alt="{{ $post->title }}"
             class="w-full h-[400px] md:h-[500px] object-cover">
    @endif
    <div class="absolute inset-0 bg-black/50 flex flex-col items-center justify-center text-center px-4">
        <h1 class="text-white text-3xl md:text-5xl font-bold mb-4">{{ $post->title }}</h1>
        <p class="text-white/80 text-sm">Published on {{ \Carbon\Carbon::parse($post->effective_date)->format('F j, Y') }}</p>
    </div>
</section>

{{-- Article content (NFRL00102 — single column, max-w 800px / max-w-3xl) --}}
<article class="bg-white py-12 md:py-16">
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">

        {{-- Back to blog --}}
        <a href="{{ route('blog.index') }}"
           class="inline-flex items-center text-primary hover:text-primary-dark text-sm font-medium mb-8 transition">
            <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
            </svg>
            Back to Blog
        </a>

        {{-- Article body (rich HTML content, sanitized by Admin Portal) --}}
        <div class="prose prose-lg max-w-none text-text-primary">
            {!! $post->content !!}
        </div>

        {{-- Category tag --}}
        @if($post->category_name)
        <div class="border-t border-border mt-10 pt-8">
            <p class="text-sm text-text-secondary">
                Category: <span class="text-primary">{{ $post->category_name }}</span>
            </p>
        </div>
        @endif

        {{-- Back to blog (bottom) --}}
        <div class="mt-10">
            <a href="{{ route('blog.index') }}"
               class="inline-flex items-center text-primary hover:text-primary-dark text-sm font-medium transition">
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                </svg>
                Back to Blog
            </a>
        </div>
    </div>
</article>

</x-landing-layout>
```

## 11. Module Routes

```php
// Modules/Blog/routes/web.php
use Illuminate\Support\Facades\Route;
use Modules\Blog\Http\Controllers\BlogPageController;

Route::get('/blog', [BlogPageController::class, 'index'])->name('blog.index');
Route::get('/blog/{slug}', [BlogPageController::class, 'show'])->name('blog.show')
    ->where('slug', '[a-z0-9\-]+');
```

## 12. Module Service Provider

```php
namespace Modules\Blog\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\Blog\Contracts\BlogServiceInterface;
use Modules\Blog\Services\BlogService;

class BlogServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(BlogServiceInterface::class, BlogService::class);
    }

    public function boot(): void
    {
        $this->loadViewsFrom(module_path('Blog', 'resources/views'), 'blog');
        $this->loadRoutesFrom(module_path('Blog', 'routes/web.php'));
    }
}
```
