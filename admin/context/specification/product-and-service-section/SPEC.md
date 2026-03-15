# Module Specification: Product and Service Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000036 | v1.0.0 | As Editor, I want to create/update multiple product and service content with image, title, description, and optional CTA |
| USA000039 | v1.0.0 | As Editor, I want to set the display order of product and service content |
| USA000042 | v1.0.0 | As Editor, I want to delete product and service content that is no longer relevant |
| USA000045 | v1.0.0 | As Editor, I want to view the list of product and service content in a 4-column card grid with status filter |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00039 | v1.0.0 | Image must be exactly 400x400 pixels; validated before upload |
| NFRA00042 | v1.0.0 | Title max 100 chars |
| NFRA00045 | v1.0.0 | Description max 500 chars |
| NFRA00048 | v1.0.0 | CTA URL must be valid URL format if provided |
| NFRA00051 | v1.0.0 | CTA text max 50 chars if provided |
| NFRA00054 | v1.0.0 | List ordered by displayOrder ASC, then createdAt ASC |
| NFRA00057 | v1.0.0 | Auto-generate thumbnail 200x200 via Scalr |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0015 | v1.0.0 | Status values: DRAFT (default), INACTIVE, ACTIVE |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.productservice`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.productservice
  +-- ProductServiceService.java                (public API - interface)
  +-- ProductServiceDTO.java                    (public API - DTO)
  +-- ProductServiceStatus.java                 (public API - enum)
  +-- internal
        +-- ProductServiceEntity.java            (JPA entity)
        +-- ProductServiceRepository.java        (Spring Data JPA)
        +-- ProductServiceServiceImpl.java       (service implementation)
        +-- ProductServiceMapper.java            (MapStruct mapper)
        +-- ProductServicePageController.java    (page controller)
        +-- ProductServiceFragmentController.java (fragment controller - htmx)
        +-- ProductServiceListView.java          (view model)
        +-- ProductServiceFormView.java          (view model)
```

### JTE Templates

```
src/main/jte
  +-- productservice
        +-- ProductServiceListPage.jte
        +-- ProductServiceCreatePage.jte
        +-- ProductServiceEditPage.jte
        +-- fragments
              +-- ProductServiceCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/product-and-service` | GET | EDITOR, ADMIN |
| `/product-and-service/create` | GET, POST | EDITOR, ADMIN |
| `/product-and-service/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/product-and-service/{id}/delete` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V4__create_product_service_tables.sql`

```sql
CREATE TABLE pns_product_service (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    image_path      VARCHAR(500)    NOT NULL,
    thumbnail_path  VARCHAR(500)    NOT NULL,
    title           VARCHAR(100)    NOT NULL,
    description     VARCHAR(500)    NOT NULL,
    cta_url         VARCHAR(500),
    cta_text        VARCHAR(50),
    display_order   INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_pns_product_service PRIMARY KEY (id)
);

CREATE INDEX idx_pns_product_service_status ON pns_product_service(status);
CREATE INDEX idx_pns_product_service_display_order ON pns_product_service(display_order);
```

---

## 5. Public API

### 5.1 ProductServiceStatus Enum

```java
package com.simplecms.adminportal.productservice;

/**
 * Status values for product and service content.
 *
 * Traces: CONSA0015
 */
public enum ProductServiceStatus {
    DRAFT,
    INACTIVE,
    ACTIVE
}
```

### 5.2 ProductServiceDTO

```java
package com.simplecms.adminportal.productservice;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for product and service content.
 *
 * Traces: USA000036
 */
public record ProductServiceDTO(
    UUID id,
    String imagePath,
    String thumbnailPath,
    String title,
    String description,
    String ctaUrl,
    String ctaText,
    int displayOrder,
    ProductServiceStatus status,
    Instant createdAt
) {}
```

### 5.3 ProductServiceService Interface

```java
package com.simplecms.adminportal.productservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Public API for the Product and Service Section module.
 *
 * Traces: USA000036-045, NFRA00039-057, CONSA0015
 */
public interface ProductServiceService {

    /**
     * List product/service items with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000045, NFRA00054
     *
     * @param status   optional status filter
     * @param pageable pagination parameters
     * @return page of product/service DTOs
     */
    Page<ProductServiceDTO> list(ProductServiceStatus status, Pageable pageable);

    /**
     * Get a product/service item by ID.
     *
     * @param id the item ID
     * @return the product/service DTO
     */
    ProductServiceDTO getById(UUID id);

    /**
     * Create a new product/service item with image upload.
     * Validates image dimensions (400x400) and generates thumbnail (200x200).
     *
     * Traces: USA000036, USA000039, NFRA00039, NFRA00057
     *
     * @param title        title text (max 100)
     * @param description  description text (max 500)
     * @param ctaUrl       optional CTA URL
     * @param ctaText      optional CTA text (max 50)
     * @param displayOrder display order integer
     * @param status       content status
     * @param image        uploaded image file
     * @return created product/service DTO
     */
    ProductServiceDTO create(String title, String description, String ctaUrl,
                             String ctaText, int displayOrder,
                             ProductServiceStatus status, MultipartFile image);

