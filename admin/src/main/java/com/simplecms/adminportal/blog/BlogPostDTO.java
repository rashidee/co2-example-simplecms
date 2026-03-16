package com.simplecms.adminportal.blog;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object for blog post content.
 * v1.0.4: Added hasImageData flag; images now served via BLOB endpoint.
 *
 * Traces: USA000096
 */
public record BlogPostDTO(
    UUID id,
    UUID categoryId,
    String categoryName,
    UUID authorId,
    String authorName,
    String title,
    String slug,
    String summary,
    String content,
    String imagePath,
    String thumbnailPath,
    boolean hasImageData,
    LocalDateTime effectiveDate,
    LocalDateTime expirationDate,
    BlogPostStatus status,
    Instant createdAt
) {
    /**
     * Returns the URL for the thumbnail image.
     * v1.0.4: Prefers BLOB endpoint if image data exists.
     */
    public String thumbnailUrl() {
        if (hasImageData) {
            return "/blog/" + id + "/thumbnail";
        }
        return thumbnailPath;
    }

    /**
     * Returns the URL for the full image.
     * v1.0.4: Prefers BLOB endpoint if image data exists.
     */
    public String imageUrl() {
        if (hasImageData) {
            return "/blog/" + id + "/image";
        }
        return imagePath;
    }
}
