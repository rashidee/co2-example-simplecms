# Module Specification: Hero Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000030 | v1.0.0 | As Editor, I want to create/update multiple hero section content with image, headline, subheadline, CTA, and date range |
| USA000033 | v1.0.0 | As Editor, I want to view the list of hero section content in a 3-column card grid with filters |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00018 | v1.0.0 | Expired hero content auto-changes status to EXPIRED; not shown on website but visible in admin |
| NFRA00021 | v1.0.0 | Image must be exactly 1600x500 pixels; validated before upload |
| NFRA00024 | v1.0.0 | Headline max 100 chars, subheadline max 200 chars |
| NFRA00027 | v1.0.0 | CTA URL must be valid URL format |
| NFRA00030 | v1.0.0 | CTA text max 50 chars |
| NFRA00033 | v1.0.0 | List ordered by effective date descending |
| NFRA00036 | v1.0.0 | Auto-generate thumbnail 400x125 via Scalr |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0012 | v1.0.0 | Status values: DRAFT (default), READY, ACTIVE, EXPIRED |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.herosection`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.herosection
  +-- HeroSectionService.java              (public API - interface)
  +-- HeroSectionDTO.java                  (public API - DTO)
  +-- HeroSectionStatus.java               (public API - enum)
  +-- internal
        +-- HeroSectionEntity.java          (JPA entity)
        +-- HeroSectionRepository.java      (Spring Data JPA)
        +-- HeroSectionServiceImpl.java     (service implementation)
        +-- HeroSectionMapper.java          (MapStruct mapper)
        +-- HeroSectionPageController.java  (page controller)
        +-- HeroSectionFragmentController.java (fragment controller - htmx)
        +-- HeroSectionListView.java        (view model)
        +-- HeroSectionFormView.java        (view model)
```

### JTE Templates

```
src/main/jte
  +-- herosection
        +-- HeroSectionListPage.jte
        +-- HeroSectionCreatePage.jte
        +-- HeroSectionEditPage.jte
        +-- fragments
              +-- HeroSectionCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/hero-section` | GET | EDITOR, ADMIN |
| `/hero-section/create` | GET, POST | EDITOR, ADMIN |
| `/hero-section/{id}/edit` | GET, POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V3__create_hero_section_tables.sql`

```sql
CREATE TABLE hrs_hero_section (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    image_path      VARCHAR(500)    NOT NULL,
    thumbnail_path  VARCHAR(500)    NOT NULL,
    headline        VARCHAR(100)    NOT NULL,
    subheadline     VARCHAR(200)    NOT NULL,
    cta_url         VARCHAR(500)    NOT NULL,
    cta_text        VARCHAR(50)     NOT NULL,
    effective_date  DATE            NOT NULL,
    expiration_date DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_hrs_hero_section PRIMARY KEY (id)
);

CREATE INDEX idx_hrs_hero_section_status ON hrs_hero_section(status);
CREATE INDEX idx_hrs_hero_section_effective_date ON hrs_hero_section(effective_date);
```

---

## 5. Public API

### 5.1 HeroSectionStatus Enum

```java
package com.simplecms.adminportal.herosection;

/**
 * Status values for hero section content.
 *
 * Traces: CONSA0012
 */
public enum HeroSectionStatus {
    DRAFT,
    READY,
    ACTIVE,
    EXPIRED
}
```

### 5.2 HeroSectionDTO

```java
package com.simplecms.adminportal.herosection;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Data transfer object for hero section content.
 *
 * Traces: USA000030
 */
public record HeroSectionDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    String headline,
    String subheadline,
    String ctaUrl,
    String ctaText,
    LocalDate effectiveDate,
    LocalDate expirationDate,
    HeroSectionStatus status,
    Instant createdAt
) {}
```

### 5.3 HeroSectionService Interface

```java
package com.simplecms.adminportal.herosection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Public API for the Hero Section module.
 *
 * Traces: USA000030, USA000033, NFRA00018-036, CONSA0012
 */
public interface HeroSectionService {

