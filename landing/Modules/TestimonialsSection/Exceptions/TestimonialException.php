<?php

namespace Modules\TestimonialsSection\Exceptions;

use App\Exceptions\WebApplicationException;

class TestimonialException extends WebApplicationException
{
    public static function notFound(string $id): self
    {
        return new self("Testimonial not found: {$id}", 404);
    }
}
