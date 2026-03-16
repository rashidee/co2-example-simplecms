<?php

namespace Modules\HeroSection\Services;

use Illuminate\Support\Collection;
use Modules\HeroSection\Contracts\HeroSectionServiceInterface;
use Modules\HeroSection\DTOs\HeroSectionData;
use Modules\HeroSection\Models\HeroSection;

class HeroSectionService implements HeroSectionServiceInterface
{
    public function getActiveSlides(): Collection
    {
        $slides = HeroSection::active()
            ->orderBy('effective_date')
            ->get();

        return $slides->map(function (HeroSection $slide) {
            return new HeroSectionData(
                id: $slide->id,
                headline: $slide->headline,
                subheadline: $slide->subheadline,
                cta_url: $slide->cta_url,
                cta_text: $slide->cta_text,
                effective_date: $slide->effective_date->toDateString(),
                expiration_date: $slide->expiration_date->toDateString(),
                status: $slide->status,
                image_url: $slide->image_data
                    ? route('image.show', ['table' => 'hero-section', 'id' => $slide->id, 'column' => 'image_data'])
                    : null,
            );
        });
    }
}
