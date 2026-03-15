# Module Specification: Authentication and Authorization

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000003 | v1.0.0 | As User, I want to log in using my email and password so I can access the admin portal |
| USA000006 | v1.0.0 | As User, I want to reset my password via a secure email link so I can regain access |
| USA000009 | v1.0.0 | As User, I want to log out so I can ensure no unauthorized access using my credential |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00003 | v1.0.0 | Forgot password function will use typical and secure forgot password flow |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| (none) | - | - |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.authentication`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Auth:** Spring Security form-based login
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.authentication
  +-- AuthService.java                          (public API - interface)
  +-- PasswordResetTokenDTO.java                (public API - DTO)
  +-- internal
        +-- PasswordResetTokenEntity.java       (JPA entity)
        +-- PasswordResetTokenRepository.java   (Spring Data JPA)
        +-- AuthServiceImpl.java                (service implementation)
        +-- AuthMapper.java                     (MapStruct mapper)
        +-- LoginPageController.java            (page controller)
        +-- ForgotPasswordPageController.java   (page controller)
        +-- ResetPasswordPageController.java    (page controller)
        +-- LoginView.java                      (view model)
        +-- ForgotPasswordView.java             (view model)
        +-- ResetPasswordView.java              (view model)
```

### JTE Templates

```
src/main/jte
  +-- authentication
        +-- LoginPage.jte
        +-- ForgotPasswordPage.jte
        +-- ResetPasswordPage.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/login` | GET, POST | public (permitAll) |
| `/forgot-password` | GET, POST | public (permitAll) |
| `/reset-password` | GET, POST | public (permitAll) |
| `/logout` | POST | authenticated |

- After successful login, redirect to `/` (home).
- Login, forgot-password, and reset-password pages use `AuthLayout` (no sidebar/header).
- Spring Security form login with `loginPage("/login")` and `loginProcessingUrl("/login")`.
- Username parameter: `email`, password parameter: `password`.

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V1__create_authentication_tables.sql`

```sql
CREATE TABLE aaa_password_reset_token (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    user_id         UUID            NOT NULL,
    token           VARCHAR(255)    NOT NULL,
    expires_at      TIMESTAMP       NOT NULL,
    used            BOOLEAN         NOT NULL DEFAULT FALSE,
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_aaa_password_reset_token PRIMARY KEY (id),
    CONSTRAINT uq_aaa_password_reset_token_token UNIQUE (token),
    CONSTRAINT fk_aaa_password_reset_token_user FOREIGN KEY (user_id) REFERENCES usr_user(id) ON DELETE CASCADE
);

CREATE INDEX idx_aaa_password_reset_token_user_id ON aaa_password_reset_token(user_id);
CREATE INDEX idx_aaa_password_reset_token_token ON aaa_password_reset_token(token);
```

---

## 5. Public API

### 5.1 AuthService Interface

```java
package com.simplecms.adminportal.authentication;

import java.util.UUID;

/**
 * Public API for the Authentication and Authorization module.
 * Provides authentication flows: login, logout, and password reset.
 *
 * Traces: USA000003, USA000006, USA000009, NFRA00003
 */
public interface AuthService {

    /**
     * Initiate a password reset flow by generating a token and sending
     * a reset link to the user's email address.
     * If the email does not exist, no error is raised (security best practice).
     *
     * Traces: USA000006, NFRA00003
     *
     * @param email the user's email address
     */
    void requestPasswordReset(String email);

    /**
     * Complete the password reset flow by validating the token and
     * updating the user's password.
     *
     * Traces: USA000006, NFRA00003
     *
     * @param token       the password reset token from the email link
     * @param newPassword the new plain-text password
     * @throws IllegalArgumentException if token is invalid, expired, or already used
     */
    void resetPassword(String token, String newPassword);
}
```

### 5.2 PasswordResetTokenDTO

```java
package com.simplecms.adminportal.authentication;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for password reset token information.
 *
 * Traces: USA000006
 */
public record PasswordResetTokenDTO(
    UUID id,
    UUID userId,
    String token,
    Instant expiresAt,
    boolean used,
    Instant createdAt
) {}
```

---

## 6. Internal Components

### 6.1 PasswordResetTokenEntity

```java
package com.simplecms.adminportal.authentication.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for password reset tokens.
 * Table: aaa_password_reset_token
 *
 * Traces: USA000006, NFRA00003
 */
@Entity
@Table(name = "aaa_password_reset_token")
@Getter
@Setter
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "used", nullable = false)
    private boolean used = false;

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

    protected PasswordResetTokenEntity() {
        // JPA
    }

    public PasswordResetTokenEntity(UUID userId, String token, Instant expiresAt, String createdBy) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.used = false;
        this.createdAt = Instant.now();
        this.createdBy = createdBy;
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

    // --- Domain Logic ---

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !this.used && !isExpired();
    }

    public void markAsUsed(String updatedBy) {
        this.used = true;
        this.updatedBy = updatedBy;
    }
}
```

### 6.2 PasswordResetTokenRepository

```java
package com.simplecms.adminportal.authentication.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for PasswordResetTokenEntity.
 *
 * Traces: USA000006, NFRA00003
 */
