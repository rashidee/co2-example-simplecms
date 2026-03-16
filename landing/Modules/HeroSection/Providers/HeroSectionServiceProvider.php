<?php

namespace Modules\HeroSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\HeroSection\Services\HeroSectionService;

class HeroSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(HeroSectionServiceInterface::class, HeroSectionService::class);
    }

    public function boot(): void
    {
        // No migrations — Admin Portal owns the schema
        // No views — rendered as section in home page
        // No routes — data served via HomeController
    }
}
