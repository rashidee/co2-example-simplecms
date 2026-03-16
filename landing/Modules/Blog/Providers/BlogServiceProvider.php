<?php

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
        $this->loadViewsFrom(__DIR__ . '/../resources/views', 'blog');
        $this->loadRoutesFrom(__DIR__ . '/../routes/web.php');
    }
}
