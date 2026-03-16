<?php

namespace Modules\FeaturesSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\FeaturesSection\Contracts\FeatureServiceInterface;
use Modules\FeaturesSection\Services\FeatureService;

class FeaturesSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(FeatureServiceInterface::class, FeatureService::class);
    }

    public function boot(): void {}
}