    /**
     * List hero sections with optional filters, ordered by effective date desc.
     *
     * Traces: USA000033, NFRA00033
     *
     * @param status        optional status filter
     * @param effectiveDate optional effective date filter
     * @param expirationDate optional expiration date filter
     * @param pageable      pagination parameters
     * @return page of hero section DTOs
     */
    Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDate effectiveDate,
                              LocalDate expirationDate, Pageable pageable);

    /**
     * Get a hero section by ID.
     *
     * @param id the hero section ID
     * @return the hero section DTO
     */
    HeroSectionDTO getById(UUID id);

    /**
     * Create a new hero section with image upload.
     * Validates image dimensions (1600x500) and generates thumbnail (400x125).
     *
     * Traces: USA000030, NFRA00021, NFRA00036
     *
     * @param headline       headline text (max 100)
     * @param subheadline    subheadline text (max 200)
     * @param ctaUrl         CTA button URL
     * @param ctaText        CTA button text (max 50)
     * @param effectiveDate  effective date
     * @param expirationDate expiration date (nullable)
     * @param status         content status
     * @param image          uploaded image file
     * @return created hero section DTO
     */
    HeroSectionDTO create(String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDate effectiveDate, LocalDate expirationDate,
                          HeroSectionStatus status, MultipartFile image);

    /**
     * Update an existing hero section. Image is optional on update.
     *
     * Traces: USA000030
     *
     * @param id             the hero section ID
     * @param headline       headline text
     * @param subheadline    subheadline text
     * @param ctaUrl         CTA button URL
     * @param ctaText        CTA button text
     * @param effectiveDate  effective date
     * @param expirationDate expiration date (nullable)
     * @param status         content status
     * @param image          optional new image file
     * @return updated hero section DTO
     */
    HeroSectionDTO update(UUID id, String headline, String subheadline, String ctaUrl,
                          String ctaText, LocalDate effectiveDate, LocalDate expirationDate,
                          HeroSectionStatus status, MultipartFile image);
}
```

---

## 6. Internal Components

### 6.1 HeroSectionEntity

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA entity for hero section content.
 * Table: hrs_hero_section
 *
 * Traces: USA000030, CONSA0012
 */
@Entity
@Table(name = "hrs_hero_section")
@Getter
@Setter
public class HeroSectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Column(name = "thumbnail_path", nullable = false, length = 500)
    private String thumbnailPath;

    @Column(name = "headline", nullable = false, length = 100)
    private String headline;

    @Column(name = "subheadline", nullable = false, length = 200)
    private String subheadline;

    @Column(name = "cta_url", nullable = false, length = 500)
    private String ctaUrl;

    @Column(name = "cta_text", nullable = false, length = 50)
    private String ctaText;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HeroSectionStatus status = HeroSectionStatus.DRAFT;

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

    protected HeroSectionEntity() {
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

### 6.2 HeroSectionRepository

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for HeroSectionEntity.
 *
 * Traces: USA000033, NFRA00033
 */
@Repository
interface HeroSectionRepository extends JpaRepository<HeroSectionEntity, UUID> {

    @Query("SELECT h FROM HeroSectionEntity h WHERE " +
           "(:status IS NULL OR h.status = :status) AND " +
           "(:effectiveDate IS NULL OR h.effectiveDate >= :effectiveDate) AND " +
           "(:expirationDate IS NULL OR h.expirationDate <= :expirationDate) " +
           "ORDER BY h.effectiveDate DESC")
    Page<HeroSectionEntity> findWithFilters(
        @Param("status") HeroSectionStatus status,
        @Param("effectiveDate") LocalDate effectiveDate,
        @Param("expirationDate") LocalDate expirationDate,
        Pageable pageable);

    List<HeroSectionEntity> findByStatusAndExpirationDateBefore(
        HeroSectionStatus status, LocalDate date);
}
```

