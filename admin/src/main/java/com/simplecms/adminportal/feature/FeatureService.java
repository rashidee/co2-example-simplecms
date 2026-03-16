package com.simplecms.adminportal.feature;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Features Section module.
 *
 * Traces: USA000048-057, NFRA00060-066, CONSA0018
 */
public interface FeatureService {

    /**
     * List features ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000057, NFRA00066
     */
    Page<FeatureDTO> list(Pageable pageable);

    /**
     * Get a feature by ID.
     */
    FeatureDTO getById(UUID id);

    /**
     * Create a new feature.
     *
     * Traces: USA000048, USA000051
     *
     * @param icon         FontAwesome CSS class
     * @param title        title text (max 100)
     * @param description  description text (max 500)
     * @param displayOrder display order integer
     * @return created feature DTO
     */
    FeatureDTO create(String icon, String title, String description,
                      int displayOrder);

    /**
     * Update an existing feature.
     *
     * Traces: USA000048, USA000051
     */
    FeatureDTO update(UUID id, String icon, String title, String description,
                      int displayOrder);

    /**
     * Delete a feature by ID.
     *
     * Traces: USA000054
     */
    void delete(UUID id);
}
