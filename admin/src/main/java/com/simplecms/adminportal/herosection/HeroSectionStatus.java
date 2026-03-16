package com.simplecms.adminportal.herosection;

import java.time.LocalDateTime;

/**
 * Status values for hero section content.
 * v1.0.4: Removed READY status. Status is now auto-determined from dates.
 *
 * Traces: CONSA0012
 */
public enum HeroSectionStatus {
    DRAFT,
    ACTIVE,
    EXPIRED;

    /**
     * Compute status based on effective and expiration dates.
     * v1.0.4: USA000030 update — auto-determine status.
     */
    public static HeroSectionStatus computeFromDates(LocalDateTime effectiveDate, LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(effectiveDate)) {
            return DRAFT;
        }
        if (expirationDate != null && now.isAfter(expirationDate)) {
            return EXPIRED;
        }
        return ACTIVE;
    }
}
