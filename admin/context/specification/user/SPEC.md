# Module Specification: User

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000012 | v1.0.0 | As User, I want to view my Profile so I can verify my profile information is correct |
| USA000015 | v1.0.0 | As User, I want to view my Account information so I can perform change password procedure |
| USA000018 | v1.0.0 | As Admin, I want to view the list of users so I can manage users in the system |
| USA000021 | v1.0.0 | As Admin, I want to create a new user so I can add new users to the system |
| USA000024 | v1.0.0 | As Admin, I want to edit a user so I can update user information |
| USA000027 | v1.0.0 | As Admin, I want to delete a user so I can remove users that are no longer needed |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00006 | v1.0.0 | New user information: email, password/confirm, first name, last name, role, status |
| NFRA00009 | v1.0.0 | New user role defaults to USER and EDITOR; only ADMIN can upgrade to ADMIN |
| NFRA00012 | v1.0.0 | New user password defaults to "password"; user prompted to change on first login |
| NFRA00015 | v1.0.0 | On startup, seed admin user if no users exist (admin/password, ADMIN role) |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0003 | v1.0.0 | Only first name and last name can be changed on User Profile |
| CONSA0006 | v1.0.0 | User status: ACTIVE (default), INACTIVE (cannot log in) |
| CONSA0009 | v1.0.0 | Only ADMIN role can access user management and perform CRUD on users |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.user`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.user
  +-- UserService.java                  (public API - interface)
  +-- UserDTO.java                      (public API - DTO)
  +-- UserRole.java                     (public API - enum)
  +-- UserStatus.java                   (public API - enum)
  +-- UserCreatedEvent.java             (public API - event)
  +-- UserUpdatedEvent.java             (public API - event)
  +-- UserDeletedEvent.java             (public API - event)
  +-- internal
        +-- UserEntity.java             (JPA entity)
        +-- UserRepository.java         (Spring Data JPA)
        +-- UserServiceImpl.java        (service implementation)
        +-- UserMapper.java             (MapStruct mapper)
        +-- UserDataSeeder.java         (startup data seeder)
        +-- ProfilePageController.java  (page controller)
        +-- AccountPageController.java  (page controller)
        +-- UserPageController.java     (page controller - ADMIN)
        +-- ProfileView.java            (view model)
        +-- AccountView.java            (view model)
        +-- UserListView.java           (view model)
        +-- UserFormView.java           (view model)
```

### JTE Templates

```
src/main/jte
  +-- user
        +-- ProfilePage.jte
        +-- AccountPage.jte
        +-- UserListPage.jte
        +-- UserCreatePage.jte
        +-- UserEditPage.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/profile` | GET, POST | authenticated (any role) |
| `/account` | GET, POST | authenticated (any role) |
| `/users` | GET | ADMIN only |
| `/users/create` | GET, POST | ADMIN only |
| `/users/{id}/edit` | GET, POST | ADMIN only |
| `/users/{id}/delete` | POST | ADMIN only |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V2__create_user_tables.sql`

```sql
CREATE TABLE usr_user (
    id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
    email                   VARCHAR(255)    NOT NULL,
    password                VARCHAR(255)    NOT NULL,
    first_name              VARCHAR(100)    NOT NULL,
    last_name               VARCHAR(100)    NOT NULL,
    role                    VARCHAR(20)     NOT NULL DEFAULT 'USER',
    status                  VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    force_password_change   BOOLEAN         NOT NULL DEFAULT FALSE,
    last_login_at           TIMESTAMP,
    version                 BIGINT          NOT NULL DEFAULT 0,
    created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(255)    NOT NULL,
    updated_at              TIMESTAMP,
    updated_by              VARCHAR(255),
    CONSTRAINT pk_usr_user PRIMARY KEY (id),
    CONSTRAINT uq_usr_user_email UNIQUE (email)
);

CREATE INDEX idx_usr_user_email ON usr_user(email);
CREATE INDEX idx_usr_user_role ON usr_user(role);
CREATE INDEX idx_usr_user_status ON usr_user(status);
```

---

## 5. Public API

### 5.1 UserRole Enum

```java
package com.simplecms.adminportal.user;

/**
 * Roles available in the system.
 *
 * Traces: NFRA00006, NFRA00009
 */
public enum UserRole {
    USER,
    EDITOR,
    ADMIN
}
```

### 5.2 UserStatus Enum

```java
package com.simplecms.adminportal.user;

/**
 * Status values for a user account.
 *
 * Traces: CONSA0006
 */
