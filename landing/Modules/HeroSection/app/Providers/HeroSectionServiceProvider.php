<?php

namespace Modules\HeroSection\Providers;

use Illuminate\Support\ServiceProvider;

class HeroSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'HeroSection';
    protected string $nameLower = 'herosection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
    }

    public function register(): void
    {
        //
    }
}
