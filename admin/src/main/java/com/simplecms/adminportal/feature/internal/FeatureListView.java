package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import org.springframework.data.domain.Page;

public record FeatureListView(
    Page<FeatureDTO> features,
    String successMessage,
    boolean hasSuccess
) {
    public static FeatureListView of(Page<FeatureDTO> features) {
        return new FeatureListView(features, null, false);
    }
}
