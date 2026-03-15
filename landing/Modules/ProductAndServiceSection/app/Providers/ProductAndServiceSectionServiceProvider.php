<?php

namespace Modules\ProductAndServiceSection\Providers;

use Illuminate\Support\ServiceProvider;

class ProductAndServiceSectionServiceProvider extends ServiceProvider
{
    protected string $name = 'ProductAndServiceSection';
    protected string $nameLower = 'productandservicesection';

    public function boot(): void
    {
        $this->loadViewsFrom(module_path($this->name, 'resources/views'), $this->nameLower);
    }

    public function register(): void {}
}