public enum UserStatus {
    ACTIVE,
    INACTIVE
}
```

### 5.3 UserDTO

```java
package com.simplecms.adminportal.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for user information.
 *
 * Traces: NFRA00006
 */
public record UserDTO(
    UUID id,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    UserStatus status,
    boolean forcePasswordChange,
    Instant lastLoginAt,
    Instant createdAt
) {}
```

### 5.4 UserService Interface

```java
package com.simplecms.adminportal.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Public API for the User module.
 * Provides user management, profile, and account operations.
 *
 * Traces: USA000012-027, NFRA00006-015, CONSA0003, CONSA0006, CONSA0009
 */
public interface UserService {

    /**
     * Get the profile of a user by ID.
     *
     * Traces: USA000012
     *
     * @param userId the user's ID
     * @return the user profile DTO
     */
    UserDTO getProfile(UUID userId);

    /**
     * Update the profile of the currently authenticated user.
     * Only firstName and lastName are editable per CONSA0003.
     *
     * Traces: USA000012, CONSA0003
     *
     * @param userId    the authenticated user's ID
     * @param firstName new first name
     * @param lastName  new last name
     * @return updated user DTO
     */
    UserDTO updateProfile(UUID userId, String firstName, String lastName);

    /**
     * Change the password for a user.
     *
     * Traces: USA000015
     *
     * @param userId      the user's ID
     * @param newPassword the new encoded password
     */
    void changePassword(UUID userId, String newPassword);

    /**
     * List all users with pagination.
     *
     * Traces: USA000018, CONSA0009
     *
     * @param pageable pagination parameters
     * @return page of user DTOs
     */
    Page<UserDTO> listUsers(Pageable pageable);

    /**
     * List all users (no pagination).
     *
     * Traces: USA000018
     *
     * @return list of all user DTOs
     */
    List<UserDTO> listUsers();

    /**
     * Create a new user with default password "password" and forcePasswordChange=true.
     *
     * Traces: USA000021, NFRA00006, NFRA00009, NFRA00012
     *
     * @param email     the user's email (unique)
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param role      the user's role
     * @param status    the user's status
     * @return created user DTO
     */
    UserDTO createUser(String email, String firstName, String lastName, UserRole role, UserStatus status);

    /**
     * Update an existing user.
     *
     * Traces: USA000024
     *
     * @param userId    the user's ID
     * @param email     the user's email
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param role      the user's role
     * @param status    the user's status
     * @return updated user DTO
     */
    UserDTO updateUser(UUID userId, String email, String firstName, String lastName, UserRole role, UserStatus status);

    /**
     * Delete a user by ID.
     *
     * Traces: USA000027
     *
     * @param userId the user's ID
     */
    void deleteUser(UUID userId);

    /**
     * Find a user by email address.
     *
     * @param email the email address
     * @return optional user DTO
     */
    Optional<UserDTO> findByEmail(String email);

    /**
     * List all users with EDITOR role (for blog author selection).
     *
     * Traces: CONSA0039 (cross-module: Blog)
     *
     * @return list of editor user DTOs
     */
    List<UserDTO> listEditors();
}
```

### 5.5 Domain Events

```java
package com.simplecms.adminportal.user;

import java.util.UUID;

public record UserCreatedEvent(UUID userId, String email, UserRole role) {}
public record UserUpdatedEvent(UUID userId, String email) {}
public record UserDeletedEvent(UUID userId, String email) {}
```

---

## 6. Internal Components

### 6.1 UserEntity

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for users.
 * Table: usr_user
 *
 * Traces: NFRA00006, CONSA0006
 */
@Entity
@Table(name = "usr_user")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "force_password_change", nullable = false)
    private boolean forcePasswordChange = false;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

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

    protected UserEntity() {
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

### 6.2 UserRepository

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for UserEntity.
 *
 * Traces: USA000018, USA000021, USA000024, USA000027
 */
@Repository
interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserEntity> findByRole(UserRole role);

    long count();
}
```