@Repository
interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    Optional<PasswordResetTokenEntity> findByTokenAndUsedFalse(String token);

    void deleteByUserId(UUID userId);
}
```

### 6.3 AuthMapper (MapStruct)

```java
package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.PasswordResetTokenDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * MapStruct mapper for converting between PasswordResetTokenEntity and DTO.
 *
 * Traces: USA000006
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface AuthMapper {

    PasswordResetTokenDTO toDTO(PasswordResetTokenEntity entity);
}
```

### 6.4 AuthServiceImpl

```java
package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import com.simplecms.adminportal.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Implementation of AuthService.
 * Handles password reset token generation, validation, and password update.
 * Login/logout are handled by Spring Security configuration.
 *
 * Traces: USA000003, USA000006, USA000009, NFRA00003
 */
@Service
@Transactional
class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final int TOKEN_EXPIRY_HOURS = 24;

    private final PasswordResetTokenRepository tokenRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    AuthServiceImpl(PasswordResetTokenRepository tokenRepository,
                    UserService userService,
                    PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generate a secure password reset token and send an email with the reset link.
     * If the email does not correspond to an active user, silently succeed
     * to prevent user enumeration attacks.
     *
     * Traces: USA000006, NFRA00003
     */
    @Override
    public void requestPasswordReset(String email) {
        log.info("Password reset requested for email: {}", email);

        UserDTO user;
        try {
            user = userService.findByEmail(email).orElse(null);
        } catch (Exception e) {
            log.warn("Error looking up user for password reset: {}", e.getMessage());
            return;
        }

        if (user == null || user.status() != UserStatus.ACTIVE) {
            log.info("No active user found for email: {}. Silently ignoring.", email);
            return;
        }

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(TOKEN_EXPIRY_HOURS, ChronoUnit.HOURS);

        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity(
            user.id(), token, expiresAt, "SYSTEM"
        );

        tokenRepository.save(resetToken);

        // TODO: Send email with reset link containing the token
        log.info("Password reset token generated for user: {}", user.id());
    }

    /**
     * Validate the reset token and update the user's password.
     * Marks the token as used after successful password reset.
     *
     * Traces: USA000006, NFRA00003
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        log.info("Password reset attempt with token");

        PasswordResetTokenEntity resetToken = tokenRepository.findByTokenAndUsedFalse(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Password reset token has expired");
        }

        if (!resetToken.isValid()) {
            throw new IllegalArgumentException("Password reset token is no longer valid");
        }

        // Update user's password
        userService.changePassword(resetToken.getUserId(), passwordEncoder.encode(newPassword));

        resetToken.markAsUsed("SYSTEM");
        tokenRepository.save(resetToken);

        log.info("Password reset completed for user: {}", resetToken.getUserId());
    }
}
```

### 6.5 View Models

#### LoginView

```java
package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the login page.
 *
 * Traces: USA000003
 */
public record LoginView(
    String email,
    String errorMessage,
    boolean hasError,
    String logoutMessage,
    boolean hasLogoutMessage
) {
    public static LoginView empty() {
        return new LoginView("", null, false, null, false);
    }

    public static LoginView withError(String errorMessage) {
        return new LoginView("", errorMessage, true, null, false);
    }

    public static LoginView withLogoutMessage(String logoutMessage) {
        return new LoginView("", null, false, logoutMessage, true);
    }
}
```

#### ForgotPasswordView

```java
package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the forgot password page.
 *
 * Traces: USA000006
 */
public record ForgotPasswordView(
    String email,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ForgotPasswordView empty() {
        return new ForgotPasswordView("", null, false, null, false);
    }

    public static ForgotPasswordView withSuccess(String successMessage) {
        return new ForgotPasswordView("", successMessage, true, null, false);
    }

    public static ForgotPasswordView withError(String errorMessage) {
        return new ForgotPasswordView("", null, false, errorMessage, true);
    }
}
```

#### ResetPasswordView

```java
package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the reset password page.
 *
 * Traces: USA000006
 */
public record ResetPasswordView(
    String token,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError,
    boolean tokenValid
) {
    public static ResetPasswordView forToken(String token) {
        return new ResetPasswordView(token, null, false, null, false, true);
    }

    public static ResetPasswordView withSuccess(String successMessage) {
        return new ResetPasswordView("", successMessage, true, null, false, false);
    }

    public static ResetPasswordView withError(String token, String errorMessage) {
        return new ResetPasswordView(token, null, false, errorMessage, true, true);
    }

    public static ResetPasswordView invalidToken() {
        return new ResetPasswordView("", null, false, "Invalid or expired reset link.", true, false);
    }
}
```

### 6.6 Page Controllers

#### LoginPageController

```java
package com.simplecms.adminportal.authentication.internal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the login page.
 * Spring Security handles POST /login for actual authentication.
 *
 * Traces: USA000003
 */
@Controller
class LoginPageController {

    @GetMapping("/login")
    String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        LoginView view;
        if (error != null) {
            view = LoginView.withError("Invalid email or password. Please try again.");
        } else if (logout != null) {
            view = LoginView.withLogoutMessage("You have been logged out successfully.");
        } else {
            view = LoginView.empty();
        }

        model.addAttribute("view", view);
        return "authentication/LoginPage";
    }
}
```

#### ForgotPasswordPageController

```java
package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the forgot password page.
 *
 * Traces: USA000006, NFRA00003
 */
