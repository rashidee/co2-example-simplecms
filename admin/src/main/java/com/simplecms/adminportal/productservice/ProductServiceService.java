package com.simplecms.adminportal.productservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Public API for the Product and Service Section module.
 *
 * Traces: USA000036-045, NFRA00039-057, CONSA0015
 */
public interface ProductServiceService {

    /**
     * List product/service items with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000045, NFRA00054
     *
     * @param status   optional status filter
     * @param pageable pagination parameters
     * @return page of product/service DTOs
     */
    Page<ProductServiceDTO> list(ProductServiceStatus status, Pageable pageable);

    /**
     * Get a product/service item by ID.
     *
     * @param id the item ID
     * @return the product/service DTO
     */
    ProductServiceDTO getById(UUID id);

    /**
     * Create a new product/service item with image upload.
     * Validates image dimensions (400x400) and generates thumbnail (200x200).
     *
     * Traces: USA000036, USA000039, NFRA00039, NFRA00057
     *
     * @param title        title text (max 100)
     * @param description  description text (max 500)
     * @param ctaUrl       optional CTA URL
     * @param ctaText      optional CTA text (max 50)
     * @param displayOrder display order integer
     * @param status       content status
     * @param image        uploaded image file
     * @return created product/service DTO
     */
    ProductServiceDTO create(String title, String description, String ctaUrl,
                             String ctaText, int displayOrder,
                             ProductServiceStatus status, MultipartFile image);

    /**
     * Update an existing product/service item. Image is optional on update.
     *
     * Traces: USA000036, USA000039
     *
     * @param id           the item ID
     * @param title        title text
     * @param description  description text
     * @param ctaUrl       optional CTA URL
     * @param ctaText      optional CTA text
     * @param displayOrder display order integer
     * @param status       content status
     * @param image        optional new image file
     * @return updated product/service DTO
     */
    ProductServiceDTO update(UUID id, String title, String description, String ctaUrl,
                             String ctaText, int displayOrder,
                             ProductServiceStatus status, MultipartFile image);

    /**
     * Delete a product/service item by ID.
     *
     * Traces: USA000042
     *
     * @param id the item ID
     */
    void delete(UUID id);
}
