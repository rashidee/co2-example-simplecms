package com.simplecms.adminportal.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Public API for the Blog module.
 *
 * Traces: USA000096-108, NFRA00111-132, CONSA0030-039
 */
public interface BlogService {

    // --- Category Operations ---

    /**
     * List all blog categories.
     *
     * Traces: USA000108
     */
    List<BlogCategoryDTO> listCategories();

    /**
     * Get a category by ID.
     */
    BlogCategoryDTO getCategoryById(UUID id);

    /**
     * Create a new blog category.
     *
     * Traces: USA000105
     */
    BlogCategoryDTO createCategory(String name, String description);

    /**
     * Update an existing blog category.
     *
     * Traces: USA000105
     */
    BlogCategoryDTO updateCategory(UUID id, String name, String description);

    /**
     * Delete a blog category.
     * Throws if blog posts are associated.
     *
     * Traces: USA000108, CONSA0036
     */
    void deleteCategory(UUID id);

    // --- Post Operations ---

    /**
     * List blog posts with optional filters, ordered by effective date desc.
     *
     * Traces: USA000102, NFRA00123
     */
    Page<BlogPostDTO> listPosts(BlogPostStatus status, LocalDateTime effectiveDate,
                                LocalDateTime expirationDate, Pageable pageable);

    /**
     * Get a blog post by ID.
     */
    BlogPostDTO getPostById(UUID id);

    /**
     * Create a new blog post with image upload.
     * Auto-generates slug from title.
     * Sanitizes HTML content via OWASP HTML Sanitizer.
     *
     * Traces: USA000096, NFRA00111, NFRA00114, NFRA00126, NFRA00132, CONSA0033, CONSA0039
     */
    BlogPostDTO createPost(UUID categoryId, UUID authorId, String title, String summary,
                           String content, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                           BlogPostStatus status, MultipartFile image);

    /**
     * Update an existing blog post. Image is optional on update.
     * Re-generates slug if title changes.
     *
     * Traces: USA000096
     */
    BlogPostDTO updatePost(UUID id, UUID categoryId, UUID authorId, String title, String summary,
                           String content, LocalDateTime effectiveDate, LocalDateTime expirationDate,
                           BlogPostStatus status, MultipartFile image);

    /**
     * Delete a blog post by ID.
     *
     * Traces: USA000099
     */
    void deletePost(UUID id);
}
