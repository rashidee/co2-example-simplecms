<?php

namespace App\Http\Controllers;

use Illuminate\View\View;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\ProductAndServiceSection\Contracts\ProductServiceServiceInterface;
use Modules\FeaturesSection\Contracts\FeatureServiceInterface;
use Modules\TestimonialsSection\Contracts\TestimonialServiceInterface;
use Modules\TeamSection\Contracts\TeamMemberServiceInterface;
use Modules\ContactSection\Contracts\ContactSectionServiceInterface;

class HomeController extends Controller
{
    public function __construct(
        private readonly HeroSectionServiceInterface $heroService,
        private readonly ProductServiceServiceInterface $productService,
        private readonly FeatureServiceInterface $featureService,
        private readonly TestimonialServiceInterface $testimonialService,
        private readonly TeamMemberServiceInterface $teamMemberService,
        private readonly ContactSectionServiceInterface $contactService,
    ) {}

    public function index(): View
    {
        return view('pages.home', [
            'heroSlides' => $this->heroService->getActiveSlides(),
            'products' => $this->productService->getAllVisible(),
            'features' => $this->featureService->getAllVisible(),
            'testimonials' => $this->testimonialService->getAllVisible(),
            'teamMembers' => $this->teamMemberService->getAllVisible(),
            'contactInfo' => $this->contactService->getContactInfo(),
        ]);
    }
}
