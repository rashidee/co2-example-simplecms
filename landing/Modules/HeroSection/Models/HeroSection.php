<?php

namespace Modules\HeroSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class HeroSection extends Model
{
    use HasUuids;

    protected $table = 'hrs_hero_section';

    public $timestamps = true;

    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    protected $fillable = [
        'image_path',
        'image_data',
        'thumbnail_path',
        'thumbnail_data',
        'headline',
        'subheadline',
        'cta_url',
        'cta_text',
        'effective_date',
        'expiration_date',
        'status',
    ];

    protected $casts = [
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>=', now());
    }
}
