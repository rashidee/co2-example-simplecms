<?php

namespace Modules\ProductAndServiceSection\Services;

use Illuminate\Support\Collection;
use Modules\ProductAndServiceSection\Models\ProductService;

class ProductServiceService
{
    public function getActiveProducts(): Collection
    {
        return ProductService::query()
            ->active()
            ->orderBy('display_order')
            ->get();
    }
}
