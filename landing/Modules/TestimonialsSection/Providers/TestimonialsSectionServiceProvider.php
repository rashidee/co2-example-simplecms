<?php

namespace Modules\TestimonialsSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TestimonialsSection\Services\TestimonialService;

class TestimonialsSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(TestimonialServiceInterface::class, TestimonialService::class);
    }

    public function boot(): void {}
}
