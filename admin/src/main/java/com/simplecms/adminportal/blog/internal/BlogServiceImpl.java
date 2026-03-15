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
import java.time.LocalDateTime;
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
    public Page<BlogPostDTO> listPosts(BlogPostStatus status, LocalDateTime effectiveDate,
                                       LocalDateTime expirationDate, Pageable pageable) {
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
                                  String content, LocalDateTime effectiveDate, LocalDateTime expirationDate,
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
                                  String content, LocalDateTime effectiveDate, LocalDateTime expirationDate,
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
