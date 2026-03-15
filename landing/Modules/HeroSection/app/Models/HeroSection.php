<?php

namespace Modules\HeroSection\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class HeroSection extends Model
{
    protected $table = 'hrs_hero_section';

    protected $keyType = 'string';
    public $incrementing = false;
    public $timestamps = false;

    protected $casts = [
        'id' => 'string',
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('status', 'ACTIVE')
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>', now());
    }
}
