package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import com.simplecms.adminportal.feature.FeatureStatus;

public record FeatureFormView(
    FeatureDTO feature,
    boolean isEdit,
    FeatureStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static FeatureFormView forCreate() {
        return new FeatureFormView(null, false, FeatureStatus.values(), null, false);
    }

    public static FeatureFormView forEdit(FeatureDTO feature) {
        return new FeatureFormView(feature, true, FeatureStatus.values(), null, false);
    }

    public static FeatureFormView withError(FeatureDTO feature, boolean isEdit, String message) {
        return new FeatureFormView(feature, isEdit, FeatureStatus.values(), message, true);
    }
}
