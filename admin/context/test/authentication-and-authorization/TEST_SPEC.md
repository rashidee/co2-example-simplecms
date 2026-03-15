# Test Specification: Authentication and Authorization

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Authentication and Authorization                                      |
| Module Prefix      | AAA                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L1 (System Module)                                                    |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L1 (System Module)** because it provides foundational authentication and authorization capabilities that all other modules depend on. Login, logout, and password reset are system-level concerns that gate access to the entire admin portal. No business content management occurs in this module.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000003  | User Story | User login with email and password                                 | v1.0.0  |
| USA000006  | User Story | Forgot password flow with secure link and password reset           | v1.0.0  |
| USA000009  | User Story | User logout from the system                                       | v1.0.0  |
| NFRA00003  | NFR        | Forgot password uses typical and secure forgot password flow       | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `users` and `password_reset_tokens` tables exist with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An SMTP server or mock email service is configured for password reset emails.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed users for authentication tests
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('a1000000-0000-0000-0000-000000000001', 'admin@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Admin', 'ADMIN', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system'),
  ('a1000000-0000-0000-0000-000000000002', 'editor1@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alice', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'admin'),
  ('a1000000-0000-0000-0000-000000000003', 'inactive@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob', 'Inactive', 'USER', 'INACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'admin');

-- Note: The hashed password above corresponds to the plaintext "password"

-- Seed a valid password reset token (expires in the future)
INSERT INTO password_reset_tokens (id, user_id, token, expires_at, used, version, created_at, created_by)
VALUES
  ('b1000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000002', 'valid-reset-token-abc123', '2026-03-20 23:59:59', false, 0, '2026-03-15 10:00:00', 'system');

-- Seed an expired password reset token
INSERT INTO password_reset_tokens (id, user_id, token, expires_at, used, version, created_at, created_by)
VALUES
  ('b1000000-0000-0000-0000-000000000002', 'a1000000-0000-0000-0000-000000000002', 'expired-reset-token-xyz789', '2026-03-10 23:59:59', false, 0, '2026-03-09 10:00:00', 'system');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-AAA-001: Navigate to Login Page

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-AAA-001                                                           |
| Title            | Navigate to the login page                                            |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000003                                                             |
| Preconditions    | User is not authenticated. Application is running.                    |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Open the Admin Portal base URL in a browser.        | The login page is displayed.                                         |
| 2    | Verify the login form contains email and password fields. | Email input field and password input field are visible.              |
| 3    | Verify a "Forgot Password?" link is present.        | A link labeled "Forgot Password?" is visible below the form.         |
| 4    | Verify a "Login" submit button is present.          | A button labeled "Login" is visible and enabled.                     |

---

### 6.2 Authentication Tests

#### AUTH-AAA-001: Login with Valid Credentials

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-001                                                          |
| Title            | Successful login with valid email and password                        |
| Type             | CRUD (Authentication)                                                 |
| Priority         | Critical                                                              |
| Source           | USA000003                                                             |
| Preconditions    | Seeded user `admin@simplecms.com` exists with status ACTIVE.          |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the login page.                         | Login page is displayed.                                             |
| 2    | Enter `admin@simplecms.com` in the email field.     | Email field is populated.                                            |
| 3    | Enter `password` in the password field.             | Password field is populated (masked).                                |
| 4    | Click the "Login" button.                           | User is redirected to the admin dashboard.                           |
| 5    | Verify the authenticated user's name is displayed.  | "System Admin" or similar greeting is shown in the header/sidebar.   |

---

#### AUTH-AAA-002: Login with Invalid Credentials

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-002                                                          |
| Title            | Login fails with invalid email or password                            |
| Type             | CRUD (Authentication)                                                 |
| Priority         | Critical                                                              |
| Source           | USA000003                                                             |
| Preconditions    | Application is running. Login page is displayed.                      |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the login page.                         | Login page is displayed.                                             |
| 2    | Enter `admin@simplecms.com` in the email field.     | Email field is populated.                                            |
| 3    | Enter `wrongpassword` in the password field.        | Password field is populated.                                         |
| 4    | Click the "Login" button.                           | An error message "Invalid email or password" is displayed.           |
| 5    | Verify the user remains on the login page.          | Login form is still visible. User is not redirected.                 |

---

#### AUTH-AAA-003: Login Blocked for Inactive User

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-003                                                          |
| Title            | Login is blocked for a user with INACTIVE status                      |
| Type             | CRUD (Authentication)                                                 |
| Priority         | High                                                                  |
| Source           | USA000003, CONSA0006                                                  |
| Preconditions    | Seeded user `inactive@simplecms.com` exists with status INACTIVE.     |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the login page.                         | Login page is displayed.                                             |
| 2    | Enter `inactive@simplecms.com` in the email field.  | Email field is populated.                                            |
| 3    | Enter `password` in the password field.             | Password field is populated.                                         |
| 4    | Click the "Login" button.                           | An error message indicating the account is inactive is displayed.    |
| 5    | Verify the user remains on the login page.          | Login form is still visible. User is not authenticated.              |

---

#### AUTH-AAA-004: Forgot Password Request

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-004                                                          |
| Title            | Request a forgot password email                                       |
| Type             | CRUD (Authentication)                                                 |
| Priority         | High                                                                  |
| Source           | USA000006, NFRA00003                                                  |
| Preconditions    | User `editor1@simplecms.com` exists with status ACTIVE.               |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the login page.                         | Login page is displayed.                                             |
| 2    | Click the "Forgot Password?" link.                  | The forgot password page is displayed with an email input field.     |
| 3    | Enter `editor1@simplecms.com` in the email field.   | Email field is populated.                                            |
| 4    | Click the "Submit" or "Send Reset Link" button.     | A success message is displayed: "If the email exists, a reset link has been sent." |
| 5    | Verify a new record is created in `password_reset_tokens` table. | A new token record exists for user `a1000000-0000-0000-0000-000000000002` with `used = false` and `expires_at` in the future. |

---

#### AUTH-AAA-005: Reset Password with Valid Token

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-005                                                          |
| Title            | Reset password using a valid, non-expired token                       |
| Type             | CRUD (Authentication)                                                 |
| Priority         | High                                                                  |
| Source           | USA000006, NFRA00003                                                  |
| Preconditions    | Seeded valid token `valid-reset-token-abc123` exists and has not expired. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the reset password URL with token `valid-reset-token-abc123`. | The reset password page is displayed with new password and confirm password fields. |
| 2    | Enter `NewSecurePass1!` in the new password field.  | Password field is populated.                                         |
| 3    | Enter `NewSecurePass1!` in the confirm password field. | Confirm password field is populated.                                |
| 4    | Click the "Reset Password" button.                  | A success message is displayed: "Password has been reset successfully." |
| 5    | Verify the token is marked as used in the database. | The record in `password_reset_tokens` with token `valid-reset-token-abc123` has `used = true`. |
| 6    | Attempt to login with the new password.             | Login succeeds with `editor1@simplecms.com` / `NewSecurePass1!`.     |

---

#### AUTH-AAA-006: Reset Password with Expired Token

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-006                                                          |
| Title            | Reset password fails with an expired token                            |
| Type             | CRUD (Authentication)                                                 |
| Priority         | High                                                                  |
| Source           | USA000006, NFRA00003                                                  |
| Preconditions    | Seeded expired token `expired-reset-token-xyz789` exists with `expires_at` in the past. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the reset password URL with token `expired-reset-token-xyz789`. | An error message is displayed: "This reset link has expired. Please request a new one." |
| 2    | Verify the user is redirected to the forgot password page or shown a link to request again. | A link or button to request a new reset link is available.           |

---

#### AUTH-AAA-007: Logout

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | AUTH-AAA-007                                                          |
| Title            | User logs out from the system                                         |
| Type             | CRUD (Authentication)                                                 |
| Priority         | High                                                                  |
| Source           | USA000009                                                             |
| Preconditions    | User is authenticated and on the admin dashboard.                     |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Verify the user is logged in and on the dashboard.  | Dashboard is displayed with user information.                        |
| 2    | Click the "Logout" button or link.                  | User is logged out and redirected to the login page.                 |
| 3    | Attempt to navigate to a protected page (e.g., dashboard URL). | User is redirected to the login page. The protected page is not accessible. |

---

### 6.3 Validation Tests

#### VAL-AAA-001: Login Required Fields Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-AAA-001                                                           |
| Title            | Login form validates required fields                                  |
| Type             | Validation                                                            |
| Priority         | Medium                                                                |
| Source           | USA000003                                                             |
| Preconditions    | Login page is displayed.                                              |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave the email field empty and the password field empty. | Both fields are empty.                                              |
| 2    | Click the "Login" button.                           | Validation error messages are displayed for both email and password fields. |
| 3    | Enter `admin@simplecms.com` in the email field, leave password empty. | Email is populated, password is empty.                              |
| 4    | Click the "Login" button.                           | Validation error message is displayed for the password field.        |
| 5    | Clear email, enter `password` in the password field. | Email is empty, password is populated.                              |
| 6    | Click the "Login" button.                           | Validation error message is displayed for the email field.           |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete password reset tokens first (FK dependency)
DELETE FROM password_reset_tokens WHERE id IN (
  'b1000000-0000-0000-0000-000000000001',
  'b1000000-0000-0000-0000-000000000002'
);

-- Delete seeded users
DELETE FROM users WHERE id IN (
  'a1000000-0000-0000-0000-000000000001',
  'a1000000-0000-0000-0000-000000000002',
  'a1000000-0000-0000-0000-000000000003'
);
```

## 8. Traceability Matrix

| Scenario ID   | Type           | USA000003 | USA000006 | USA000009 | NFRA00003 |
|---------------|----------------|-----------|-----------|-----------|-----------|
| NAV-AAA-001   | Navigation     | X         |           |           |           |
| AUTH-AAA-001  | Authentication | X         |           |           |           |
| AUTH-AAA-002  | Authentication | X         |           |           |           |
| AUTH-AAA-003  | Authentication | X         |           |           |           |
| AUTH-AAA-004  | Authentication |           | X         |           | X         |
| AUTH-AAA-005  | Authentication |           | X         |           | X         |
| AUTH-AAA-006  | Authentication |           | X         |           | X         |
| AUTH-AAA-007  | Authentication |           |           | X         |           |
| VAL-AAA-001   | Validation     | X         |           |           |           |
