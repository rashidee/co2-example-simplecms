<?php

namespace Modules\HeroSection\ViewModels;

use Illuminate\Support\Collection;

class HeroSectionViewModel
{
    public function __construct(
        public readonly Collection $slides,
    ) {}
}
