package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;

public record BlogCategoryFormView(
    BlogCategoryDTO category,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static BlogCategoryFormView forCreate() {
        return new BlogCategoryFormView(null, false, null, false);
    }

    public static BlogCategoryFormView forEdit(BlogCategoryDTO category) {
        return new BlogCategoryFormView(category, true, null, false);
    }

    public static BlogCategoryFormView withError(BlogCategoryDTO category, boolean isEdit, String message) {
        return new BlogCategoryFormView(category, isEdit, message, true);
    }
}
