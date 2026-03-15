# Module Specification: Blog

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000096 | v1.0.0 | As Editor, I want to create/update blog posts with category, title, summary, rich text content, author, image, dates, and status |
| USA000099 | v1.0.0 | As Editor, I want to delete blog content that is no longer relevant |
| USA000102 | v1.0.0 | As Editor, I want to view the list of blog posts in a 3-column card grid with filters |
| USA000105 | v1.0.0 | As Editor, I want to create blog categories to organize blog content |
| USA000108 | v1.0.0 | As Editor, I want to view and manage the list of blog categories |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00111 | v1.0.0 | Blog image must be exactly 1600x500 pixels; validated before upload |
| NFRA00114 | v1.0.0 | Auto-generate thumbnail 400x125 via Scalr |
| NFRA00117 | v1.0.0 | Title max 100 chars |
| NFRA00120 | v1.0.0 | Summary max 300 chars |
| NFRA00123 | v1.0.0 | Blog list ordered by effective date descending |
| NFRA00126 | v1.0.0 | Blog slug auto-generated from title; must be unique |
| NFRA00129 | v1.0.0 | Rich text editor supporting bold, italic, underline, headings, lists, links, images, code blocks, blockquotes |
| NFRA00132 | v1.0.0 | Blog content stored as HTML; sanitized via OWASP HTML Sanitizer to prevent XSS |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0030 | v1.0.0 | Status values: DRAFT (default), READY, ACTIVE, EXPIRED |
| CONSA0033 | v1.0.0 | Blog category must be created before blog post; each post must have one category |
| CONSA0036 | v1.0.0 | Blog category cannot be deleted if associated blog posts exist |
| CONSA0039 | v1.0.0 | Blog author must be a user with EDITOR role; validated before accepting |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.blog`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.blog
  +-- BlogService.java                       (public API - interface)
  +-- BlogPostDTO.java                       (public API - DTO)
  +-- BlogCategoryDTO.java                   (public API - DTO)
  +-- BlogPostStatus.java                    (public API - enum)
  +-- internal
        +-- BlogCategoryEntity.java           (JPA entity)
        +-- BlogPostEntity.java               (JPA entity)
        +-- BlogCategoryRepository.java       (Spring Data JPA)
        +-- BlogPostRepository.java           (Spring Data JPA)
        +-- BlogServiceImpl.java              (service implementation)
        +-- BlogMapper.java                   (MapStruct mapper)
        +-- BlogCategoryPageController.java   (page controller)
        +-- BlogPostPageController.java       (page controller)
        +-- BlogPostFragmentController.java   (fragment controller - htmx)
        +-- BlogCategoryListView.java         (view model)
        +-- BlogCategoryFormView.java         (view model)
        +-- BlogPostListView.java             (view model)
        +-- BlogPostFormView.java             (view model)
```

### JTE Templates

```
src/main/jte
  +-- blog
        +-- BlogCategoryListPage.jte
        +-- BlogCategoryCreatePage.jte
        +-- BlogCategoryEditPage.jte
        +-- BlogPostListPage.jte
        +-- BlogPostCreatePage.jte
        +-- BlogPostEditPage.jte
        +-- fragments
              +-- BlogPostCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/blog/categories` | GET | EDITOR, ADMIN |
| `/blog/categories/create` | GET, POST | EDITOR, ADMIN |
| `/blog/categories/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/blog/categories/{id}/delete` | POST | EDITOR, ADMIN |
| `/blog` | GET | EDITOR, ADMIN |
| `/blog/create` | GET, POST | EDITOR, ADMIN |
| `/blog/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/blog/{id}/delete` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V9__create_blog_tables.sql`

```sql
CREATE TABLE blg_blog_category (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(100)    NOT NULL,
    description     VARCHAR(500),
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_blg_blog_category PRIMARY KEY (id),
    CONSTRAINT uq_blg_blog_category_name UNIQUE (name)
);

