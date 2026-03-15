# Module Specification: Features Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000048 | v1.0.0 | As Editor, I want to create/update multiple features with icon, title, description, and status |
| USA000051 | v1.0.0 | As Editor, I want to set the display order of features content |
| USA000054 | v1.0.0 | As Editor, I want to delete features content that is no longer relevant |
| USA000057 | v1.0.0 | As Editor, I want to view the list of features in a 4-column card grid with status filter |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00060 | v1.0.0 | Title max 100 chars |
| NFRA00063 | v1.0.0 | Description max 500 chars |
| NFRA00066 | v1.0.0 | List ordered by displayOrder ASC, then createdAt ASC |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0018 | v1.0.0 | Status values: DRAFT (default), INACTIVE, ACTIVE |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.featuressection`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.featuressection
  +-- FeatureService.java                    (public API - interface)
  +-- FeatureDTO.java                        (public API - DTO)
  +-- FeatureStatus.java                     (public API - enum)
  +-- internal
        +-- FeatureEntity.java                (JPA entity)
        +-- FeatureRepository.java            (Spring Data JPA)
        +-- FeatureServiceImpl.java           (service implementation)
        +-- FeatureMapper.java                (MapStruct mapper)
        +-- FeaturePageController.java        (page controller)
        +-- FeatureFragmentController.java    (fragment controller - htmx)
        +-- FeatureListView.java              (view model)
        +-- FeatureFormView.java              (view model)
```

### JTE Templates

```
src/main/jte
  +-- featuressection
        +-- FeatureListPage.jte
        +-- FeatureCreatePage.jte
        +-- FeatureEditPage.jte
        +-- fragments
              +-- FeatureCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/features-section` | GET | EDITOR, ADMIN |
| `/features-section/create` | GET, POST | EDITOR, ADMIN |
| `/features-section/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/features-section/{id}/delete` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V5__create_feature_tables.sql`

```sql
CREATE TABLE fts_feature (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    icon            VARCHAR(100)    NOT NULL,
    title           VARCHAR(100)    NOT NULL,
    description     VARCHAR(500)    NOT NULL,
    display_order   INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_fts_feature PRIMARY KEY (id)
);

CREATE INDEX idx_fts_feature_status ON fts_feature(status);
CREATE INDEX idx_fts_feature_display_order ON fts_feature(display_order);
```

---

## 5. Public API

### 5.1 FeatureStatus Enum

```java
package com.simplecms.adminportal.featuressection;

/**
 * Status values for feature content.
 *
 * Traces: CONSA0018
 */
public enum FeatureStatus {
    DRAFT,
    INACTIVE,
    ACTIVE
}
```

### 5.2 FeatureDTO

```java
package com.simplecms.adminportal.featuressection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000048
 */
public record FeatureDTO(
    UUID id,
    String icon,
    String title,
    String description,
    int displayOrder,
    FeatureStatus status,
    Instant createdAt
) {}
```

### 5.3 FeatureService Interface

```java
package com.simplecms.adminportal.featuressection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Features Section module.
 *
 * Traces: USA000048-057, NFRA00060-066, CONSA0018
 */
public interface FeatureService {

    /**
     * List features with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000057, NFRA00066
     */
    Page<FeatureDTO> list(FeatureStatus status, Pageable pageable);

    /**
     * Get a feature by ID.
     */
    FeatureDTO getById(UUID id);

    /**
     * Create a new feature.
     *
     * Traces: USA000048, USA000051
     *
     * @param icon         FontAwesome CSS class
     * @param title        title text (max 100)
     * @param description  description text (max 500)
     * @param displayOrder display order integer
     * @param status       content status
     * @return created feature DTO
     */
    FeatureDTO create(String icon, String title, String description,
                      int displayOrder, FeatureStatus status);

    /**
     * Update an existing feature.
     *
     * Traces: USA000048, USA000051
     */
    FeatureDTO update(UUID id, String icon, String title, String description,
                      int displayOrder, FeatureStatus status);

    /**
     * Delete a feature by ID.
     *
     * Traces: USA000054
     */
    void delete(UUID id);
}
```

---

## 6. Internal Components

### 6.1 FeatureEntity

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for features.
 * Table: fts_feature
 *
 * Traces: USA000048, CONSA0018
 */
@Entity
@Table(name = "fts_feature")
@Getter
@Setter
public class FeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "icon", nullable = false, length = 100)
    private String icon;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FeatureStatus status = FeatureStatus.DRAFT;

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

    protected FeatureEntity() {
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

### 6.2 FeatureRepository

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Traces: USA000057, NFRA00066
 */
@Repository
interface FeatureRepository extends JpaRepository<FeatureEntity, UUID> {

    @Query("SELECT f FROM FeatureEntity f WHERE " +
           "(:status IS NULL OR f.status = :status) " +
           "ORDER BY f.displayOrder ASC, f.createdAt ASC")
    Page<FeatureEntity> findWithFilters(
        @Param("status") FeatureStatus status,
        Pageable pageable);
}
```

