# Module Specification: Team Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000072 | v1.0.0 | As Editor, I want to create/update multiple team members with profile picture, name, role, LinkedIn, and status |
| USA000075 | v1.0.0 | As Editor, I want to set the display order of team members |
| USA000078 | v1.0.0 | As Editor, I want to delete team members that are no longer relevant |
| USA000081 | v1.0.0 | As Editor, I want to view the list of team members in a 4-column card grid with status filter |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00081 | v1.0.0 | Profile picture must be exactly 400x400 square pixels; validated before upload |
| NFRA00084 | v1.0.0 | Name max 100 chars |
| NFRA00087 | v1.0.0 | Role max 100 chars |
| NFRA00090 | v1.0.0 | LinkedIn URL must be valid URL format |
| NFRA00093 | v1.0.0 | List ordered by displayOrder ASC, then createdAt ASC |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0024 | v1.0.0 | Status values: DRAFT (default), INACTIVE, ACTIVE |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.teamsection`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.teamsection
  +-- TeamMemberService.java                 (public API - interface)
  +-- TeamMemberDTO.java                     (public API - DTO)
  +-- TeamMemberStatus.java                  (public API - enum)
  +-- internal
        +-- TeamMemberEntity.java             (JPA entity)
        +-- TeamMemberRepository.java         (Spring Data JPA)
        +-- TeamMemberServiceImpl.java        (service implementation)
        +-- TeamMemberMapper.java             (MapStruct mapper)
        +-- TeamSectionPageController.java    (page controller)
        +-- TeamSectionFragmentController.java (fragment controller - htmx)
        +-- TeamMemberListView.java           (view model)
        +-- TeamMemberFormView.java           (view model)
```

### JTE Templates

```
src/main/jte
  +-- teamsection
        +-- TeamMemberListPage.jte
        +-- TeamMemberCreatePage.jte
        +-- TeamMemberEditPage.jte
        +-- fragments
              +-- TeamMemberCardGrid.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/team-section` | GET | EDITOR, ADMIN |
| `/team-section/create` | GET, POST | EDITOR, ADMIN |
| `/team-section/{id}/edit` | GET, POST | EDITOR, ADMIN |
| `/team-section/{id}/delete` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V7__create_team_member_tables.sql`

```sql
CREATE TABLE tms_team_member (
    id                   UUID            NOT NULL DEFAULT gen_random_uuid(),
    profile_picture_path VARCHAR(500)    NOT NULL,
    name                 VARCHAR(100)    NOT NULL,
    role                 VARCHAR(100)    NOT NULL,
    linkedin_url         VARCHAR(500),
    display_order        INTEGER         NOT NULL DEFAULT 0,
    status               VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    version              BIGINT          NOT NULL DEFAULT 0,
    created_at           TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by           VARCHAR(255)    NOT NULL,
    updated_at           TIMESTAMP,
    updated_by           VARCHAR(255),
    CONSTRAINT pk_tms_team_member PRIMARY KEY (id)
);

CREATE INDEX idx_tms_team_member_status ON tms_team_member(status);
CREATE INDEX idx_tms_team_member_display_order ON tms_team_member(display_order);
```

---

## 5. Public API

### 5.1 TeamMemberStatus Enum

```java
package com.simplecms.adminportal.teamsection;

/**
 * Traces: CONSA0024
 */
public enum TeamMemberStatus {
    DRAFT,
    INACTIVE,
    ACTIVE
}
```

### 5.2 TeamMemberDTO

```java
package com.simplecms.adminportal.teamsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000072
 */
public record TeamMemberDTO(
    UUID id,
    String profilePicturePath,
    String name,
    String role,
    String linkedinUrl,
    int displayOrder,
    TeamMemberStatus status,
    Instant createdAt
) {}
```

### 5.3 TeamMemberService Interface

```java
package com.simplecms.adminportal.teamsection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Public API for the Team Section module.
 *
 * Traces: USA000072-081, NFRA00081-093, CONSA0024
 */
public interface TeamMemberService {

    /**
     * List team members with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000081, NFRA00093
     */
    Page<TeamMemberDTO> list(TeamMemberStatus status, Pageable pageable);

    /**
     * Get a team member by ID.
     */
    TeamMemberDTO getById(UUID id);

    /**
     * Create a new team member with profile picture upload.
     * Validates image dimensions (400x400 square).
     *
     * Traces: USA000072, USA000075, NFRA00081
     *
     * @param name          member name (max 100)
     * @param role          member role (max 100)
     * @param linkedinUrl   LinkedIn URL (nullable)
     * @param displayOrder  display order integer
     * @param status        content status
     * @param profilePicture uploaded profile picture
     * @return created team member DTO
     */
    TeamMemberDTO create(String name, String role, String linkedinUrl,
                         int displayOrder, TeamMemberStatus status, MultipartFile profilePicture);

