package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import org.springframework.data.domain.Page;

/**
 * View model for the product and service list page.
 *
 * Traces: USA000045
 */
public record ProductServiceListView(
    Page<ProductServiceDTO> items,
    String successMessage,
    boolean hasSuccess
) {
    public static ProductServiceListView of(Page<ProductServiceDTO> items) {
        return new ProductServiceListView(items, null, false);
    }
}
