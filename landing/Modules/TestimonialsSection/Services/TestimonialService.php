<?php

namespace Modules\TestimonialsSection\Services;

use Illuminate\Support\Collection;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TestimonialsSection\DTOs\TestimonialData;
use Modules\TestimonialsSection\Models\Testimonial;

class TestimonialService implements TestimonialServiceInterface
{
    public function getAllVisible(): Collection
    {
        // v1.0.4: No status filter — status column removed
        return Testimonial::orderBy('display_order')
            ->get()
            ->map(fn (Testimonial $t) => new TestimonialData(
                id: $t->id,
                customer_name: $t->customer_name,
                customer_review: $t->customer_review,
                customer_rating: $t->customer_rating,
                display_order: $t->display_order,
            ));
    }
}
