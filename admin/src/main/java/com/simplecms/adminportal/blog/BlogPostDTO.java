package com.simplecms.adminportal.blog;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
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
    LocalDateTime effectiveDate,
    LocalDateTime expirationDate,
    BlogPostStatus status,
    Instant createdAt
) {}
