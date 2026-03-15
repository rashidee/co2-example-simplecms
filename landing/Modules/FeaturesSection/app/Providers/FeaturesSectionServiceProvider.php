<?php

namespace Modules\FeaturesSection\Providers;

use Illuminate\Support\ServiceProvider;

class FeaturesSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'FeaturesSection';
    protected string $nameLower = 'featuressection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
    }

    public function register(): void {}
}
