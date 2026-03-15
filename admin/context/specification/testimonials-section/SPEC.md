# Module Specification: Testimonials Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000060 | v1.0.0 | As Editor, I want to create/update multiple testimonials with customer name, review, rating, and status |
| USA000063 | v1.0.0 | As Editor, I want to set the display order of testimonials content |
| USA000066 | v1.0.0 | As Editor, I want to delete testimonials content that is no longer relevant |
| USA000069 | v1.0.0 | As Editor, I want to view the list of testimonials in a 4-column card grid with status filter |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00069 | v1.0.0 | Customer name max 100 chars |
| NFRA00072 | v1.0.0 | Customer review max 1000 chars |
| NFRA00075 | v1.0.0 | Customer rating must be integer 1-5 |
| NFRA00078 | v1.0.0 | List ordered by displayOrder ASC, then createdAt ASC |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0021 | v1.0.0 | Status values: DRAFT (default), INACTIVE, ACTIVE |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.testimonials`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.testimonials
  +-- TestimonialService.java                (public API - interface)
  +-- TestimonialDTO.java                    (public API - DTO)
  +-- TestimonialStatus.java                 (public API - enum)
  +-- internal
        +-- TestimonialEntity.java            (JPA entity)
        +-- TestimonialRepository.java        (Spring Data JPA)
        +-- TestimonialServiceImpl.java       (service implementation)
        +-- TestimonialMapper.java            (MapStruct mapper)
        +-- TestimonialPageController.java    (page controller)
        +-- TestimonialFragmentController.java (fragment controller - htmx)
        +-- TestimonialListView.java          (view model)
        +-- TestimonialFormView.java          (view model)
```

### JTE Templates

```
src/main/jte
  +-- testimonials
        +-- TestimonialListPage.jte
        +-- TestimonialCreatePage.jte
        +-- TestimonialEditPage.jte
        +-- fragments
              +-- TestimonialCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/testimonials` | GET | EDITOR, ADMIN |
| `/testimonials/create` | GET, POST | EDITOR, ADMIN |
| `/testimonials/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/testimonials/{id}/delete` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V6__create_testimonial_tables.sql`

```sql
CREATE TABLE tst_testimonial (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    customer_name   VARCHAR(100)    NOT NULL,
    customer_review VARCHAR(1000)   NOT NULL,
    customer_rating INTEGER         NOT NULL,
    display_order   INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_tst_testimonial PRIMARY KEY (id),
    CONSTRAINT chk_tst_testimonial_rating CHECK (customer_rating BETWEEN 1 AND 5)
);

CREATE INDEX idx_tst_testimonial_status ON tst_testimonial(status);
CREATE INDEX idx_tst_testimonial_display_order ON tst_testimonial(display_order);
```

---

## 5. Public API

### 5.1 TestimonialStatus Enum

```java
package com.simplecms.adminportal.testimonials;

/**
 * Traces: CONSA0021
 */
public enum TestimonialStatus {
    DRAFT,
    INACTIVE,
    ACTIVE
}
```

### 5.2 TestimonialDTO

```java
package com.simplecms.adminportal.testimonials;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000060
 */
public record TestimonialDTO(
    UUID id,
    String customerName,
    String customerReview,
    int customerRating,
    int displayOrder,
    TestimonialStatus status,
    Instant createdAt
) {}
```

### 5.3 TestimonialService Interface

```java
package com.simplecms.adminportal.testimonials;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Testimonials Section module.
 *
 * Traces: USA000060-069, NFRA00069-078, CONSA0021
 */
public interface TestimonialService {

    /**
     * List testimonials with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000069, NFRA00078
     */
    Page<TestimonialDTO> list(TestimonialStatus status, Pageable pageable);

    /**
     * Get a testimonial by ID.
     */
    TestimonialDTO getById(UUID id);

    /**
     * Create a new testimonial.
     *
     * Traces: USA000060, USA000063
     *
     * @param customerName   customer name (max 100)
     * @param customerReview customer review (max 1000)
     * @param customerRating rating 1-5
     * @param displayOrder   display order integer
     * @param status         content status
     * @return created testimonial DTO
     */
    TestimonialDTO create(String customerName, String customerReview,
                          int customerRating, int displayOrder, TestimonialStatus status);