### 6.3 FeatureMapper (MapStruct)

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FeatureMapper {

    FeatureDTO toDTO(FeatureEntity entity);

    List<FeatureDTO> toDTOList(List<FeatureEntity> entities);
}
```

### 6.4 FeatureServiceImpl

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureDTO;
import com.simplecms.adminportal.featuressection.FeatureService;
import com.simplecms.adminportal.featuressection.FeatureStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of FeatureService.
 *
 * Traces: USA000048-057, NFRA00060-066, CONSA0018
 */
@Service
@Transactional
class FeatureServiceImpl implements FeatureService {

    private static final Logger log = LoggerFactory.getLogger(FeatureServiceImpl.class);

    private final FeatureRepository repository;
    private final FeatureMapper mapper;

    FeatureServiceImpl(FeatureRepository repository, FeatureMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeatureDTO> list(FeatureStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureDTO getById(UUID id) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public FeatureDTO create(String icon, String title, String description,
                             int displayOrder, FeatureStatus status) {
        FeatureEntity entity = new FeatureEntity();
        entity.setIcon(icon);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        FeatureEntity saved = repository.save(entity);
        log.info("Feature created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public FeatureDTO update(UUID id, String icon, String title, String description,
                             int displayOrder, FeatureStatus status) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));

        entity.setIcon(icon);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        FeatureEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        FeatureEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));
        repository.delete(entity);
        log.info("Feature deleted: {}", id);
    }
}
```

### 6.5 View Models

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureDTO;
import com.simplecms.adminportal.featuressection.FeatureStatus;
import org.springframework.data.domain.Page;

public record FeatureListView(
    Page<FeatureDTO> features,
    FeatureStatus filterStatus,
    FeatureStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static FeatureListView of(Page<FeatureDTO> features, FeatureStatus filterStatus) {
        return new FeatureListView(features, filterStatus, FeatureStatus.values(), null, false);
    }
}
```

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureDTO;
import com.simplecms.adminportal.featuressection.FeatureStatus;

public record FeatureFormView(
    FeatureDTO feature,
    boolean isEdit,
    FeatureStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static FeatureFormView forCreate() {
        return new FeatureFormView(null, false, FeatureStatus.values(), null, false);
    }

    public static FeatureFormView forEdit(FeatureDTO feature) {
        return new FeatureFormView(feature, true, FeatureStatus.values(), null, false);
    }

    public static FeatureFormView withError(FeatureDTO feature, boolean isEdit, String message) {
        return new FeatureFormView(feature, isEdit, FeatureStatus.values(), message, true);
    }
}
```

### 6.6 Page Controllers

#### FeaturePageController

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureDTO;
import com.simplecms.adminportal.featuressection.FeatureService;
import com.simplecms.adminportal.featuressection.FeatureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Traces: USA000048-057
 */
@Controller
@RequestMapping("/features-section")
class FeaturePageController {

    private final FeatureService featureService;

    FeaturePageController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) FeatureStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<FeatureDTO> features = featureService.list(status, pageable);
        model.addAttribute("view", FeatureListView.of(features, status));
        return "featuressection/FeatureListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", FeatureFormView.forCreate());
        return "featuressection/FeatureCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("icon") String icon,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") FeatureStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            featureService.create(icon, title, description, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Feature created successfully.");
            return "redirect:/features-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/features-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        FeatureDTO feature = featureService.getById(id);
        model.addAttribute("view", FeatureFormView.forEdit(feature));
        return "featuressection/FeatureEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("icon") String icon,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") FeatureStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            featureService.update(id, icon, title, description, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Feature updated successfully.");
            return "redirect:/features-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/features-section/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            featureService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Feature deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/features-section";
    }
}
```

#### FeatureFragmentController

```java
package com.simplecms.adminportal.featuressection.internal;

import com.simplecms.adminportal.featuressection.FeatureService;
import com.simplecms.adminportal.featuressection.FeatureStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/features-section/fragments")
class FeatureFragmentController {

    private final FeatureService featureService;

    FeatureFragmentController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) FeatureStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("features", featureService.list(status, pageable));
        return "featuressection/fragments/FeatureCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 FeatureListPage.jte

```html
@param com.simplecms.adminportal.featuressection.internal.FeatureListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Features</h1>
        <a href="/features-section/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New</a>
    </div>

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/features-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
        @for(var feature : view.features().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4">
                <div class="text-3xl text-[#2271b1] mb-3"><i class="${feature.icon()}"></i></div>
                <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${feature.title()}</h3>
                <p class="text-xs text-[#646970] mb-2 line-clamp-3">${feature.description()}</p>
                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                    ${feature.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      feature.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                      "bg-[#d63638]/10 text-[#d63638]"}">
                    ${feature.status().name()}
                </span>
                <div class="mt-3 flex gap-3">
                    <a href="/features-section/${feature.id()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                    <form method="post" action="/features-section/${feature.id()}/delete" class="inline"
                          x-data x-on:submit.prevent="if(confirm('Are you sure?')) $el.submit()">
                        <button type="submit" class="text-sm text-[#d63638] hover:underline">Delete</button>
                    </form>
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
| FeatureCreated | New feature created | featureId | USA000048 |
| FeatureUpdated | Feature updated | featureId | USA000048 |
| FeatureDeleted | Feature deleted | featureId | USA000054 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Icon stored as FontAwesome CSS class string | Per PRD, predefined icon list from FontAwesome | USA000048 |
| 2 | No image upload required | Features use icons instead of images | USA000048 |
| 3 | List ordered by displayOrder ASC, createdAt ASC | Per NFRA00066 | NFRA00066 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | FeatureEntity | Feature with icon, title, description, order, status | USA000048, CONSA0018 |
| Enum | FeatureStatus | DRAFT, INACTIVE, ACTIVE | CONSA0018 |
| Service | FeatureService | Public API for feature CRUD | USA000048-057 |
| Controller | FeaturePageController | Page controller for list/create/edit/delete | USA000048-057 |
| Controller | FeatureFragmentController | htmx fragment for card grid filtering | USA000057 |
| Template | FeatureListPage.jte | 4-column card grid with icons | USA000057 |
| Template | FeatureCreatePage.jte | Create form with icon selection | USA000048 |
| Template | FeatureEditPage.jte | Edit form | USA000048 |