    /**
     * Update an existing team member. Profile picture is optional on update.
     *
     * Traces: USA000072, USA000075
     */
    TeamMemberDTO update(UUID id, String name, String role, String linkedinUrl,
                         int displayOrder, TeamMemberStatus status, MultipartFile profilePicture);

    /**
     * Delete a team member by ID.
     *
     * Traces: USA000078
     */
    void delete(UUID id);
}
```

---

## 6. Internal Components

### 6.1 TeamMemberEntity

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for team members.
 * Table: tms_team_member
 *
 * Traces: USA000072, CONSA0024
 */
@Entity
@Table(name = "tms_team_member")
@Getter
@Setter
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "profile_picture_path", nullable = false, length = 500)
    private String profilePicturePath;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TeamMemberStatus status = TeamMemberStatus.DRAFT;

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

    protected TeamMemberEntity() {
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

### 6.2 TeamMemberRepository

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, UUID> {

    @Query("SELECT t FROM TeamMemberEntity t WHERE " +
           "(:status IS NULL OR t.status = :status) " +
           "ORDER BY t.displayOrder ASC, t.createdAt ASC")
    Page<TeamMemberEntity> findWithFilters(
        @Param("status") TeamMemberStatus status,
        Pageable pageable);
}
```

### 6.3 TeamMemberMapper (MapStruct)

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface TeamMemberMapper {

    TeamMemberDTO toDTO(TeamMemberEntity entity);

    List<TeamMemberDTO> toDTOList(List<TeamMemberEntity> entities);
}
```

### 6.4 TeamMemberServiceImpl

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
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
 * Traces: USA000072-081, NFRA00081-093, CONSA0024
 */
@Service
@Transactional
class TeamMemberServiceImpl implements TeamMemberService {

    private static final Logger log = LoggerFactory.getLogger(TeamMemberServiceImpl.class);
    private static final int IMAGE_SIZE = 400;

    private final TeamMemberRepository repository;
    private final TeamMemberMapper mapper;

    @Value("${app.upload.path:/uploads}")
    private String uploadPath;

    TeamMemberServiceImpl(TeamMemberRepository repository, TeamMemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamMemberDTO> list(TeamMemberStatus status, Pageable pageable) {
        return repository.findWithFilters(status, pageable).map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMemberDTO getById(UUID id) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    public TeamMemberDTO create(String name, String role, String linkedinUrl,
                                int displayOrder, TeamMemberStatus status,
                                MultipartFile profilePicture) {
        validateImage(profilePicture);
        String picturePath = saveImage(profilePicture);

        TeamMemberEntity entity = new TeamMemberEntity();
        entity.setProfilePicturePath(picturePath);
        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setCreatedBy("EDITOR");

        TeamMemberEntity saved = repository.save(entity);
        log.info("Team member created: {}", saved.getId());
        return mapper.toDTO(saved);
    }

    @Override
    public TeamMemberDTO update(UUID id, String name, String role, String linkedinUrl,
                                int displayOrder, TeamMemberStatus status,
                                MultipartFile profilePicture) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));

        if (profilePicture != null && !profilePicture.isEmpty()) {
            validateImage(profilePicture);
            String picturePath = saveImage(profilePicture);
            entity.setProfilePicturePath(picturePath);
        }

        entity.setName(name);
        entity.setRole(role);
        entity.setLinkedinUrl(linkedinUrl);
        entity.setDisplayOrder(displayOrder);
        entity.setStatus(status);
        entity.setUpdatedBy("EDITOR");

        TeamMemberEntity saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void delete(UUID id) {
        TeamMemberEntity entity = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Team member not found: " + id));
        repository.delete(entity);
        log.info("Team member deleted: {}", id);
    }

    /**
     * Validate image dimensions are exactly 400x400 square.
     *
     * Traces: NFRA00081
     */
    private void validateImage(MultipartFile image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image file");
            }
            if (bufferedImage.getWidth() != IMAGE_SIZE || bufferedImage.getHeight() != IMAGE_SIZE) {
                throw new IllegalArgumentException(
                    String.format("Profile picture must be %dx%d pixels. Got %dx%d.",
                        IMAGE_SIZE, IMAGE_SIZE,
                        bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read image file", e);
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String filename = "team-" + UUID.randomUUID() + getExtension(image.getOriginalFilename());
            Path dir = Paths.get(uploadPath, "team-section");
            Files.createDirectories(dir);
            Path filePath = dir.resolve(filename);
            image.transferTo(filePath.toFile());
            return "/uploads/team-section/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
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

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;

public record TeamMemberListView(
    Page<TeamMemberDTO> teamMembers,
    TeamMemberStatus filterStatus,
    TeamMemberStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static TeamMemberListView of(Page<TeamMemberDTO> teamMembers, TeamMemberStatus filterStatus) {
        return new TeamMemberListView(teamMembers, filterStatus, TeamMemberStatus.values(), null, false);
    }
}
```

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;