### 6.3 UserMapper (MapStruct)

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * MapStruct mapper for converting between UserEntity and UserDTO.
 *
 * Traces: NFRA00006
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper {

    UserDTO toDTO(UserEntity entity);

    List<UserDTO> toDTOList(List<UserEntity> entities);
}
```

### 6.4 UserServiceImpl

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService.
 *
 * Traces: USA000012-027, NFRA00006-015, CONSA0003, CONSA0006, CONSA0009
 */
@Service
@Transactional
class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    UserServiceImpl(UserRepository userRepository,
                    UserMapper userMapper,
                    PasswordEncoder passwordEncoder,
                    ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getProfile(UUID userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return userMapper.toDTO(entity);
    }

    /**
     * Only firstName and lastName are editable on profile.
     *
     * Traces: USA000012, CONSA0003
     */
    @Override
    public UserDTO updateProfile(UUID userId, String firstName, String lastName) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setUpdatedBy(entity.getEmail());

        UserEntity saved = userRepository.save(entity);
        return userMapper.toDTO(saved);
    }

    @Override
    public void changePassword(UUID userId, String newPassword) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        entity.setPassword(newPassword);
        entity.setForcePasswordChange(false);
        entity.setUpdatedBy(entity.getEmail());

        userRepository.save(entity);
        log.info("Password changed for user: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listUsers() {
        return userMapper.toDTOList(userRepository.findAll());
    }

    /**
     * Create a new user with default password "password" and forcePasswordChange=true.
     *
     * Traces: USA000021, NFRA00006, NFRA00009, NFRA00012
     */
    @Override
    public UserDTO createUser(String email, String firstName, String lastName,
                              UserRole role, UserStatus status) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setRole(role);
        entity.setStatus(status);
        entity.setForcePasswordChange(true);
        entity.setCreatedBy("ADMIN");

        UserEntity saved = userRepository.save(entity);
        eventPublisher.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail(), saved.getRole()));

        log.info("User created: {} with role {}", saved.getEmail(), saved.getRole());
        return userMapper.toDTO(saved);
    }

    @Override
    public UserDTO updateUser(UUID userId, String email, String firstName, String lastName,
                              UserRole role, UserStatus status) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!entity.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        entity.setEmail(email);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setRole(role);
        entity.setStatus(status);
        entity.setUpdatedBy("ADMIN");

        UserEntity saved = userRepository.save(entity);
        eventPublisher.publishEvent(new UserUpdatedEvent(saved.getId(), saved.getEmail()));

        return userMapper.toDTO(saved);
    }

    @Override
    public void deleteUser(UUID userId) {
        UserEntity entity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        userRepository.delete(entity);
        eventPublisher.publishEvent(new UserDeletedEvent(entity.getId(), entity.getEmail()));

        log.info("User deleted: {}", entity.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDTO);
    }

    /**
     * List users with EDITOR role for blog author selection.
     *
     * Traces: CONSA0039 (cross-module)
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> listEditors() {
        return userMapper.toDTOList(userRepository.findByRole(UserRole.EDITOR));
    }
}
```

### 6.5 UserDataSeeder

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds the admin user on startup if no users exist in the database.
 *
 * Traces: NFRA00015
 */
@Component
class UserDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UserDataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setEmail("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setForcePasswordChange(true);
            admin.setCreatedBy("SYSTEM");

            userRepository.save(admin);
            log.info("Admin user seeded: admin / password (change on first login)");
        }
    }
}
```

### 6.6 View Models

#### ProfileView

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;

/**
 * View model for the profile page.
 *
 * Traces: USA000012, CONSA0003
 */
public record ProfileView(
    UserDTO user,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ProfileView of(UserDTO user) {
        return new ProfileView(user, null, false, null, false);
    }

    public static ProfileView withSuccess(UserDTO user, String message) {
        return new ProfileView(user, message, true, null, false);
    }

    public static ProfileView withError(UserDTO user, String message) {
        return new ProfileView(user, null, false, message, true);
    }
}
```

#### AccountView

```java
package com.simplecms.adminportal.user.internal;

/**
 * View model for the account / change password page.
 *
 * Traces: USA000015
 */
public record AccountView(
    String email,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static AccountView of(String email) {
        return new AccountView(email, null, false, null, false);
    }

    public static AccountView withSuccess(String email, String message) {
        return new AccountView(email, message, true, null, false);
    }

    public static AccountView withError(String email, String message) {
        return new AccountView(email, null, false, message, true);
    }
}
```

#### UserListView

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import org.springframework.data.domain.Page;

/**
 * View model for the user list page.
 *
 * Traces: USA000018
 */
