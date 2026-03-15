package com.simplecms.adminportal.herosection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Public API for the Hero Section module.
 *
 * Traces: USA000030, USA000033, NFRA00018-036, CONSA0012
 */
public interface HeroSectionService {

    /**
     * List hero sections with optional filters, ordered by effective date desc.
     *
     * Traces: USA000033, NFRA00033
     *
     * @param status        optional status filter
     * @param effectiveDate optional effective date filter
     * @param expirationDate optional expiration date filter
     * @param pageable      pagination parameters
     * @return page of hero section DTOs
     */
    Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDateTime effectiveDate,
                              LocalDateTime expirationDate, Pageable pageable);

    /**
     * Get a hero section by ID.
     *
     * @param id the hero section ID
     * @return the hero section DTO
     */
    HeroSectionDTO getById(UUID id);

    /**
     * Create a new hero section with image upload.
     * Validates image dimensions (1600x500) and generates thumbnail (400x125).
     *
     * Traces: USA000030, NFRA00021, NFRA00036
     *
     * @param headline       headline text (max 100)
     * @param subheadline    subheadline text (max 200)
     * @param ctaUrl         CTA button URL
     * @param ctaText        CTA button text (max 50)
     * @param effectiveDate  effective date
     * @param expirationDate expiration date (nullable)
     * @param status         content status
     * @param image          uploaded image file
     * @return created hero section DTO
     */
    HeroSectionDTO create(String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                          HeroSectionStatus status, MultipartFile image);

    /**
     * Update an existing hero section. Image is optional on update.
     *
     * Traces: USA000030
     *
     * @param id             the hero section ID
     * @param headline       headline text
     * @param subheadline    subheadline text
     * @param ctaUrl         CTA button URL
     * @param ctaText        CTA button text
     * @param effectiveDate  effective date
     * @param expirationDate expiration date (nullable)
     * @param status         content status
     * @param image          optional new image file
     * @return updated hero section DTO
     */
    HeroSectionDTO update(UUID id, String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                          HeroSectionStatus status, MultipartFile image);
}
