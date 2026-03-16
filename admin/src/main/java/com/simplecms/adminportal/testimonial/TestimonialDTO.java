package com.simplecms.adminportal.testimonial;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000060
 */
public record TestimonialDTO(
    UUID id,
    String customerName,
    String customerReview,
    int customerRating,
    int displayOrder,
    Instant createdAt
) {}
