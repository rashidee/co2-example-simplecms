<?php

namespace Modules\ProductAndServiceSection\Services;

use Illuminate\Support\Collection;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\ProductAndServiceSection\DTOs\ProductServiceData;
use Modules\ProductAndServiceSection\Models\ProductService;

class ProductServiceService implements ProductServiceServiceInterface
{
    public function getAllVisible(): Collection
    {
        $items = ProductService::orderBy('display_order')
            ->orderBy('created_at')
            ->get();

        return $items->map(fn (ProductService $item) => new ProductServiceData(
            id: $item->id,
            title: $item->title,
            description: $item->description,
            cta_url: $item->cta_url,
            cta_text: $item->cta_text,
            display_order: $item->display_order,
            image_url: $item->image_data
                ? route('image.show', ['table' => 'product-service', 'id' => $item->id, 'column' => 'image_data'])
                : null,
        ));
    }
}