### 6.3 HeroSectionMapper (MapStruct)

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for HeroSectionEntity to HeroSectionDTO.
 *
 * Traces: USA000030
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface HeroSectionMapper {

    HeroSectionDTO toDTO(HeroSectionEntity entity);

    List<HeroSectionDTO> toDTOList(List<HeroSectionEntity> entities);
}
```

### 6.4 HeroSectionServiceImpl

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.imgscalr.Scalr;
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
import java.time.LocalDate;
import java.util.UUID;

/**
 * Implementation of HeroSectionService.
 *
 * Traces: USA000030, USA000033, NFRA00018-036, CONSA0012
 */
@Service
@Transactional
class HeroSectionServiceImpl implements HeroSectionService {

    private static final Logger log = LoggerFactory.getLogger(HeroSectionServiceImpl.class);
    private static final int IMAGE_WIDTH = 1600;
    private static final int IMAGE_HEIGHT = 500;
    private static final int THUMBNAIL_WIDTH = 400;
    private static final int THUMBNAIL_HEIGHT = 125;

    private final HeroSectionRepository repository;
    private final HeroSectionMapper mapper;

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    HeroSectionServiceImpl(HeroSectionRepository repository, HeroSectionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HeroSectionDTO> list(HeroSectionStatus status, LocalDate effectiveDate,
                                     LocalDate expirationDate, Pageable pageable) {
        return repository.findWithFilters(status, effectiveDate, expirationDate, pageable)
            .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public HeroSectionDTO getById(UUID id) {
        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public HeroSectionDTO create(String headline, String subheadline, String ctaUrl,
                                 String ctaText, LocalDate effectiveDate, LocalDate expirationDate,
                                 HeroSectionStatus status, MultipartFile image) {

        validateImage(image);

        String imagePath = saveImage(image, "hero");
        String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        HeroSectionEntity entity = new HeroSectionEntity();
        entity.setImagePath(imagePath);
        entity.setThumbnailPath(thumbnailPath);
        entity.setHeadline(headline);
        entity.setSubheadline(subheadline);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        HeroSectionEntity saved = repository.save(entity);
        log.info("Hero section created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public HeroSectionDTO update(UUID id, String headline, String subheadline, String ctaUrl,
                                 String ctaText, LocalDate effectiveDate, LocalDate expirationDate,
                                 HeroSectionStatus status, MultipartFile image) {

        HeroSectionEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Hero section not found: " + id));

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imagePath = saveImage(image, "hero");
            String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            entity.setImagePath(imagePath);
            entity.setThumbnailPath(thumbnailPath);
        }

        entity.setHeadline(headline);
        entity.setSubheadline(subheadline);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setEffectiveDate(effectiveDate);
        entity.setExpirationDate(expirationDate);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        HeroSectionEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Validate image dimensions are exactly 1600x500.
     *
     * Traces: NFRA00021
     */
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

    private String saveImage(MultipartFile image, String prefix) {
        try {
            String filename = prefix + "-" + UUID.randomUUID() + getExtension(image.getOriginalFilename());
            Path dir = Paths.get(uploadPath, "hero-section");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/hero-section/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Generate thumbnail using Scalr center-crop.
     *
     * Traces: NFRA00036
     */
    private String generateThumbnail(String originalPath, int width, int height) {
        try {
            Path source = Paths.get(uploadPath).resolve(originalPath.replaceFirst("^/uploads/", ""));
            BufferedImage original = ImageIO.read(source.toFile());
            BufferedImage thumbnail = Scalr.resize(original, Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT, width, height);

            String thumbFilename = "thumb-" + source.getFileName();
            Path thumbPath = source.getParent().resolve(thumbFilename);
            ImageIO.write(thumbnail, "png", thumbPath.toFile());

            return "/uploads/hero-section/" + thumbFilename;
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

### 6.5 View Models

#### HeroSectionListView

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;

/**
 * View model for the hero section list page.
 *
 * Traces: USA000033
 */
public record HeroSectionListView(
    Page<HeroSectionDTO> heroSections,
    HeroSectionStatus filterStatus,
    String filterEffectiveDate,
    String filterExpirationDate,
    HeroSectionStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static HeroSectionListView of(Page<HeroSectionDTO> heroSections,
                                          HeroSectionStatus filterStatus,
                                          String filterEffectiveDate,
                                          String filterExpirationDate) {
        return new HeroSectionListView(heroSections, filterStatus,
            filterEffectiveDate, filterExpirationDate,
            HeroSectionStatus.values(), null, false);
    }
}
```

#### HeroSectionFormView

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionStatus;

/**
 * View model for hero section create/edit forms.
 *
 * Traces: USA000030
 */
public record HeroSectionFormView(
    HeroSectionDTO heroSection,
    boolean isEdit,
    HeroSectionStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static HeroSectionFormView forCreate() {
        return new HeroSectionFormView(null, false, HeroSectionStatus.values(), null, false);
    }

    public static HeroSectionFormView forEdit(HeroSectionDTO heroSection) {
        return new HeroSectionFormView(heroSection, true, HeroSectionStatus.values(), null, false);
    }

    public static HeroSectionFormView withError(HeroSectionDTO heroSection, boolean isEdit, String message) {
        return new HeroSectionFormView(heroSection, isEdit, HeroSectionStatus.values(), message, true);
    }
}
```

### 6.6 Page Controllers

#### HeroSectionPageController

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
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

/**
 * Controller for hero section pages.
 *
 * Traces: USA000030, USA000033
 */
@Controller
@RequestMapping("/hero-section")
class HeroSectionPageController {

    private final HeroSectionService heroSectionService;

    HeroSectionPageController(HeroSectionService heroSectionService) {
        this.heroSectionService = heroSectionService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) HeroSectionStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDate effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate) : null;
        LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate) : null;

        Page<HeroSectionDTO> heroSections = heroSectionService.list(status, effDate, expDate, pageable);
        model.addAttribute("view", HeroSectionListView.of(heroSections, status,
            effectiveDate, expirationDate));
        return "herosection/HeroSectionListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", HeroSectionFormView.forCreate());
        return "herosection/HeroSectionCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("headline") String headline,
            @RequestParam("subheadline") String subheadline,
            @RequestParam("ctaUrl") String ctaUrl,
            @RequestParam("ctaText") String ctaText,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") HeroSectionStatus status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDate effDate = LocalDate.parse(effectiveDate);
            LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate) : null;

            heroSectionService.create(headline, subheadline, ctaUrl, ctaText,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Hero section created successfully.");
            return "redirect:/hero-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hero-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        HeroSectionDTO heroSection = heroSectionService.getById(id);
        model.addAttribute("view", HeroSectionFormView.forEdit(heroSection));
        return "herosection/HeroSectionEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("headline") String headline,
            @RequestParam("subheadline") String subheadline,
            @RequestParam("ctaUrl") String ctaUrl,
            @RequestParam("ctaText") String ctaText,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") HeroSectionStatus status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDate effDate = LocalDate.parse(effectiveDate);
            LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate) : null;

            heroSectionService.update(id, headline, subheadline, ctaUrl, ctaText,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Hero section updated successfully.");
            return "redirect:/hero-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/hero-section/" + id + "/edit";
        }
    }
}
```

