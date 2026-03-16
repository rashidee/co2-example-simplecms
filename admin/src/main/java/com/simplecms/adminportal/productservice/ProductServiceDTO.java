package com.simplecms.adminportal.productservice;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for product and service content.
 * v1.0.4: Added hasImageData flag; images now served via BLOB endpoint.
 *
 * Traces: USA000036
 */
public record ProductServiceDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    boolean hasImageData,
    String title,
    String description,
    String ctaUrl,
    String ctaText,
    int displayOrder,
    Instant createdAt
) {
    /**
     * Returns the URL for the thumbnail image.
     * v1.0.4: Prefers BLOB endpoint if image data exists.
     */
    public String thumbnailUrl() {
        if (hasImageData) {
            return "/product-and-service/" + id + "/thumbnail";
        }
        return thumbnailPath;
    }

    /**
     * Returns the URL for the full image.
     */
    public String imageUrl() {
        if (hasImageData) {
            return "/product-and-service/" + id + "/image";
        }
        return imagePath;
    }
}
