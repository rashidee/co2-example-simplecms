package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;

/**
 * View model for the hero section list page.
 *
 * Traces: USA000033
 */
public record HeroSectionListView(
    Page<HeroSectionDTO> heroSections,
    HeroSectionStatus filterStatus,
    String filterEffectiveDate,
    String filterExpirationDate,
    HeroSectionStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static HeroSectionListView of(Page<HeroSectionDTO> heroSections,
                                          HeroSectionStatus filterStatus,
                                          String filterEffectiveDate,
                                          String filterExpirationDate) {
        return new HeroSectionListView(heroSections, filterStatus,
            filterEffectiveDate, filterExpirationDate,
            HeroSectionStatus.values(), null, false);
    }
}