CREATE TABLE blg_blog_post (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    category_id     UUID            NOT NULL,
    author_id       UUID            NOT NULL,
    title           VARCHAR(100)    NOT NULL,
    slug            VARCHAR(200)    NOT NULL,
    summary         VARCHAR(300)    NOT NULL,
    content         TEXT            NOT NULL,
    image_path      VARCHAR(500)    NOT NULL,
    thumbnail_path  VARCHAR(500)    NOT NULL,
    effective_date  DATE            NOT NULL,
    expiration_date DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_blg_blog_post PRIMARY KEY (id),
    CONSTRAINT uq_blg_blog_post_slug UNIQUE (slug),
    CONSTRAINT fk_blg_blog_post_category FOREIGN KEY (category_id) REFERENCES blg_blog_category(id),
    CONSTRAINT fk_blg_blog_post_author FOREIGN KEY (author_id) REFERENCES usr_user(id)
);

CREATE INDEX idx_blg_blog_post_category_id ON blg_blog_post(category_id);
CREATE INDEX idx_blg_blog_post_author_id ON blg_blog_post(author_id);
CREATE INDEX idx_blg_blog_post_slug ON blg_blog_post(slug);
CREATE INDEX idx_blg_blog_post_status ON blg_blog_post(status);
CREATE INDEX idx_blg_blog_post_effective_date ON blg_blog_post(effective_date);
```

---

## 5. Public API

### 5.1 BlogPostStatus Enum

```java
package com.simplecms.adminportal.blog;

/**
 * Traces: CONSA0030
 */
public enum BlogPostStatus {
    DRAFT,
    READY,
    ACTIVE,
    EXPIRED
}
```

### 5.2 BlogCategoryDTO

```java
package com.simplecms.adminportal.blog;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000105
 */
public record BlogCategoryDTO(
    UUID id,
    String name,
    String description,
    Instant createdAt
) {}
```

### 5.3 BlogPostDTO

```java
package com.simplecms.adminportal.blog;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Traces: USA000096
 */
public record BlogPostDTO(
    UUID id,
    UUID categoryId,
    String categoryName,
    UUID authorId,
    String authorName,
    String title,
    String slug,
    String summary,
    String content,
    String imagePath,
    String thumbnailPath,
    LocalDate effectiveDate,
    LocalDate expirationDate,
    BlogPostStatus status,
    Instant createdAt
) {}
```

### 5.4 BlogService Interface

```java
package com.simplecms.adminportal.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
     *
     * @param name        category name (unique)
     * @param description category description
     * @return created category DTO
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
     *
     * @param status        optional status filter
     * @param effectiveDate optional effective date filter
     * @param expirationDate optional expiration date filter
     * @param pageable      pagination parameters
     * @return page of blog post DTOs
     */
    Page<BlogPostDTO> listPosts(BlogPostStatus status, LocalDate effectiveDate,
                                LocalDate expirationDate, Pageable pageable);

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
     *
     * @param categoryId     the blog category ID
     * @param authorId       the author user ID (must be EDITOR)
     * @param title          post title (max 100)
     * @param summary        post summary (max 300)
     * @param content        post content (HTML, sanitized)
     * @param effectiveDate  effective date
     * @param expirationDate expiration date (nullable)
     * @param status         post status
     * @param image          uploaded image (1600x500)
     * @return created blog post DTO
     */
    BlogPostDTO createPost(UUID categoryId, UUID authorId, String title, String summary,
                           String content, LocalDate effectiveDate, LocalDate expirationDate,
                           BlogPostStatus status, MultipartFile image);

    /**
     * Update an existing blog post. Image is optional on update.
     * Re-generates slug if title changes.
     *
     * Traces: USA000096
     */
    BlogPostDTO updatePost(UUID id, UUID categoryId, UUID authorId, String title, String summary,
                           String content, LocalDate effectiveDate, LocalDate expirationDate,
                           BlogPostStatus status, MultipartFile image);

    /**
     * Delete a blog post by ID.
     *
     * Traces: USA000099
     */
    void deletePost(UUID id);
}
```

---

## 6. Internal Components

### 6.1 BlogCategoryEntity

```java
package com.simplecms.adminportal.blog.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for blog categories.
 * Table: blg_blog_category
 *
 * Traces: USA000105, CONSA0033
 */
