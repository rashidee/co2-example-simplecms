<?php

namespace Modules\TestimonialsSection\Services;

use Illuminate\Support\Collection;
use Modules\TestimonialsSection\Models\Testimonial;

class TestimonialService
{
    public function getActiveTestimonials(): Collection
    {
        return Testimonial::query()
            ->active()
            ->orderBy('display_order')
            ->get();
    }
}
