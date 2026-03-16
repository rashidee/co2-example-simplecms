package com.simplecms.adminportal.testimonial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Testimonials Section module.
 *
 * Traces: USA000060-069, NFRA00069-078, CONSA0021
 */
public interface TestimonialService {

    /**
     * List testimonials ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000069, NFRA00078
     */
    Page<TestimonialDTO> list(Pageable pageable);

    /**
     * Get a testimonial by ID.
     */
    TestimonialDTO getById(UUID id);

    /**
     * Create a new testimonial.
     *
     * Traces: USA000060, USA000063
     *
     * @param customerName   customer name (max 100)
     * @param customerReview customer review (max 1000)
     * @param customerRating rating 1-5
     * @param displayOrder   display order integer
     * @return created testimonial DTO
     */
    TestimonialDTO create(String customerName, String customerReview,
                          int customerRating, int displayOrder);

    /**
     * Update an existing testimonial.
     *
     * Traces: USA000060, USA000063
     */
    TestimonialDTO update(UUID id, String customerName, String customerReview,
                          int customerRating, int displayOrder);

    /**
     * Delete a testimonial by ID.
     *
     * Traces: USA000066
     */
    void delete(UUID id);
}