@Entity
@Table(name = "blg_blog_category")
@Getter
@Setter
public class BlogCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    protected BlogCategoryEntity() {
        // JPA
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 6.2 BlogPostEntity

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity for blog posts.
 * Table: blg_blog_post
 *
 * Traces: USA000096, CONSA0030, CONSA0033, CONSA0039
 */
@Entity
@Table(name = "blg_blog_post")
@Getter
@Setter
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;

    @Column(name = "summary", nullable = false, length = 300)
    private String summary;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Column(name = "thumbnail_path", nullable = false, length = 500)
    private String thumbnailPath;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BlogPostStatus status = BlogPostStatus.DRAFT;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    protected BlogPostEntity() {
        // JPA
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 6.3 Repositories

```java
package com.simplecms.adminportal.blog.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface BlogCategoryRepository extends JpaRepository<BlogCategoryEntity, UUID> {

    Optional<BlogCategoryEntity> findByName(String name);

    boolean existsByName(String name);
}
```

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
interface BlogPostRepository extends JpaRepository<BlogPostEntity, UUID> {

    @Query("SELECT p FROM BlogPostEntity p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:effectiveDate IS NULL OR p.effectiveDate >= :effectiveDate) AND " +
           "(:expirationDate IS NULL OR p.expirationDate <= :expirationDate) " +
           "ORDER BY p.effectiveDate DESC")
    Page<BlogPostEntity> findWithFilters(
        @Param("status") BlogPostStatus status,
        @Param("effectiveDate") LocalDate effectiveDate,
        @Param("expirationDate") LocalDate expirationDate,
        Pageable pageable);

    boolean existsByCategoryId(UUID categoryId);

    boolean existsBySlug(String slug);
}
```

### 6.4 BlogMapper (MapStruct)

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface BlogMapper {

    BlogCategoryDTO toCategoryDTO(BlogCategoryEntity entity);

    List<BlogCategoryDTO> toCategoryDTOList(List<BlogCategoryEntity> entities);

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "authorName", ignore = true)
    BlogPostDTO toPostDTO(BlogPostEntity entity);
}
```

### 6.5 BlogServiceImpl

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.*;
import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import org.imgscalr.Scalr;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Traces: USA000096-108, NFRA00111-132, CONSA0030-039
 */
@Service
@Transactional
class BlogServiceImpl implements BlogService {

    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 500;
    private static final int THUMBNAIL_WIDTH = 400;
    private static final int THUMBNAIL_HEIGHT = 125;
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");

    /**
     * OWASP HTML Sanitizer policy for blog content.
     *
     * Traces: NFRA00132
     */
    private static final PolicyFactory HTML_POLICY = new HtmlPolicyBuilder()
        .allowElements("p", "br", "strong", "b", "em", "i", "u", "s", "del",
            "h1", "h2", "h3", "h4", "h5", "h6",
            "ul", "ol", "li",
            "a", "img",
            "pre", "code", "blockquote")
        .allowUrlProtocols("http", "https")
        .allowAttributes("href").onElements("a")
        .allowAttributes("src", "alt", "width", "height").onElements("img")
        .allowAttributes("class").globally()
        .toFactory();

    private final BlogCategoryRepository categoryRepository;
    private final BlogPostRepository postRepository;
    private final BlogMapper mapper;
    private final UserService userService;

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    BlogServiceImpl(BlogCategoryRepository categoryRepository,
                    BlogPostRepository postRepository,
                    BlogMapper mapper,
                    UserService userService) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.userService = userService;
    }

    // --- Category Operations ---

    @Override
    @Transactional(readOnly = true)
    public List<BlogCategoryDTO> listCategories() {
        return mapper.toCategoryDTOList(categoryRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public BlogCategoryDTO getCategoryById(UUID id) {
        BlogCategoryEntity entity = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        return mapper.toCategoryDTO(entity);
    }

    @Override
    public BlogCategoryDTO createCategory(String name, String description) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category name already exists: " + name);
        }

        BlogCategoryEntity entity = new BlogCategoryEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setCreatedBy("EDITOR");

        BlogCategoryEntity saved = categoryRepository.save(entity);
        log.info("Blog category created: {}", saved.getName());
        return mapper.toCategoryDTO(saved);
    }

    @Override
    public BlogCategoryDTO updateCategory(UUID id, String name, String description) {
        BlogCategoryEntity entity = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        if (!entity.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category name already exists: " + name);
        }

        entity.setName(name);
        entity.setDescription(description);
        entity.setUpdatedBy("EDITOR");

        BlogCategoryEntity saved = categoryRepository.save(entity);
        return mapper.toCategoryDTO(saved);
    }

    /**
     * Delete category only if no posts are associated.
     *
     * Traces: CONSA0036
     */
    @Override
    public void deleteCategory(UUID id) {
        BlogCategoryEntity entity = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        if (postRepository.existsByCategoryId(id)) {
            throw new IllegalArgumentException(
                "Cannot delete category: blog posts are associated with this category");
        }

        categoryRepository.delete(entity);
        log.info("Blog category deleted: {}", entity.getName());
    }

    // --- Post Operations ---

    @Override
    @Transactional(readOnly = true)
    public Page<BlogPostDTO> listPosts(BlogPostStatus status, LocalDate effectiveDate,
                                       LocalDate expirationDate, Pageable pageable) {
        return postRepository.findWithFilters(status, effectiveDate, expirationDate, pageable)
            .map(this::enrichPostDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogPostDTO getPostById(UUID id) {
        BlogPostEntity entity = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));
        return enrichPostDTO(entity);
    }

    @Override
    public BlogPostDTO createPost(UUID categoryId, UUID authorId, String title, String summary,
                                  String content, LocalDate effectiveDate, LocalDate expirationDate,
                                  BlogPostStatus status, MultipartFile image) {
        // Validate category exists (CONSA0033)
        categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

        // Validate author is EDITOR (CONSA0039)
        validateAuthorIsEditor(authorId);

        // Validate and save image
        validateImage(image);
        String imagePath = saveImage(image);
        String thumbnailPath = generateThumbnail(imagePath);

        // Generate unique slug (NFRA00126)
        String slug = generateUniqueSlug(title);

        // Sanitize HTML content (NFRA00132)
        String sanitizedContent = HTML_POLICY.sanitize(content);

        BlogPostEntity entity = new BlogPostEntity();
        entity.setCategoryId(categoryId);
        entity.setAuthorId(authorId);
        entity.setTitle(title);
        entity.setSlug(slug);
        entity.setSummary(summary);
        entity.setContent(sanitizedContent);
        entity.setImagePath(imagePath);
        entity.setThumbnailPath(thumbnailPath);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        BlogPostEntity saved = postRepository.save(entity);
        log.info("Blog post created: {} (slug: {})", saved.getId(), saved.getSlug());
        return enrichPostDTO(saved);
    }

    @Override
    public BlogPostDTO updatePost(UUID id, UUID categoryId, UUID authorId, String title, String summary,
                                  String content, LocalDate effectiveDate, LocalDate expirationDate,
                                  BlogPostStatus status, MultipartFile image) {
        BlogPostEntity entity = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

        categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

        validateAuthorIsEditor(authorId);

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imagePath = saveImage(image);
            String thumbnailPath = generateThumbnail(imagePath);
            entity.setImagePath(imagePath);
            entity.setThumbnailPath(thumbnailPath);
        }

        // Re-generate slug if title changed
        if (!entity.getTitle().equals(title)) {
            entity.setSlug(generateUniqueSlug(title));
        }

        String sanitizedContent = HTML_POLICY.sanitize(content);

        entity.setCategoryId(categoryId);
        entity.setAuthorId(authorId);
        entity.setTitle(title);
        entity.setSummary(summary);
        entity.setContent(sanitizedContent);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        BlogPostEntity saved = postRepository.save(entity);
        return enrichPostDTO(saved);
    }