    /**
     * Update an existing product/service item. Image is optional on update.
     *
     * Traces: USA000036, USA000039
     *
     * @param id           the item ID
     * @param title        title text
     * @param description  description text
     * @param ctaUrl       optional CTA URL
     * @param ctaText      optional CTA text
     * @param displayOrder display order integer
     * @param status       content status
     * @param image        optional new image file
     * @return updated product/service DTO
     */
    ProductServiceDTO update(UUID id, String title, String description, String ctaUrl,
                             String ctaText, int displayOrder,
                             ProductServiceStatus status, MultipartFile image);

    /**
     * Delete a product/service item by ID.
     *
     * Traces: USA000042
     *
     * @param id the item ID
     */
    void delete(UUID id);
}
```

---

## 6. Internal Components

### 6.1 ProductServiceEntity

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for product and service content.
 * Table: pns_product_service
 *
 * Traces: USA000036, CONSA0015
 */
@Entity
@Table(name = "pns_product_service")
@Getter
@Setter
public class ProductServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "image_path", nullable = false, length = 500)
    private String imagePath;

    @Column(name = "thumbnail_path", nullable = false, length = 500)
    private String thumbnailPath;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "cta_url", length = 500)
    private String ctaUrl;

    @Column(name = "cta_text", length = 50)
    private String ctaText;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductServiceStatus status = ProductServiceStatus.DRAFT;

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

    protected ProductServiceEntity() {
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

### 6.2 ProductServiceRepository

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for ProductServiceEntity.
 *
 * Traces: USA000045, NFRA00054
 */
@Repository
interface ProductServiceRepository extends JpaRepository<ProductServiceEntity, UUID> {

    @Query("SELECT p FROM ProductServiceEntity p WHERE " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.displayOrder ASC, p.createdAt ASC")
    Page<ProductServiceEntity> findWithFilters(
        @Param("status") ProductServiceStatus status,
        Pageable pageable);
}
```

### 6.3 ProductServiceMapper (MapStruct)

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for ProductServiceEntity to ProductServiceDTO.
 *
 * Traces: USA000036
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ProductServiceMapper {

    ProductServiceDTO toDTO(ProductServiceEntity entity);

    List<ProductServiceDTO> toDTOList(List<ProductServiceEntity> entities);
}
```

### 6.4 ProductServiceServiceImpl

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
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
import java.util.UUID;

/**
 * Implementation of ProductServiceService.
 *
 * Traces: USA000036-045, NFRA00039-057, CONSA0015
 */
@Service
@Transactional
class ProductServiceServiceImpl implements ProductServiceService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceServiceImpl.class);
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 400;
    private static final int THUMBNAIL_WIDTH = 200;
    private static final int THUMBNAIL_HEIGHT = 200;

    private final ProductServiceRepository repository;
    private final ProductServiceMapper mapper;

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    ProductServiceServiceImpl(ProductServiceRepository repository, ProductServiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductServiceDTO> list(ProductServiceStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductServiceDTO getById(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public ProductServiceDTO create(String title, String description, String ctaUrl,
                                    String ctaText, int displayOrder,
                                    ProductServiceStatus status, MultipartFile image) {
        validateImage(image);

        String imagePath = saveImage(image, "product");
        String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        ProductServiceEntity entity = new ProductServiceEntity();
        entity.setImagePath(imagePath);
        entity.setThumbnailPath(thumbnailPath);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        ProductServiceEntity saved = repository.save(entity);
        log.info("Product/Service created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public ProductServiceDTO update(UUID id, String title, String description, String ctaUrl,
                                    String ctaText, int displayOrder,
                                    ProductServiceStatus status, MultipartFile image) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));

        if (image != null && !image.isEmpty()) {
            validateImage(image);
            String imagePath = saveImage(image, "product");
            String thumbnailPath = generateThumbnail(imagePath, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
            entity.setImagePath(imagePath);
            entity.setThumbnailPath(thumbnailPath);
        }

        entity.setTitle(title);
        entity.setDescription(description);
        entity.setCtaUrl(ctaUrl);
        entity.setCtaText(ctaText);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        ProductServiceEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        ProductServiceEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Product/Service not found: " + id));
        repository.delete(entity);
        log.info("Product/Service deleted: {}", id);
    }

    /**
     * Validate image dimensions are exactly 400x400.
     *
     * Traces: NFRA00039
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
            Path dir = Paths.get(uploadPath, "product-service");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/product-service/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    /**
     * Generate thumbnail using Scalr center-crop.
     *
     * Traces: NFRA00057
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

            return "/uploads/product-service/" + thumbFilename;
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

#### ProductServiceListView

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;

/**
 * Traces: USA000045
 */
public record ProductServiceListView(
    Page<ProductServiceDTO> items,
    ProductServiceStatus filterStatus,
    ProductServiceStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static ProductServiceListView of(Page<ProductServiceDTO> items, ProductServiceStatus filterStatus) {
        return new ProductServiceListView(items, filterStatus, ProductServiceStatus.values(), null, false);
    }
}
```

