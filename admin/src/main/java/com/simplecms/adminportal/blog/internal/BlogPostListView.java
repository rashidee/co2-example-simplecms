package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostDTO;
import com.simplecms.adminportal.blog.BlogPostStatus;
import org.springframework.data.domain.Page;

public record BlogPostListView(
    Page<BlogPostDTO> posts,
    BlogPostStatus filterStatus,
    String filterEffectiveDate,
    String filterExpirationDate,
    BlogPostStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static BlogPostListView of(Page<BlogPostDTO> posts, BlogPostStatus filterStatus,
                                       String filterEffectiveDate, String filterExpirationDate) {
        return new BlogPostListView(posts, filterStatus, filterEffectiveDate,
            filterExpirationDate, BlogPostStatus.values(), null, false);
    }
}