    @Override
    public void deletePost(UUID id) {
        BlogPostEntity entity = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));
        postRepository.delete(entity);
        log.info("Blog post deleted: {}", id);
    }

    // --- Helper Methods ---

    /**
     * Enrich the post DTO with category name and author name.
     */
    private BlogPostDTO enrichPostDTO(BlogPostEntity entity) {
        BlogPostDTO dto = mapper.toPostDTO(entity);

        String categoryName = categoryRepository.findById(entity.getCategoryId())
            .map(BlogCategoryEntity::getName).orElse("Unknown");

        String authorName = "Unknown";
        try {
            UserDTO author = userService.getProfile(entity.getAuthorId());
            authorName = author.firstName() + " " + author.lastName();
        } catch (Exception e) {
            log.warn("Author not found for post: {}", entity.getId());
        }

        return new BlogPostDTO(dto.id(), dto.categoryId(), categoryName,
            dto.authorId(), authorName, dto.title(), dto.slug(), dto.summary(),
            dto.content(), dto.imagePath(), dto.thumbnailPath(),
            dto.effectiveDate(), dto.expirationDate(), dto.status(), dto.createdAt());
    }

    /**
     * Validate that the author has EDITOR role.
     *
     * Traces: CONSA0039
     */
    private void validateAuthorIsEditor(UUID authorId) {
        UserDTO author = userService.getProfile(authorId);
        if (author.role() != com.simplecms.adminportal.user.UserRole.EDITOR &&
            author.role() != com.simplecms.adminportal.user.UserRole.ADMIN) {
            throw new IllegalArgumentException("Author must have EDITOR or ADMIN role");
        }
    }

    /**
     * Generate URL-friendly slug from title, ensuring uniqueness.
     *
     * Traces: NFRA00126
     */
    private String generateUniqueSlug(String title) {
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        String slug = WHITESPACE.matcher(normalized).replaceAll("-");
        slug = NON_LATIN.matcher(slug).replaceAll("");
        slug = slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-")
            .replaceAll("^-|-$", "");

        String baseSlug = slug;
        int counter = 1;
        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    private void validateImage(MultipartFile image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image file");
            }
            if (bufferedImage.getWidth() != IMAGE_WIDTH || bufferedImage.getHeight() != IMAGE_HEIGHT) {
                throw new IllegalArgumentException(
                    String.format("Image must be %dx%d pixels. Got %dx%d.",
                        IMAGE_WIDTH, IMAGE_HEIGHT,
                        bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image file", e);
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String filename = "blog-" + UUID.randomUUID() + getExtension(image.getOriginalFilename());
            Path dir = Paths.get(uploadPath, "blog");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/blog/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private String generateThumbnail(String originalPath) {
        try {
            Path source = Paths.get(uploadPath).resolve(originalPath.replaceFirst("^/uploads/", ""));
            BufferedImage original = ImageIO.read(source.toFile());
            BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

            String thumbFilename = "thumb-" + source.getFileName();
            Path thumbPath = source.getParent().resolve(thumbFilename);
            ImageIO.write(thumbnail, "png", thumbPath.toFile());

            return "/uploads/blog/" + thumbFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate thumbnail", e);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return ".png";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".png";
    }
}
```

### 6.6 View Models

```java
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
```

```java
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
```

```java
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
```

```java
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
```

### 6.7 Page Controllers

#### BlogCategoryPageController

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/blog/categories")
class BlogCategoryPageController {

    private final BlogService blogService;

    BlogCategoryPageController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    String listCategories(Model model) {
        model.addAttribute("view", BlogCategoryListView.of(blogService.listCategories()));
        return "blog/BlogCategoryListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", BlogCategoryFormView.forCreate());
        return "blog/BlogCategoryCreatePage";
    }

    @PostMapping("/create")
    String createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            blogService.createCategory(name, description);
            redirectAttributes.addFlashAttribute("successMessage", "Category created successfully.");
            return "redirect:/blog/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/categories/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        BlogCategoryDTO category = blogService.getCategoryById(id);
        model.addAttribute("view", BlogCategoryFormView.forEdit(category));
        return "blog/BlogCategoryEditPage";
    }

    @PostMapping("/{id}/edit")
    String updateCategory(
            @PathVariable("id") UUID id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            blogService.updateCategory(id, name, description);
            redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully.");
            return "redirect:/blog/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deleteCategory(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/blog/categories";
    }
}
```

#### BlogPostPageController

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostDTO;
import com.simplecms.adminportal.blog.BlogPostStatus;
import com.simplecms.adminportal.blog.BlogService;
import com.simplecms.adminportal.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
class BlogPostPageController {

    private final BlogService blogService;
    private final UserService userService;

    BlogPostPageController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping
    String listPosts(
            @RequestParam(value = "status", required = false) BlogPostStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDate effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate) : null;
        LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate) : null;

        Page<BlogPostDTO> posts = blogService.listPosts(status, effDate, expDate, pageable);
        model.addAttribute("view", BlogPostListView.of(posts, status, effectiveDate, expirationDate));
        return "blog/BlogPostListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", BlogPostFormView.forCreate(
            blogService.listCategories(), userService.listEditors()));
        return "blog/BlogPostCreatePage";
    }

    @PostMapping("/create")
    String createPost(
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("authorId") UUID authorId,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("content") String content,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") BlogPostStatus status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDate effDate = LocalDate.parse(effectiveDate);
            LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate) : null;

            blogService.createPost(categoryId, authorId, title, summary, content,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post created successfully.");
            return "redirect:/blog";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        BlogPostDTO post = blogService.getPostById(id);
        model.addAttribute("view", BlogPostFormView.forEdit(post,
            blogService.listCategories(), userService.listEditors()));
        return "blog/BlogPostEditPage";
    }

    @PostMapping("/{id}/edit")
    String updatePost(
            @PathVariable("id") UUID id,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("authorId") UUID authorId,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("content") String content,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") BlogPostStatus status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDate effDate = LocalDate.parse(effectiveDate);
            LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate) : null;

            blogService.updatePost(id, categoryId, authorId, title, summary, content,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post updated successfully.");
            return "redirect:/blog";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deletePost(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deletePost(id);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/blog";
    }
}
```

#### BlogPostFragmentController

```java
package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostStatus;
import com.simplecms.adminportal.blog.BlogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/blog/fragments")
class BlogPostFragmentController {

    private final BlogService blogService;

    BlogPostFragmentController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) BlogPostStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDate effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate) : null;
        LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate) : null;

        model.addAttribute("posts", blogService.listPosts(status, effDate, expDate, pageable));
        return "blog/fragments/BlogPostCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 BlogPostListPage.jte

