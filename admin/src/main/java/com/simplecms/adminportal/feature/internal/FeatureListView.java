package com.simplecms.adminportal.feature.internal;

import com.simplecms.adminportal.feature.FeatureDTO;
import com.simplecms.adminportal.feature.FeatureStatus;
import org.springframework.data.domain.Page;

public record FeatureListView(
    Page<FeatureDTO> features,
    FeatureStatus filterStatus,
    FeatureStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static FeatureListView of(Page<FeatureDTO> features, FeatureStatus filterStatus) {
        return new FeatureListView(features, filterStatus, FeatureStatus.values(), null, false);
    }
}
