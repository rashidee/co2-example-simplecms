package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceStatus;

/**
 * View model for product/service create/edit forms.
 *
 * Traces: USA000036
 */
public record ProductServiceFormView(
    ProductServiceDTO item,
    boolean isEdit,
    ProductServiceStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static ProductServiceFormView forCreate() {
        return new ProductServiceFormView(null, false, ProductServiceStatus.values(), null, false);
    }

    public static ProductServiceFormView forEdit(ProductServiceDTO item) {
        return new ProductServiceFormView(item, true, ProductServiceStatus.values(), null, false);
    }

    public static ProductServiceFormView withError(ProductServiceDTO item, boolean isEdit, String message) {
        return new ProductServiceFormView(item, isEdit, ProductServiceStatus.values(), message, true);
    }
}
