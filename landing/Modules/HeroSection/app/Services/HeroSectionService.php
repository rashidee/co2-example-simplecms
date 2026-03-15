<?php

namespace Modules\HeroSection\Services;

use Illuminate\Support\Collection;
use Modules\HeroSection\Models\HeroSection;

class HeroSectionService
{
    public function getActiveHeroes(): Collection
    {
        return HeroSection::query()
            ->active()
            ->orderBy('effective_date', 'desc')
            ->get();
    }
}
