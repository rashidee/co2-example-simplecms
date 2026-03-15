package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import java.util.List;

public record BlogCategoryListView(
    List<BlogCategoryDTO> categories,
    String successMessage,
    boolean hasSuccess
) {
    public static BlogCategoryListView of(List<BlogCategoryDTO> categories) {
        return new BlogCategoryListView(categories, null, false);
    }
}
