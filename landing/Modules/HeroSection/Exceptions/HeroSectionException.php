<?php

namespace Modules\HeroSection\Exceptions;

use App\Exceptions\WebApplicationException;

class HeroSectionException extends WebApplicationException
{
    public static function noActiveSlides(): self
    {
        return new self('No active hero slides available', 404);
    }
}