@Controller
class ForgotPasswordPageController {

    private final AuthService authService;

    ForgotPasswordPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/forgot-password")
    String showForgotPasswordPage(Model model) {
        model.addAttribute("view", ForgotPasswordView.empty());
        return "authentication/ForgotPasswordPage";
    }

    @PostMapping("/forgot-password")
    String processForgotPassword(@RequestParam("email") String email, Model model) {
        authService.requestPasswordReset(email);
        model.addAttribute("view", ForgotPasswordView.withSuccess(
            "If an account with that email exists, a password reset link has been sent."
        ));
        return "authentication/ForgotPasswordPage";
    }
}
```

#### ResetPasswordPageController

```java
package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the reset password page.
 *
 * Traces: USA000006, NFRA00003
 */
@Controller
class ResetPasswordPageController {

    private final AuthService authService;

    ResetPasswordPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/reset-password")
    String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("view", ResetPasswordView.invalidToken());
        } else {
            model.addAttribute("view", ResetPasswordView.forToken(token));
        }
        return "authentication/ResetPasswordPage";
    }

    @PostMapping("/reset-password")
    String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("view", ResetPasswordView.withError(token, "Passwords do not match."));
            return "authentication/ResetPasswordPage";
        }

        try {
            authService.resetPassword(token, newPassword);
            model.addAttribute("view", ResetPasswordView.withSuccess(
                "Your password has been reset successfully. You can now log in with your new password."
            ));
        } catch (IllegalArgumentException e) {
            model.addAttribute("view", ResetPasswordView.withError(token, e.getMessage()));
        }

        return "authentication/ResetPasswordPage";
    }
}
```

---

## 7. JTE Templates

### 7.1 LoginPage.jte

```html
@param com.simplecms.adminportal.authentication.internal.LoginView view

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Simple CMS Admin</title>
    <link rel="stylesheet" href="/css/output.css">
</head>
<body class="bg-[#f0f0f1] min-h-screen flex items-center justify-center font-body">

    <div class="w-full max-w-md px-6">
        <div class="text-center mb-8">
            <h1 class="text-2xl font-bold text-[#1e1e1e]">Simple CMS</h1>
            <p class="text-[#646970] text-sm mt-1">Admin Portal</p>
        </div>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            <h2 class="text-lg font-semibold text-[#1e1e1e] mb-5">Log In</h2>

            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            @if(view.hasLogoutMessage())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                    ${view.logoutMessage()}
                </div>
            @endif

            <form method="post" action="/login" class="space-y-4">
                <div>
                    <label for="email" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email</label>
                    <input type="email" id="email" name="email" required
                           placeholder="you@example.com"
                           value="${view.email()}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                </div>

                <div>
                    <label for="password" class="block text-sm font-medium text-[#1e1e1e] mb-1">Password</label>
                    <input type="password" id="password" name="password" required
                           placeholder="Enter your password"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                </div>

                <button type="submit"
                        class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                    Log In
                </button>
            </form>

            <div class="mt-4 text-center">
                <a href="/forgot-password" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                    Forgot your password?
                </a>
            </div>
        </div>
    </div>

</body>
</html>
```

### 7.2 ForgotPasswordPage.jte

```html
@param com.simplecms.adminportal.authentication.internal.ForgotPasswordView view

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - Simple CMS Admin</title>
    <link rel="stylesheet" href="/css/output.css">
