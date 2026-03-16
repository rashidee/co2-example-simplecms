package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;

/**
 * View model for hero section create/edit forms.
 * v1.0.4: Removed statuses array — status is auto-computed from dates.
 *
 * Traces: USA000030
 */
public record HeroSectionFormView(
    HeroSectionDTO heroSection,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static HeroSectionFormView forCreate() {
        return new HeroSectionFormView(null, false, null, false);
    }

    public static HeroSectionFormView forEdit(HeroSectionDTO heroSection) {
        return new HeroSectionFormView(heroSection, true, null, false);
    }

    public static HeroSectionFormView withError(HeroSectionDTO heroSection, boolean isEdit, String message) {
        return new HeroSectionFormView(heroSection, isEdit, message, true);
    }
}
