package com.simplecms.adminportal.herosection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object for hero section content.
 *
 * Traces: USA000030
 */
public record HeroSectionDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    String headline,
    String subheadline,
    String ctaUrl,
    String ctaText,
    LocalDateTime effectiveDate,
    LocalDateTime expirationDate,
    HeroSectionStatus status,
    Instant createdAt
) {}
