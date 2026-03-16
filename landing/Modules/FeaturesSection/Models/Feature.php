<?php

namespace Modules\FeaturesSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class Feature extends Model
{
    use HasUuids;

    protected $table = 'fts_feature';

    protected $fillable = [
        'icon', 'title', 'description', 'display_order',
    ];

    protected $casts = [
        'display_order' => 'integer',
    ];
}