public record UserListView(
    Page<UserDTO> users,
    String successMessage,
    boolean hasSuccess
) {
    public static UserListView of(Page<UserDTO> users) {
        return new UserListView(users, null, false);
    }

    public static UserListView withSuccess(Page<UserDTO> users, String message) {
        return new UserListView(users, message, true);
    }
}
```

#### UserFormView

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;

/**
 * View model for user create/edit forms.
 *
 * Traces: USA000021, USA000024
 */
public record UserFormView(
    UserDTO user,
    boolean isEdit,
    UserRole[] roles,
    UserStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static UserFormView forCreate() {
        return new UserFormView(null, false, UserRole.values(), UserStatus.values(), null, false);
    }

    public static UserFormView forEdit(UserDTO user) {
        return new UserFormView(user, true, UserRole.values(), UserStatus.values(), null, false);
    }

    public static UserFormView withError(UserDTO user, boolean isEdit, String message) {
        return new UserFormView(user, isEdit, UserRole.values(), UserStatus.values(), message, true);
    }
}
```

### 6.7 Page Controllers

#### ProfilePageController

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for profile pages.
 *
 * Traces: USA000012, CONSA0003
 */
@Controller
class ProfilePageController {

    private final UserService userService;

    ProfilePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        model.addAttribute("view", ProfileView.of(user));
        return "user/ProfilePage";
    }

    @PostMapping("/profile")
    String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            Model model) {

        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        try {
            UserDTO updated = userService.updateProfile(user.id(), firstName, lastName);
            model.addAttribute("view", ProfileView.withSuccess(updated, "Profile updated successfully."));
        } catch (Exception e) {
            model.addAttribute("view", ProfileView.withError(user, e.getMessage()));
        }

        return "user/ProfilePage";
    }
}
```

#### AccountPageController

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the account / change password page.
 *
 * Traces: USA000015
 */
@Controller
class AccountPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    AccountPageController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account")
    String showAccount(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("view", AccountView.of(userDetails.getUsername()));
        return "user/AccountPage";
    }

    @PostMapping("/account")
    String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("view", AccountView.withError(
                userDetails.getUsername(), "Passwords do not match."));
            return "user/AccountPage";
        }

        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        userService.changePassword(user.id(), passwordEncoder.encode(newPassword));

        model.addAttribute("view", AccountView.withSuccess(
            userDetails.getUsername(), "Password changed successfully."));
        return "user/AccountPage";
    }
}
```

#### UserPageController

```java
package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserService;
import com.simplecms.adminportal.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Controller for user management pages (ADMIN only).
 *
 * Traces: USA000018-027, CONSA0009
 */
@Controller
@RequestMapping("/users")
class UserPageController {

    private final UserService userService;

    UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String listUsers(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<UserDTO> users = userService.listUsers(pageable);
        model.addAttribute("view", UserListView.of(users));
        return "user/UserListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", UserFormView.forCreate());
        return "user/UserCreatePage";
    }

    @PostMapping("/create")
    String createUser(
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") UserRole role,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(email, firstName, lastName, role, status);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        UserDTO user = userService.getProfile(id);
        model.addAttribute("view", UserFormView.forEdit(user));
        return "user/UserEditPage";
    }

    @PostMapping("/{id}/edit")
    String updateUser(
            @PathVariable("id") UUID id,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") UserRole role,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateUser(id, email, firstName, lastName, role, status);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
```

---

## 7. JTE Templates

### 7.1 ProfilePage.jte

