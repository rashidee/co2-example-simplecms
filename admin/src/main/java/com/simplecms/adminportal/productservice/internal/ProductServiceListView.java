package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;

/**
 * View model for the product and service list page.
 *
 * Traces: USA000045
 */
public record ProductServiceListView(
    Page<ProductServiceDTO> items,
    ProductServiceStatus filterStatus,
    ProductServiceStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static ProductServiceListView of(Page<ProductServiceDTO> items, ProductServiceStatus filterStatus) {
        return new ProductServiceListView(items, filterStatus, ProductServiceStatus.values(), null, false);
    }
}
