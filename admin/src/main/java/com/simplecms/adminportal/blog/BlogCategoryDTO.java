package com.simplecms.adminportal.blog;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000105
 */
public record BlogCategoryDTO(
    UUID id,
    String name,
    String description,
    Instant createdAt
) {}
