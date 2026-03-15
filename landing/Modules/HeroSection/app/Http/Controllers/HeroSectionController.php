<?php

namespace Modules\HeroSection\Http\Controllers;

use App\Http\Controllers\Controller;
use Modules\HeroSection\Services\HeroSectionService;

class HeroSectionController extends Controller
{
    public function __construct(
        private readonly HeroSectionService $heroSectionService,
    ) {}

    public function getActiveHeroes()
    {
        return $this->heroSectionService->getActiveHeroes();
    }
}