#### HeroSectionFragmentController

```java
package com.simplecms.adminportal.herosection.internal;

import com.simplecms.adminportal.herosection.HeroSectionDTO;
import com.simplecms.adminportal.herosection.HeroSectionService;
import com.simplecms.adminportal.herosection.HeroSectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * Fragment controller for htmx partial updates on the hero section list.
 *
 * Traces: USA000033
 */
@Controller
@RequestMapping("/hero-section/fragments")
class HeroSectionFragmentController {

    private final HeroSectionService heroSectionService;

    HeroSectionFragmentController(HeroSectionService heroSectionService) {
        this.heroSectionService = heroSectionService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) HeroSectionStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDate effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate) : null;
        LocalDate expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate) : null;

        Page<HeroSectionDTO> heroSections = heroSectionService.list(status, effDate, expDate, pageable);
        model.addAttribute("heroSections", heroSections);
        return "herosection/fragments/HeroSectionCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 HeroSectionListPage.jte

```html
@param com.simplecms.adminportal.herosection.internal.HeroSectionListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Hero Sections</h1>
        <a href="/hero-section/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
            Add New
        </a>
    </div>

    <!-- Filters -->
    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/hero-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
            <button type="submit"
                    class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
                Filter
            </button>
        </form>
    </div>

    <!-- Card Grid -->
    <div id="card-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        @for(var hero : view.heroSections().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${hero.thumbnailPath()}" alt="${hero.headline()}" class="w-full h-[125px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${hero.headline()}</h3>
                    <p class="text-xs text-[#646970] mb-2">${hero.subheadline()}</p>
                    <div class="flex items-center justify-between text-xs text-[#646970]">
                        <span>${hero.effectiveDate()} - ${hero.expirationDate()}</span>
                        <span class="inline-block px-2 py-0.5 font-medium rounded-full
                            ${hero.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              hero.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                              hero.status().name().equals("EXPIRED") ? "bg-[#d63638]/10 text-[#d63638]" :
                              "bg-[#2271b1]/10 text-[#2271b1]"}">
                            ${hero.status().name()}
                        </span>
                    </div>
                    <div class="mt-3">
                        <a href="/hero-section/${hero.id()}/edit"
                           class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
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
| HeroSectionCreated | New hero section created | heroSectionId | USA000030 |
| HeroSectionUpdated | Hero section updated | heroSectionId | USA000030 |
| HeroSectionExpired | Scheduled job expires content | heroSectionId | NFRA00018 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Image validated at exact 1600x500 | Per NFRA00021, no resizing on upload | NFRA00021 |
| 2 | Thumbnail generated via Scalr FIT_EXACT | Per NFRA00036, 400x125 for list display | NFRA00036 |
| 3 | Expiration handled by scheduled job or query-time check | NFRA00018 auto-expiration | NFRA00018 |
| 4 | List ordered by effectiveDate DESC | Per NFRA00033 | NFRA00033 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | HeroSectionEntity | Hero content with image, CTA, dates, status | USA000030, CONSA0012 |
| Enum | HeroSectionStatus | DRAFT, READY, ACTIVE, EXPIRED | CONSA0012 |
| Service | HeroSectionService | Public API for hero section CRUD | USA000030, USA000033 |
| Controller | HeroSectionPageController | Page controller for list/create/edit | USA000030, USA000033 |
| Controller | HeroSectionFragmentController | htmx fragment for card grid filtering | USA000033 |
| Template | HeroSectionListPage.jte | Card grid list with filters | USA000033 |
| Template | HeroSectionCreatePage.jte | Create form with image upload | USA000030 |
| Template | HeroSectionEditPage.jte | Edit form with optional image | USA000030 |