    /**
     * Update an existing testimonial.
     *
     * Traces: USA000060, USA000063
     */
    TestimonialDTO update(UUID id, String customerName, String customerReview,
                          int customerRating, int displayOrder, TestimonialStatus status);

    /**
     * Delete a testimonial by ID.
     *
     * Traces: USA000066
     */
    void delete(UUID id);
}
```

---

## 6. Internal Components

### 6.1 TestimonialEntity

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for testimonials.
 * Table: tst_testimonial
 *
 * Traces: USA000060, CONSA0021
 */
@Entity
@Table(name = "tst_testimonial")
@Getter
@Setter
public class TestimonialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_review", nullable = false, length = 1000)
    private String customerReview;

    @Column(name = "customer_rating", nullable = false)
    private int customerRating;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TestimonialStatus status = TestimonialStatus.DRAFT;

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

    protected TestimonialEntity() {
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

### 6.2 TestimonialRepository

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface TestimonialRepository extends JpaRepository<TestimonialEntity, UUID> {

    @Query("SELECT t FROM TestimonialEntity t WHERE " +
           "(:status IS NULL OR t.status = :status) " +
           "ORDER BY t.displayOrder ASC, t.createdAt ASC")
    Page<TestimonialEntity> findWithFilters(
        @Param("status") TestimonialStatus status,
        Pageable pageable);
}
```

### 6.3 TestimonialMapper (MapStruct)

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface TestimonialMapper {

    TestimonialDTO toDTO(TestimonialEntity entity);

    List<TestimonialDTO> toDTOList(List<TestimonialEntity> entities);
}
```

### 6.4 TestimonialServiceImpl

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialDTO;
import com.simplecms.adminportal.testimonials.TestimonialService;
import com.simplecms.adminportal.testimonials.TestimonialStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Traces: USA000060-069, NFRA00069-078, CONSA0021
 */
@Service
@Transactional
class TestimonialServiceImpl implements TestimonialService {

    private static final Logger log = LoggerFactory.getLogger(TestimonialServiceImpl.class);

    private final TestimonialRepository repository;
    private final TestimonialMapper mapper;

    TestimonialServiceImpl(TestimonialRepository repository, TestimonialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestimonialDTO> list(TestimonialStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TestimonialDTO getById(UUID id) {
        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public TestimonialDTO create(String customerName, String customerReview,
                                 int customerRating, int displayOrder, TestimonialStatus status) {
        if (customerRating < 1 || customerRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        TestimonialEntity entity = new TestimonialEntity();
        entity.setCustomerName(customerName);
        entity.setCustomerReview(customerReview);
        entity.setCustomerRating(customerRating);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        TestimonialEntity saved = repository.save(entity);
        log.info("Testimonial created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public TestimonialDTO update(UUID id, String customerName, String customerReview,
                                 int customerRating, int displayOrder, TestimonialStatus status) {
        if (customerRating < 1 || customerRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));

        entity.setCustomerName(customerName);
        entity.setCustomerReview(customerReview);
        entity.setCustomerRating(customerRating);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        TestimonialEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        TestimonialEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Testimonial not found: " + id));
        repository.delete(entity);
        log.info("Testimonial deleted: {}", id);
    }
}
```

### 6.5 View Models

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialDTO;
import com.simplecms.adminportal.testimonials.TestimonialStatus;
import org.springframework.data.domain.Page;

public record TestimonialListView(
    Page<TestimonialDTO> testimonials,
    TestimonialStatus filterStatus,
    TestimonialStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static TestimonialListView of(Page<TestimonialDTO> testimonials, TestimonialStatus filterStatus) {
        return new TestimonialListView(testimonials, filterStatus, TestimonialStatus.values(), null, false);
    }
}
```

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialDTO;
import com.simplecms.adminportal.testimonials.TestimonialStatus;

public record TestimonialFormView(
    TestimonialDTO testimonial,
    boolean isEdit,
    TestimonialStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static TestimonialFormView forCreate() {
        return new TestimonialFormView(null, false, TestimonialStatus.values(), null, false);
    }

    public static TestimonialFormView forEdit(TestimonialDTO testimonial) {
        return new TestimonialFormView(testimonial, true, TestimonialStatus.values(), null, false);
    }

    public static TestimonialFormView withError(TestimonialDTO testimonial, boolean isEdit, String message) {
        return new TestimonialFormView(testimonial, isEdit, TestimonialStatus.values(), message, true);
    }
}
```

