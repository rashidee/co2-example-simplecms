<?php

namespace Modules\TestimonialsSection\DTOs;

use Spatie\LaravelData\Data;

class TestimonialData extends Data
{
    public function __construct(
        public readonly string $id,
        public readonly string $customer_name,
        public readonly string $customer_review,
        public readonly int $customer_rating,
        public readonly int $display_order,
    ) {}
}
