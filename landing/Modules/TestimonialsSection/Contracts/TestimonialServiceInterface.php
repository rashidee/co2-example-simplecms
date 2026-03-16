<?php

namespace Modules\TestimonialsSection\Contracts;

use Illuminate\Support\Collection;

interface TestimonialServiceInterface
{
    /**
     * Get all visible testimonials.
     * v1.0.4: Status column removed — all items are visible.
     * Ordered by display_order ascending.
     *
     * @return Collection<TestimonialData>
     */
    public function getAllVisible(): Collection;
}
