<?php

namespace Modules\HeroSection\Contracts;

use Illuminate\Support\Collection;

interface HeroSectionServiceInterface
{
    public function getActiveSlides(): Collection;
}
