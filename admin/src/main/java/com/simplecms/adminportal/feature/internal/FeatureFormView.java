package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;

public record FeatureFormView(
    FeatureDTO feature,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static FeatureFormView forCreate() {
        return new FeatureFormView(null, false, null, false);
    }

    public static FeatureFormView forEdit(FeatureDTO feature) {
        return new FeatureFormView(feature, true, null, false);
    }

    public static FeatureFormView withError(FeatureDTO feature, boolean isEdit, String message) {
        return new FeatureFormView(feature, isEdit, message, true);
    }
}
