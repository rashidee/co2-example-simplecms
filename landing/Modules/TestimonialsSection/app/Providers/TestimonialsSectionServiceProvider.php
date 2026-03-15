<?php

namespace Modules\TestimonialsSection\Providers;

use Illuminate\Support\ServiceProvider;

class TestimonialsSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'TestimonialsSection';
    protected string $nameLower = 'testimonialssection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
    }

    public function register(): void {}
}
