<?php

namespace Modules\ProductAndServiceSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class ProductService extends Model
{
    use HasUuids;

    protected $table = 'pas_product_service';

    protected $fillable = [
        'image_path', 'image_data', 'thumbnail_path', 'thumbnail_data',
        'title', 'description', 'cta_url', 'cta_text', 'display_order',
    ];

    protected $casts = [
        'display_order' => 'integer',
    ];
}
