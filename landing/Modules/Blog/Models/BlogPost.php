<?php

namespace Modules\Blog\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class BlogPost extends Model
{
    use HasUuids;

    protected $table = 'blg_blog_post';

    public $timestamps = true;

    const CREATED_AT = 'created_at';
    const UPDATED_AT = 'updated_at';

    protected $fillable = [
        'category_id',
        'author_id',
        'title',
        'slug',
        'summary',
        'content',
        'image_path',
        'thumbnail_path',
        'effective_date',
        'expiration_date',
        'status',
    ];

    protected $casts = [
        'effective_date' => 'datetime',
        'expiration_date' => 'datetime',
    ];

    public function category(): BelongsTo
    {
        return $this->belongsTo(BlogCategory::class, 'category_id');
    }

    public function scopeActive(Builder $query): Builder
    {
        return $query
            ->where('status', 'ACTIVE')
            ->where('effective_date', '<=', now())
            ->where('expiration_date', '>=', now());
    }
}
