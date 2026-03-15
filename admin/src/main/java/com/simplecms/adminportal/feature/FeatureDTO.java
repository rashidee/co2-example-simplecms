package com.simplecms.adminportal.feature;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000048
 */
public record FeatureDTO(
    UUID id,
    String icon,
    String title,
    String description,
    int displayOrder,
    FeatureStatus status,
    Instant createdAt
) {}
