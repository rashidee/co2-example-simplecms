<?php

namespace Modules\TestimonialsSection\Models;

use Illuminate\Database\Eloquent\Concerns\HasUuids;
use Illuminate\Database\Eloquent\Model;

class Testimonial extends Model
{
    use HasUuids;

    protected $table = 'tst_testimonial';

    protected $fillable = [
        'customer_name', 'customer_review', 'customer_rating', 'display_order',
    ];

    protected $casts = [
        'customer_rating' => 'integer',
        'display_order' => 'integer',
    ];
}
