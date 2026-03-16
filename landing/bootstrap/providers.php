<?php

use App\Providers\AppServiceProvider;

return [
    AppServiceProvider::class,
    Modules\HeroSection\Providers\HeroSectionServiceProvider::class,
    Modules\ProductAndServiceSection\Providers\ProductAndServiceSectionServiceProvider::class,
    Modules\FeaturesSection\Providers\FeaturesSectionServiceProvider::class,
    Modules\TestimonialsSection\Providers\TestimonialsSectionServiceProvider::class,
    Modules\TeamSection\Providers\TeamSectionServiceProvider::class,
    Modules\ContactSection\Providers\ContactSectionServiceProvider::class,
    Modules\Blog\Providers\BlogServiceProvider::class,
];