#### ProductServiceFormView

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceStatus;

/**
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
```

### 6.6 Page Controllers

#### ProductServicePageController

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Controller for product and service section pages.
 *
 * Traces: USA000036-045
 */
@Controller
@RequestMapping("/product-and-service")
class ProductServicePageController {

    private final ProductServiceService productServiceService;

    ProductServicePageController(ProductServiceService productServiceService) {
        this.productServiceService = productServiceService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) ProductServiceStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<ProductServiceDTO> items = productServiceService.list(status, pageable);
        model.addAttribute("view", ProductServiceListView.of(items, status));
        return "productservice/ProductServiceListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", ProductServiceFormView.forCreate());
        return "productservice/ProductServiceCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "ctaUrl", required = false) String ctaUrl,
            @RequestParam(value = "ctaText", required = false) String ctaText,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") ProductServiceStatus status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            productServiceService.create(title, description, ctaUrl, ctaText, displayOrder, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service created successfully.");
            return "redirect:/product-and-service";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/product-and-service/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        ProductServiceDTO item = productServiceService.getById(id);
        model.addAttribute("view", ProductServiceFormView.forEdit(item));
        return "productservice/ProductServiceEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "ctaUrl", required = false) String ctaUrl,
            @RequestParam(value = "ctaText", required = false) String ctaText,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") ProductServiceStatus status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            productServiceService.update(id, title, description, ctaUrl, ctaText, displayOrder, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service updated successfully.");
            return "redirect:/product-and-service";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/product-and-service/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            productServiceService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product/Service deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/product-and-service";
    }
}
```

#### ProductServiceFragmentController

```java
package com.simplecms.adminportal.productservice.internal;

import com.simplecms.adminportal.productservice.ProductServiceDTO;
import com.simplecms.adminportal.productservice.ProductServiceService;
import com.simplecms.adminportal.productservice.ProductServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fragment controller for htmx partial updates.
 *
 * Traces: USA000045
 */
@Controller
@RequestMapping("/product-and-service/fragments")
class ProductServiceFragmentController {

    private final ProductServiceService productServiceService;

    ProductServiceFragmentController(ProductServiceService productServiceService) {
        this.productServiceService = productServiceService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) ProductServiceStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<ProductServiceDTO> items = productServiceService.list(status, pageable);
        model.addAttribute("items", items);
        return "productservice/fragments/ProductServiceCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 ProductServiceListPage.jte

```html
@param com.simplecms.adminportal.productservice.internal.ProductServiceListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Products & Services</h1>
        <a href="/product-and-service/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">
            Add New
        </a>
    </div>

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/product-and-service/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
            <button type="submit" class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Filter</button>
        </form>
    </div>

    <div id="card-grid" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        @for(var item : view.items().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${item.thumbnailPath()}" alt="${item.title()}" class="w-full h-[200px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${item.title()}</h3>
                    <p class="text-xs text-[#646970] mb-2 line-clamp-2">${item.description()}</p>
                    <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                        ${item.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                          item.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                          "bg-[#d63638]/10 text-[#d63638]"}">
                        ${item.status().name()}
                    </span>
                    <div class="mt-3 flex gap-3">
                        <a href="/product-and-service/${item.id()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                        <form method="post" action="/product-and-service/${item.id()}/delete" class="inline"
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
| ProductServiceCreated | New item created | itemId | USA000036 |
| ProductServiceUpdated | Item updated | itemId | USA000036 |
| ProductServiceDeleted | Item deleted | itemId | USA000042 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Image validated at exact 400x400 | Per NFRA00039 | NFRA00039 |
| 2 | Thumbnail 200x200 via Scalr FIT_EXACT | Per NFRA00057 | NFRA00057 |
| 3 | List ordered by displayOrder ASC, createdAt ASC | Per NFRA00054 | NFRA00054 |
| 4 | CTA fields are nullable | Per USA000036, CTA is optional | USA000036 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | ProductServiceEntity | Product/service content with image, CTA, order, status | USA000036, CONSA0015 |
| Enum | ProductServiceStatus | DRAFT, INACTIVE, ACTIVE | CONSA0015 |
| Service | ProductServiceService | Public API for product/service CRUD | USA000036-045 |
| Controller | ProductServicePageController | Page controller for list/create/edit/delete | USA000036-045 |
| Controller | ProductServiceFragmentController | htmx fragment for card grid filtering | USA000045 |
| Template | ProductServiceListPage.jte | 4-column card grid with status filter | USA000045 |
| Template | ProductServiceCreatePage.jte | Create form with image upload | USA000036 |
| Template | ProductServiceEditPage.jte | Edit form with optional image | USA000036 |
