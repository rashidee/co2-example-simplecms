<?php

namespace App\Http\Controllers;

use Modules\HeroSection\Services\HeroSectionService;
use Modules\ProductAndServiceSection\Services\ProductServiceService;
use Modules\FeaturesSection\Services\FeatureService;
use Modules\TestimonialsSection\Services\TestimonialService;
use Modules\TeamSection\Services\TeamMemberService;
use Modules\ContactSection\Services\ContactInfoService;

class HomeController extends Controller
{
    public function __construct(
        private readonly HeroSectionService $heroSectionService,
        private readonly ProductServiceService $productServiceService,
        private readonly FeatureService $featureService,
        private readonly TestimonialService $testimonialService,
        private readonly TeamMemberService $teamMemberService,
        private readonly ContactInfoService $contactInfoService,
    ) {}

    public function index()
    {
        return view('pages.home', [
            'heroes' => $this->heroSectionService->getActiveHeroes(),
            'products' => $this->productServiceService->getActiveProducts(),
            'features' => $this->featureService->getActiveFeatures(),
            'testimonials' => $this->testimonialService->getActiveTestimonials(),
            'teamMembers' => $this->teamMemberService->getActiveMembers(),
            'contactInfo' => $this->contactInfoService->getContactInfo(),
        ]);
    }
}