public record TeamMemberFormView(
    TeamMemberDTO teamMember,
    boolean isEdit,
    TeamMemberStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static TeamMemberFormView forCreate() {
        return new TeamMemberFormView(null, false, TeamMemberStatus.values(), null, false);
    }

    public static TeamMemberFormView forEdit(TeamMemberDTO teamMember) {
        return new TeamMemberFormView(teamMember, true, TeamMemberStatus.values(), null, false);
    }

    public static TeamMemberFormView withError(TeamMemberDTO teamMember, boolean isEdit, String message) {
        return new TeamMemberFormView(teamMember, isEdit, TeamMemberStatus.values(), message, true);
    }
}
```

### 6.6 TeamSectionPageController

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/team-section")
class TeamSectionPageController {

    private final TeamMemberService teamMemberService;

    TeamSectionPageController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) TeamMemberStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<TeamMemberDTO> members = teamMemberService.list(status, pageable);
        model.addAttribute("view", TeamMemberListView.of(members, status));
        return "teamsection/TeamMemberListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", TeamMemberFormView.forCreate());
        return "teamsection/TeamMemberCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("name") String name,
            @RequestParam("role") String role,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TeamMemberStatus status,
            @RequestParam("profilePicture") MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.create(name, role, linkedinUrl, displayOrder, status, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Team member created successfully.");
            return "redirect:/team-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/team-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        TeamMemberDTO member = teamMemberService.getById(id);
        model.addAttribute("view", TeamMemberFormView.forEdit(member));
        return "teamsection/TeamMemberEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("name") String name,
            @RequestParam("role") String role,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TeamMemberStatus status,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.update(id, name, role, linkedinUrl, displayOrder, status, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Team member updated successfully.");
            return "redirect:/team-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/team-section/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Team member deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/team-section";
    }
}
```

#### TeamSectionFragmentController

```java
package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberService;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/team-section/fragments")
class TeamSectionFragmentController {

    private final TeamMemberService teamMemberService;

    TeamSectionFragmentController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) TeamMemberStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("teamMembers", teamMemberService.list(status, pageable));
        return "teamsection/fragments/TeamMemberCardGrid";
    }
}
```

---

## 7. JTE Templates

### 7.1 TeamMemberListPage.jte

```html
@param com.simplecms.adminportal.teamsection.internal.TeamMemberListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Team Members</h1>
        <a href="/team-section/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Add New</a>
    </div>

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-4 mb-6">
        <form hx-get="/team-section/fragments/card-grid" hx-target="#card-grid" hx-swap="innerHTML"
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
        @for(var m : view.teamMembers().getContent())
            <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
                <img src="${m.profilePicturePath()}" alt="${m.name()}" class="w-full h-[200px] object-cover">
                <div class="p-4">
                    <h3 class="font-semibold text-[#1e1e1e] text-sm">${m.name()}</h3>
                    <p class="text-xs text-[#646970] mb-1">${m.role()}</p>
                    @if(m.linkedinUrl() != null)
                        <a href="${m.linkedinUrl()}" target="_blank" class="text-xs text-[#2271b1] hover:underline">LinkedIn</a>
                    @endif
                    <div class="mt-2">
                        <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                            ${m.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" :
                              m.status().name().equals("DRAFT") ? "bg-[#dba617]/10 text-[#dba617]" :
                              "bg-[#d63638]/10 text-[#d63638]"}">
                            ${m.status().name()}
                        </span>
                    </div>
                    <div class="mt-3 flex gap-3">
                        <a href="/team-section/${m.id()}/edit" class="text-sm text-[#2271b1] hover:underline">Edit</a>
                        <form method="post" action="/team-section/${m.id()}/delete" class="inline"
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
| TeamMemberCreated | New team member created | memberId | USA000072 |
| TeamMemberUpdated | Team member updated | memberId | USA000072 |
| TeamMemberDeleted | Team member deleted | memberId | USA000078 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Profile picture validated at exact 400x400 square | Per NFRA00081 | NFRA00081 |
| 2 | Image processed via Scalr for consistent sizing | Per PRD coding standards | NFRA00081 |
| 3 | List ordered by displayOrder ASC, createdAt ASC | Per NFRA00093 | NFRA00093 |
| 4 | LinkedIn URL is optional | Not all team members may have LinkedIn | USA000072 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | TeamMemberEntity | Team member with picture, name, role, LinkedIn, order, status | USA000072, CONSA0024 |
| Enum | TeamMemberStatus | DRAFT, INACTIVE, ACTIVE | CONSA0024 |
| Service | TeamMemberService | Public API for team member CRUD | USA000072-081 |
| Controller | TeamSectionPageController | Page controller for list/create/edit/delete | USA000072-081 |
| Controller | TeamSectionFragmentController | htmx fragment for card grid filtering | USA000081 |
| Template | TeamMemberListPage.jte | 4-column card grid with profile pictures | USA000081 |
| Template | TeamMemberCreatePage.jte | Create form with image upload | USA000072 |
| Template | TeamMemberEditPage.jte | Edit form with optional image | USA000072 |
