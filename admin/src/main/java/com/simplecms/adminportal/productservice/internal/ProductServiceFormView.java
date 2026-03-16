package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;

/**
 * View model for product/service create/edit forms.
 *
 * Traces: USA000036
 */
public record ProductServiceFormView(
    ProductServiceDTO item,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static ProductServiceFormView forCreate() {
        return new ProductServiceFormView(null, false, null, false);
    }

    public static ProductServiceFormView forEdit(ProductServiceDTO item) {
        return new ProductServiceFormView(item, true, null, false);
    }

    public static ProductServiceFormView withError(ProductServiceDTO item, boolean isEdit, String message) {
        return new ProductServiceFormView(item, isEdit, message, true);
    }
}