```html
@param com.simplecms.adminportal.blog.internal.BlogPostListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Blog Posts</h1>
        <div class="flex gap-3">
            <a href="/blog/categories" class="px-4 py-2 border border-[#c3c4c7] text-sm font-medium rounded-[4px] hover:bg-gray-50">Categories</a>
            <a href="/blog/create" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New Post</a>
        </div>
    </div>

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/blog/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
              class="flex flex-wrap gap-4 items-end">
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Status</label>
                <select name="status" class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px]">
                    <option value="">All</option>
                    @for(var s : view.statuses())
                        <option value="${s.name()}" ${s.equals(view.filterStatus()) ? "selected" : ""}>${s.name()}</option>
                    @endfor
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Effective Date</label>
                <input type="date" name="effectiveDate" value="${view.filterEffectiveDate()}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px]">
            </div>
            <div>
                <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Expiration Date</label>
                <input type="date" name="expirationDate" value="${view.filterExpirationDate()}"
                       class="px-3 py-2 text-sm border border-[#c3c4c7] rounded-[4px]">
            </div>
            <button type="submit" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Filter</button>
        </form>
    </div>

    <div id="card-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for(var post : view.posts().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${post.thumbnailPath()}" alt="${post.title()}" class="w-full h-[125px] object-cover">
                <div class="p-4">
                    <span class="text-xs text-[#2271b1] font-medium">${post.categoryName()}</span>
                    <h3 class="font-semibold text-[#1e1e1e] text-sm mt-1 mb-1">${post.title()}</h3>
                    <p class="text-xs text-[#646970] mb-2 line-clamp-2">${post.summary()}</p>
                    <p class="text-xs text-[#646970] mb-1">By ${post.authorName()}</p>
                    <div class="flex items-center justify-between text-xs text-[#646970]">
                        <span>${post.effectiveDate()} - ${post.expirationDate()}</span>
                        <span class="inline-block px-2 py-0.5 font-medium rounded-full
                            ${post.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              post.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                              post.status().name().equals("EXPIRED") ? "bg-[#d63638]/10 text-[#d63638]" :
                              "bg-[#2271b1]/10 text-[#2271b1]"}">
                            ${post.status().name()}
                        </span>
                    </div>
                    <div class="mt-3 flex gap-3">
                        <a href="/blog/${post.id()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                        <form method="post" action="/blog/${post.id()}/delete" class="inline"
                              x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
                            <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        @endfor
    </div>
    `
)
```

---

## 8. Domain Events

| Event | Trigger | Payload | Traces |
|-------|---------|---------|--------|
| BlogCategoryCreated | New category created | categoryId | USA000105 |
| BlogCategoryDeleted | Category deleted | categoryId | USA000108 |
| BlogPostCreated | New post created | postId, slug | USA000096 |
| BlogPostUpdated | Post updated | postId | USA000096 |
| BlogPostDeleted | Post deleted | postId | USA000099 |

---

## 9. Cross-Module Dependencies

| Dependency | Direction | Purpose | Traces |
|------------|-----------|---------|--------|
| UserService | Blog -> User | listEditors() for author dropdown, getProfile() for author name | CONSA0039 |
| UserDTO | Blog -> User | Read editor user information | CONSA0039 |
| UserRole | Blog -> User | Validate author is EDITOR | CONSA0039 |

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Image validated at exact 1600x500 | Per NFRA00111 | NFRA00111 |
| 2 | Thumbnail 400x125 via Scalr FIT_EXACT | Per NFRA00114 | NFRA00114 |
| 3 | Slug auto-generated from title with uniqueness suffix | Per NFRA00126 | NFRA00126 |
| 4 | HTML content sanitized via OWASP HTML Sanitizer | Per NFRA00132, prevent XSS | NFRA00132 |
| 5 | Category deletion blocked if posts exist | Per CONSA0036 | CONSA0036 |
| 6 | Author must be EDITOR role | Per CONSA0039, cross-module dependency on UserService | CONSA0039 |
| 7 | List ordered by effectiveDate DESC | Per NFRA00123 | NFRA00123 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | BlogCategoryEntity | Blog category with unique name | USA000105, CONSA0033 |
| Entity | BlogPostEntity | Blog post with slug, content, image, dates, status | USA000096, CONSA0030 |
| Enum | BlogPostStatus | DRAFT, READY, ACTIVE, EXPIRED | CONSA0030 |
| Service | BlogService | Public API for category and post CRUD | USA000096-108 |
| Controller | BlogCategoryPageController | Category list/create/edit/delete | USA000105, USA000108 |
| Controller | BlogPostPageController | Post list/create/edit/delete | USA000096-102 |
| Controller | BlogPostFragmentController | htmx fragment for card grid filtering | USA000102 |
| Template | BlogCategoryListPage.jte | Category list table | USA000108 |
| Template | BlogCategoryCreatePage.jte | Create category form | USA000105 |
| Template | BlogCategoryEditPage.jte | Edit category form | USA000105 |
| Template | BlogPostListPage.jte | 3-column card grid with filters | USA000102 |
| Template | BlogPostCreatePage.jte | Create post form with rich text editor | USA000096 |
| Template | BlogPostEditPage.jte | Edit post form | USA000096 |
