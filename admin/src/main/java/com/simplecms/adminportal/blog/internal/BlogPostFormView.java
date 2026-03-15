package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogPostDTO;
import com.simplecms.adminportal.blog.BlogPostStatus;
import com.simplecms.adminportal.user.UserDTO;
import java.util.List;

public record BlogPostFormView(
    BlogPostDTO post,
    boolean isEdit,
    List<BlogCategoryDTO> categories,
    List<UserDTO> editors,
    BlogPostStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static BlogPostFormView forCreate(List<BlogCategoryDTO> categories, List<UserDTO> editors) {
        return new BlogPostFormView(null, false, categories, editors, BlogPostStatus.values(), null, false);
    }

    public static BlogPostFormView forEdit(BlogPostDTO post, List<BlogCategoryDTO> categories, List<UserDTO> editors) {
        return new BlogPostFormView(post, true, categories, editors, BlogPostStatus.values(), null, false);
    }

    public static BlogPostFormView withError(BlogPostDTO post, boolean isEdit,
                                              List<BlogCategoryDTO> categories, List<UserDTO> editors, String message) {
        return new BlogPostFormView(post, isEdit, categories, editors, BlogPostStatus.values(), message, true);
    }
}