### 6.6 TestimonialPageController

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialDTO;
import com.simplecms.adminportal.testimonials.TestimonialService;
import com.simplecms.adminportal.testimonials.TestimonialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/testimonials")
class TestimonialPageController {

    private final TestimonialService testimonialService;

    TestimonialPageController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) TestimonialStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<TestimonialDTO> testimonials = testimonialService.list(status, pageable);
        model.addAttribute("view", TestimonialListView.of(testimonials, status));
        return "testimonials/TestimonialListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", TestimonialFormView.forCreate());
        return "testimonials/TestimonialCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerReview") String customerReview,
            @RequestParam("customerRating") int customerRating,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TestimonialStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            testimonialService.create(customerName, customerReview, customerRating, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial created successfully.");
            return "redirect:/testimonials";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/testimonials/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        TestimonialDTO testimonial = testimonialService.getById(id);
        model.addAttribute("view", TestimonialFormView.forEdit(testimonial));
        return "testimonials/TestimonialEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("customerName") String customerName,
            @RequestParam("customerReview") String customerReview,
            @RequestParam("customerRating") int customerRating,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TestimonialStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            testimonialService.update(id, customerName, customerReview, customerRating, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial updated successfully.");
            return "redirect:/testimonials";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/testimonials/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            testimonialService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/testimonials";
    }
}
```

#### TestimonialFragmentController

```java
package com.simplecms.adminportal.testimonials.internal;

import com.simplecms.adminportal.testimonials.TestimonialService;
import com.simplecms.adminportal.testimonials.TestimonialStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testimonials/fragments")
class TestimonialFragmentController {

    private final TestimonialService testimonialService;

    TestimonialFragmentController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) TestimonialStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("testimonials", testimonialService.list(status, pageable));
        return "testimonials/fragments/TestimonialCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 TestimonialListPage.jte

```html
@param com.simplecms.adminportal.testimonials.internal.TestimonialListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Testimonials</h1>
        <a href="/testimonials/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New</a>
    </div>

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/testimonials/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
        @for(var t : view.testimonials().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4">
                <h3 class="font-semibold text-[#1e1e1e] text-sm mb-1">${t.customerName()}</h3>
                <p class="text-xs text-[#646970] mb-2 line-clamp-3">${t.customerReview()}</p>
                <div class="flex items-center gap-1 mb-2">
                    @for(int i = 1; i <= 5; i++)
                        <span class="text-sm ${i <= t.customerRating() ? "text-[#dba617]" : "text-[#c3c4c7]"}">&#9733;</span>
                    @endfor
                </div>
                <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                    ${t.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                      t.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                      "bg-[#d63638]/10 text-[#d63638]"}">
                    ${t.status().name()}
                </span>
                <div class="mt-3 flex gap-3">
                    <a href="/testimonials/${t.id()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                    <form method="post" action="/testimonials/${t.id()}/delete" class="inline"
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
| TestimonialCreated | New testimonial created | testimonialId | USA000060 |
| TestimonialUpdated | Testimonial updated | testimonialId | USA000060 |
| TestimonialDeleted | Testimonial deleted | testimonialId | USA000066 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Rating validated 1-5 at service and DB level | Per NFRA00075 | NFRA00075 |
| 2 | No image required for testimonials | PRD specifies name, review, rating only | USA000060 |
| 3 | List ordered by displayOrder ASC, createdAt ASC | Per NFRA00078 | NFRA00078 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | TestimonialEntity | Testimonial with name, review, rating, order, status | USA000060, CONSA0021 |
| Enum | TestimonialStatus | DRAFT, INACTIVE, ACTIVE | CONSA0021 |
| Service | TestimonialService | Public API for testimonial CRUD | USA000060-069 |
| Controller | TestimonialPageController | Page controller for list/create/edit/delete | USA000060-069 |
| Controller | TestimonialFragmentController | htmx fragment for card grid filtering | USA000069 |
| Template | TestimonialListPage.jte | 4-column card grid with star ratings | USA000069 |
| Template | TestimonialCreatePage.jte | Create form | USA000060 |
| Template | TestimonialEditPage.jte | Edit form | USA000060 |
