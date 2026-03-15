package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionStatus;

/**
 * View model for hero section create/edit forms.
 *
 * Traces: USA000030
 */
public record HeroSectionFormView(
    HeroSectionDTO heroSection,
    boolean isEdit,
    HeroSectionStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static HeroSectionFormView forCreate() {
        return new HeroSectionFormView(null, false, HeroSectionStatus.values(), null, false);
    }

    public static HeroSectionFormView forEdit(HeroSectionDTO heroSection) {
        return new HeroSectionFormView(heroSection, true, HeroSectionStatus.values(), null, false);
    }

    public static HeroSectionFormView withError(HeroSectionDTO heroSection, boolean isEdit, String message) {
        return new HeroSectionFormView(heroSection, isEdit, HeroSectionStatus.values(), message, true);
    }
}
