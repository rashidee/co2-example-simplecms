package com.simplecms.adminportal.herosection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Public API for the Hero Section module.
 * v1.0.4: Status is auto-computed from dates; images stored as BLOB.
 *
 * Traces: USA000030, USA000033, NFRA00018-036, CONSA0012
 */
public interface HeroSectionService {

    Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDateTime effectiveDate,
                              LocalDateTime expirationDate, Pageable pageable);

    HeroSectionDTO getById(UUID id);

    /**
     * v1.0.4: Status is auto-determined from dates. Image stored as BLOB.
     */
    HeroSectionDTO create(String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                          MultipartFile image);

    /**
     * v1.0.4: Status is auto-determined from dates. Image stored as BLOB.
     */
    HeroSectionDTO update(UUID id, String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                          MultipartFile image);

    /**
     * v1.0.4: Retrieve original image data as bytes.
     */
    byte[] getImageData(UUID id);

    /**
     * v1.0.4: Retrieve thumbnail image data as bytes.
     */
    byte[] getThumbnailData(UUID id);
}