```html
@param com.simplecms.adminportal.user.internal.ProfileView view

@template.layout.MainLayout(
    content = @`
    <div class="max-w-2xl mx-auto">
        <h1 class="text-xl font-bold text-[#1e1e1e] mb-6">My Profile</h1>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasSuccess())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                    ${view.successMessage()}
                </div>
            @endif
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            <form method="post" action="/profile" class="space-y-4">
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Email</label>
                    <input type="text" value="${view.user().email()}" disabled
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] bg-gray-50 text-[#646970]">
                </div>
                <div>
                    <label for="firstName" class="block text-sm font-medium text-[#1e1e1e] mb-1">First Name</label>
                    <input type="text" id="firstName" name="firstName" required
                           value="${view.user().firstName()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="lastName" class="block text-sm font-medium text-[#1e1e1e] mb-1">Last Name</label>
                    <input type="text" id="lastName" name="lastName" required
                           value="${view.user().lastName()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label class="block text-sm font-medium text-[#1e1e1e] mb-1">Role</label>
                    <input type="text" value="${view.user().role().name()}" disabled
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] bg-gray-50 text-[#646970]">
                </div>
                <button type="submit"
                        class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                    Update Profile
                </button>
            </form>
        </div>
    </div>
    `
)
```

### 7.2 UserListPage.jte

```html
@param com.simplecms.adminportal.user.internal.UserListView view

@template.layout.MainLayout(
    content = @`
    <div class="flex items-center justify-between mb-6">
        <h1 class="text-xl font-bold text-[#1e1e1e]">Users</h1>
        <a href="/users/create"
           class="px-4 py-2 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
            Add New User
        </a>
    </div>

    @if(view.hasSuccess())
        <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
            ${view.successMessage()}
        </div>
    @endif

    <div class="bg-white border border-[#c3c4c7] rounded-[8px] overflow-hidden">
        <table class="w-full text-sm">
            <thead class="bg-gray-50 border-b border-[#c3c4c7]">
                <tr>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Email</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Name</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Role</th>
                    <th class="px-4 py-3 text-left font-medium text-[#1e1e1e]">Status</th>
                    <th class="px-4 py-3 text-right font-medium text-[#1e1e1e]">Actions</th>
                </tr>
            </thead>
            <tbody>
                @for(var user : view.users().getContent())
                    <tr class="border-b border-[#c3c4c7] last:border-b-0">
                        <td class="px-4 py-3 text-[#1e1e1e]">${user.email()}</td>
                        <td class="px-4 py-3 text-[#1e1e1e]">${user.firstName()} ${user.lastName()}</td>
                        <td class="px-4 py-3">${user.role().name()}</td>
                        <td class="px-4 py-3">
                            <span class="inline-block px-2 py-0.5 text-xs font-medium rounded-full
                                ${user.status().name().equals("ACTIVE") ? "bg-[#00a32a]/10 text-[#00a32a]" : "bg-[#d63638]/10 text-[#d63638]"}">
                                ${user.status().name()}
                            </span>
                        </td>
                        <td class="px-4 py-3 text-right space-x-2">
                            <a href="/users/${user.id()}/edit" class="text-[#2271b1] hover:text-[#135e96] hover:underline">Edit</a>
                            <form method="post" action="/users/${user.id()}/delete" class="inline"
                                  x-data x-on:submit.prevent="if(confirm('Are you sure you want to delete this user?')) $el.submit()">
                                <button type="submit" class="text-[#d63638] hover:underline">Delete</button>
                            </form>
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>
    `
)
```

---

## 8. Domain Events

| Event | Trigger | Payload | Traces |
|-------|---------|---------|--------|
| UserCreatedEvent | New user created by admin | userId, email, role | USA000021 |
| UserUpdatedEvent | User updated by admin | userId, email | USA000024 |
| UserDeletedEvent | User deleted by admin | userId, email | USA000027 |

---

## 9. Cross-Module Dependencies

| Dependency | Direction | Purpose | Traces |
|------------|-----------|---------|--------|
| PasswordEncoder | Shared (Spring Security) | BCrypt encoding for passwords | NFRA00012 |
| AuthService | Auth -> User | Calls findByEmail, changePassword for password reset | USA000006 |
| BlogService | Blog -> User | Calls listEditors for author dropdown | CONSA0039 |

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Default password is "password" | Simple default per NFRA00012; forcePasswordChange ensures user changes it | NFRA00012 |
| 2 | Admin seeded on empty DB | Ensures first-time access is possible | NFRA00015 |
| 3 | Profile only edits firstName/lastName | Per CONSA0003 constraint | CONSA0003 |
| 4 | INACTIVE users cannot log in | Spring Security UserDetailsService checks status | CONSA0006 |
| 5 | User management restricted to ADMIN | Security config enforces /users/** to ADMIN role | CONSA0009 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | UserEntity | Stores user accounts with role and status | NFRA00006, CONSA0006 |
| Enum | UserRole | USER, EDITOR, ADMIN | NFRA00009 |
| Enum | UserStatus | ACTIVE, INACTIVE | CONSA0006 |
| Service | UserService | Public API for user management | USA000012-027 |
| Seeder | UserDataSeeder | Seeds admin user on startup | NFRA00015 |
| Controller | ProfilePageController | Profile view and edit | USA000012, CONSA0003 |
| Controller | AccountPageController | Change password | USA000015 |
| Controller | UserPageController | User CRUD (ADMIN) | USA000018-027, CONSA0009 |
| Template | ProfilePage.jte | Profile form | USA000012 |
| Template | AccountPage.jte | Change password form | USA000015 |
| Template | UserListPage.jte | User list table | USA000018 |
| Template | UserCreatePage.jte | Create user form | USA000021 |
| Template | UserEditPage.jte | Edit user form | USA000024 |