</head>
<body class="bg-[#f0f0f1] min-h-screen flex items-center justify-center font-body">

    <div class="w-full max-w-md px-6">
        <div class="text-center mb-8">
            <h1 class="text-2xl font-bold text-[#1e1e1e]">Simple CMS</h1>
            <p class="text-[#646970] text-sm mt-1">Admin Portal</p>
        </div>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            <h2 class="text-lg font-semibold text-[#1e1e1e] mb-2">Forgot Password</h2>
            <p class="text-sm text-[#646970] mb-5">Enter your email address and we'll send you a link to reset your password.</p>

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

            <form method="post" action="/forgot-password" class="space-y-4">
                <div>
                    <label for="email" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email Address</label>
                    <input type="email" id="email" name="email" required
                           placeholder="you@example.com"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                </div>

                <button type="submit"
                        class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                    Send Reset Link
                </button>
            </form>

            <div class="mt-4 text-center">
                <a href="/login" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                    Back to Login
                </a>
            </div>
        </div>
    </div>

</body>
</html>
```

### 7.3 ResetPasswordPage.jte

```html
@param com.simplecms.adminportal.authentication.internal.ResetPasswordView view

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password - Simple CMS Admin</title>
    <link rel="stylesheet" href="/css/output.css">
</head>
<body class="bg-[#f0f0f1] min-h-screen flex items-center justify-center font-body">

    <div class="w-full max-w-md px-6">
        <div class="text-center mb-8">
            <h1 class="text-2xl font-bold text-[#1e1e1e]">Simple CMS</h1>
            <p class="text-[#646970] text-sm mt-1">Admin Portal</p>
        </div>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            <h2 class="text-lg font-semibold text-[#1e1e1e] mb-2">Reset Password</h2>
            <p class="text-sm text-[#646970] mb-5">Enter your new password below.</p>

            @if(view.hasSuccess())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">
                    ${view.successMessage()}
                </div>
                <div class="text-center mt-4">
                    <a href="/login" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">Go to Login</a>
                </div>
            @endif

            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">
                    ${view.errorMessage()}
                </div>
            @endif

            @if(view.tokenValid())
                <form method="post" action="/reset-password" class="space-y-4">
                    <input type="hidden" name="token" value="${view.token()}">

                    <div>
                        <label for="newPassword" class="block text-sm font-medium text-[#1e1e1e] mb-1">New Password</label>
                        <input type="password" id="newPassword" name="newPassword" required
                               placeholder="Enter your new password"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                    </div>

                    <div>
                        <label for="confirmPassword" class="block text-sm font-medium text-[#1e1e1e] mb-1">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required
                               placeholder="Confirm your new password"
                               class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1] focus:border-[#2271b1] transition-colors">
                    </div>

                    <button type="submit"
                            class="w-full px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px] transition-colors duration-200">
                        Reset Password
                    </button>
                </form>
            @endif

            @if(!view.tokenValid() && !view.hasSuccess())
                <div class="text-center mt-4">
                    <a href="/forgot-password" class="text-sm text-[#2271b1] hover:text-[#135e96] hover:underline">
                        Request a new reset link
                    </a>
                </div>
            @endif
        </div>
    </div>

</body>
</html>
```

---

## 8. Domain Events

| Event | Trigger | Payload | Traces |
|-------|---------|---------|--------|
| PasswordResetRequested | User submits forgot password form | userId, token, expiresAt | USA000006 |
| PasswordResetCompleted | User successfully resets password | userId, tokenId | USA000006 |

---

## 9. Cross-Module Dependencies

| Dependency | Direction | Purpose | Traces |
|------------|-----------|---------|--------|
| UserService | Auth -> User | Look up user by email for password reset; change password | USA000006 |
| PasswordEncoder | Shared (Spring Security) | BCrypt password hashing for reset flow | NFRA00003 |
| UserDTO | Auth -> User | Read user information during password reset | USA000006 |
| UserStatus | Auth -> User | Check if user is ACTIVE before allowing password reset | USA000006 |

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Token expiry set to 24 hours | Standard security practice for password reset tokens | NFRA00003 |
| 2 | Silent success on unknown email | Prevents user enumeration attacks per OWASP guidelines | NFRA00003 |
| 3 | Auth pages use standalone AuthLayout (no sidebar) | Login/forgot/reset are public pages without authenticated shell | USA000003, USA000006 |
| 4 | Spring Security handles form login POST | Leverages framework rather than custom authentication logic | USA000003 |
| 5 | Username field is `email` | User entity uses email as the login identifier per NFRA00006 | USA000003 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | PasswordResetTokenEntity | Stores secure password reset tokens with expiry | USA000006, NFRA00003 |
| Service | AuthService | Public API for authentication flows | USA000003, USA000006, USA000009 |
| Controller | LoginPageController | Renders login page with error/logout messages | USA000003 |
| Controller | ForgotPasswordPageController | Handles forgot password request flow | USA000006, NFRA00003 |
| Controller | ResetPasswordPageController | Handles password reset with token validation | USA000006, NFRA00003 |
| Template | LoginPage.jte | Login form with AuthLayout | USA000003 |
| Template | ForgotPasswordPage.jte | Email input form for password reset | USA000006 |
| Template | ResetPasswordPage.jte | New password form with token | USA000006 |
