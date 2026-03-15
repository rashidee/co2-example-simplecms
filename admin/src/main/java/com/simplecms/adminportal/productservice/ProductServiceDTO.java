package com.simplecms.adminportal.productservice;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for product and service content.
 *
 * Traces: USA000036
 */
public record ProductServiceDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    String title,
    String description,
    String ctaUrl,
    String ctaText,
    int displayOrder,
    ProductServiceStatus status,
    Instant createdAt
) {}
