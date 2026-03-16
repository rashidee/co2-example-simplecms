<?php

namespace Modules\ProductAndServiceSection\Providers;

use Illuminate\Support\ServiceProvider;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\ProductAndServiceSection\Services\ProductServiceService;

class ProductAndServiceSectionServiceProvider extends ServiceProvider
{
    public function register(): void
    {
        $this->app->bind(ProductServiceServiceInterface::class, ProductServiceService::class);
    }

    public function boot(): void {}
}
