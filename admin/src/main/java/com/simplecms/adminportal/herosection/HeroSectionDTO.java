package com.simplecms.adminportal.herosection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object for hero section content.
 * v1.0.4: Added hasImageData flag; images now served via BLOB endpoint.
 *
 * Traces: USA000030
 */
public record HeroSectionDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    boolean hasImageData,
    String headline,
    String subheadline,
    String ctaUrl,
    String ctaText,
    LocalDateTime effectiveDate,
    LocalDateTime expirationDate,
    HeroSectionStatus status,
    Instant createdAt
) {
    /**
     * Returns the URL for the thumbnail image.
     * v1.0.4: Prefers BLOB endpoint if image data exists.
     */
    public String thumbnailUrl() {
        if (hasImageData) {
            return "/hero-section/" + id + "/thumbnail";
        }
        return thumbnailPath;
    }

    /**
     * Returns the URL for the full image.
     */
    public String imageUrl() {
        if (hasImageData) {
            return "/hero-section/" + id + "/image";
        }
        return imagePath;
    }
}
