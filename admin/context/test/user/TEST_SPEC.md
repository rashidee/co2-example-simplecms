# Test Specification: User

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | User                                                                  |
| Module Prefix      | USR                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L1 (System Module)                                                    |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L1 (System Module)** because user management is a foundational system concern. User accounts, roles, and statuses are prerequisites for all other modules. The authentication module depends on the User entity for login credentials, and the Blog module references users as authors. User management is restricted to ADMIN role, making it a system administration function rather than a business content function.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000012  | User Story | View user profile                                                  | v1.0.0  |
| USA000015  | User Story | View account information and change password                       | v1.0.0  |
| USA000018  | User Story | Admin views list of users                                          | v1.0.0  |
| USA000021  | User Story | Admin creates a new user                                           | v1.0.0  |
| USA000024  | User Story | Admin edits a user                                                 | v1.0.0  |
| USA000027  | User Story | Admin deletes a user                                               | v1.0.0  |
| NFRA00006  | NFR        | New user information fields (email, password, name, role, status)  | v1.0.0  |
| NFRA00009  | NFR        | Default role is USER and EDITOR; only admin can upgrade to ADMIN   | v1.0.0  |
| NFRA00012  | NFR        | Default password is "password"; forced change on first login       | v1.0.0  |
| NFRA00015  | NFR        | System seeds admin user on startup if no users exist               | v1.0.0  |
| CONSA0003  | Constraint | Only first name and last name can be changed on User Profile       | v1.0.0  |
| CONSA0006  | Constraint | User statuses: ACTIVE (default), INACTIVE (blocks login)          | v1.0.0  |
| CONSA0009  | Constraint | Only ADMIN role can access user management and perform CRUD        | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `users` table exists with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- At least one ADMIN user exists for performing user management operations.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed 5 users with different roles and statuses
-- Note: Password hash corresponds to plaintext "password" using BCrypt
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u1000000-0000-0000-0000-000000000001', 'admin@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System', 'Admin', 'ADMIN', 'ACTIVE', false, '2026-03-14 09:00:00', 0, '2026-03-01 08:00:00', 'system'),
  ('u1000000-0000-0000-0000-000000000002', 'editor1@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alice', 'Johnson', 'EDITOR', 'ACTIVE', false, '2026-03-13 14:30:00', 0, '2026-03-02 09:00:00', 'admin'),
  ('u1000000-0000-0000-0000-000000000003', 'editor2@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob', 'Williams', 'EDITOR', 'ACTIVE', false, '2026-03-12 11:00:00', 0, '2026-03-03 10:00:00', 'admin'),
  ('u1000000-0000-0000-0000-000000000004', 'user1@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Carol', 'Davis', 'USER', 'ACTIVE', true, NULL, 0, '2026-03-04 11:00:00', 'admin'),
  ('u1000000-0000-0000-0000-000000000005', 'user2@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dave', 'Brown', 'USER', 'INACTIVE', false, '2026-03-05 08:00:00', 0, '2026-03-05 08:00:00', 'admin');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-USR-001: Navigate to Users Management Page

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-USR-001                                                           |
| Title            | Navigate to the users management page as admin                        |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000018, CONSA0009                                                  |
| Preconditions    | User is logged in as `admin@simplecms.com` (ADMIN role).              |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Users" or "User Management" link in the sidebar navigation. | The users management page is displayed.                             |
| 2    | Verify the page shows a list of users in a table format. | A table listing users with columns for name, email, role, status, and actions is displayed. |
| 3    | Verify a "Create User" button is present.           | A button labeled "Create User" or similar is visible.                |

---

### 6.2 View/Detail Tests

#### VIEW-USR-001: View User Profile

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VIEW-USR-001                                                          |
| Title            | View own profile information                                          |
| Type             | View/Detail                                                           |
| Priority         | High                                                                  |
| Source           | USA000012, CONSA0003                                                  |
| Preconditions    | User is logged in as `editor1@simplecms.com`.                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Profile page.                       | The profile page is displayed.                                       |
| 2    | Verify the first name "Alice" is displayed.         | First name field shows "Alice".                                      |
| 3    | Verify the last name "Johnson" is displayed.        | Last name field shows "Johnson".                                     |
| 4    | Verify the email "editor1@simplecms.com" is displayed but not editable. | Email is shown as read-only text or a disabled input field.         |
| 5    | Verify the role "EDITOR" is displayed but not editable. | Role is shown as read-only text or a disabled input field.          |

---

#### VIEW-USR-002: View Account Information

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VIEW-USR-002                                                          |
| Title            | View account information with change password option                  |
| Type             | View/Detail                                                           |
| Priority         | High                                                                  |
| Source           | USA000015                                                             |
| Preconditions    | User is logged in as `editor1@simplecms.com`.                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Account page.                       | The account page is displayed.                                       |
| 2    | Verify account information is displayed (email, role, status). | Account details are shown.                                          |
| 3    | Verify a "Change Password" form or section is present. | Fields for current password, new password, and confirm new password are visible. |

---

### 6.3 CRUD Tests

#### CRUD-USR-001: Update Profile (Name Only)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-USR-001                                                          |
| Title            | Update profile - only first name and last name can be changed         |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000012, CONSA0003                                                  |
| Preconditions    | User is logged in as `editor1@simplecms.com`.                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Profile page.                       | The profile page is displayed with current information.              |
| 2    | Change the first name from "Alice" to "Alicia".     | First name field is updated.                                         |
| 3    | Change the last name from "Johnson" to "Smith".     | Last name field is updated.                                          |
| 4    | Click the "Save" or "Update Profile" button.        | A success message "Profile updated successfully" is displayed.       |
| 5    | Refresh the page and verify the changes persisted.  | First name shows "Alicia", last name shows "Smith".                  |
| 6    | Verify the email field was not editable.            | Email remains `editor1@simplecms.com` and was not modifiable.        |

---

#### CRUD-USR-002: Change Password

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-USR-002                                                          |
| Title            | Change password from account page                                     |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000015                                                             |
| Preconditions    | User is logged in as `editor1@simplecms.com` with current password "password". |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Account page.                       | The account page is displayed.                                       |
| 2    | Enter `password` in the current password field.     | Current password field is populated.                                 |
| 3    | Enter `NewSecurePass1!` in the new password field.  | New password field is populated.                                     |
| 4    | Enter `NewSecurePass1!` in the confirm password field. | Confirm password field is populated.                                |
| 5    | Click the "Change Password" button.                 | A success message "Password changed successfully" is displayed.      |
| 6    | Log out and log back in with the new password.      | Login succeeds with `editor1@simplecms.com` / `NewSecurePass1!`.     |

---

#### CRUD-USR-003: Create User (Admin Only)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-USR-003                                                          |
| Title            | Admin creates a new user                                              |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000021, NFRA00006, NFRA00009, NFRA00012, CONSA0009                |
| Preconditions    | User is logged in as `admin@simplecms.com` (ADMIN role).              |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Users Management page.              | The users list page is displayed.                                    |
| 2    | Click the "Create User" button.                     | The create user form is displayed.                                   |
| 3    | Enter `newuser@simplecms.com` in the email field.   | Email field is populated.                                            |
| 4    | Enter `Test` in the first name field.               | First name field is populated.                                       |
| 5    | Enter `User` in the last name field.                | Last name field is populated.                                        |
| 6    | Select `EDITOR` from the role dropdown.             | Role is set to EDITOR.                                               |
| 7    | Select `ACTIVE` from the status dropdown.           | Status is set to ACTIVE.                                             |
| 8    | Click the "Save" or "Create" button.                | A success message "User created successfully" is displayed.          |
| 9    | Verify the new user appears in the users list.      | `newuser@simplecms.com` with role EDITOR and status ACTIVE is listed.|
| 10   | Verify in the database that the password is "password" (hashed) and `force_password_change` is `true`. | Database record confirms default password and forced change flag.   |

---

#### CRUD-USR-004: Edit User (Admin Only)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-USR-004                                                          |
| Title            | Admin edits an existing user                                          |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000024, CONSA0009                                                  |
| Preconditions    | User is logged in as `admin@simplecms.com`. Seeded user `user1@simplecms.com` exists. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Users Management page.              | The users list page is displayed.                                    |
| 2    | Click the "Edit" link for user `user1@simplecms.com`. | The edit user form is displayed with pre-populated data.            |
| 3    | Change the first name from "Carol" to "Caroline".   | First name field is updated.                                         |
| 4    | Change the role from "USER" to "EDITOR".            | Role dropdown is updated to EDITOR.                                  |
| 5    | Click the "Save" or "Update" button.                | A success message "User updated successfully" is displayed.          |
| 6    | Verify the changes in the users list.               | User shows "Caroline Davis" with role EDITOR.                        |

---

#### CRUD-USR-005: Delete User (Admin Only)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-USR-005                                                          |
| Title            | Admin deletes a user                                                  |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000027, CONSA0009                                                  |
| Preconditions    | User is logged in as `admin@simplecms.com`. Seeded user `user2@simplecms.com` exists. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Users Management page.              | The users list page is displayed.                                    |
| 2    | Click the "Delete" link for user `user2@simplecms.com`. | A confirmation dialog is displayed.                                 |
| 3    | Confirm the deletion.                               | A success message "User deleted successfully" is displayed.          |
| 4    | Verify the user no longer appears in the list.      | `user2@simplecms.com` is not in the users list.                      |
| 5    | Verify the user is removed from the database.       | No record exists in `users` table with email `user2@simplecms.com`.  |

---

### 6.4 Validation Tests

#### VAL-USR-001: Create User Required Fields Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-USR-001                                                           |
| Title            | Create user form validates all required fields                        |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000021, NFRA00006                                                  |
| Preconditions    | User is logged in as `admin@simplecms.com`. Create user form is displayed. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty on the create user form.     | All fields are empty.                                                |
| 2    | Click the "Save" or "Create" button.                | Validation error messages are displayed for email, first name, and last name fields. |
| 3    | Enter only the email field and submit.              | Validation errors remain for first name and last name.               |
| 4    | Fill in all required fields and submit.             | User is created successfully with no validation errors.              |

---

#### VAL-USR-002: Duplicate Email Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-USR-002                                                           |
| Title            | Create user fails with duplicate email                                |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000021, NFRA00006                                                  |
| Preconditions    | User is logged in as `admin@simplecms.com`. Seeded user `editor1@simplecms.com` exists. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Create User form.                   | Create user form is displayed.                                       |
| 2    | Enter `editor1@simplecms.com` in the email field.   | Email field is populated with an existing email.                     |
| 3    | Fill in all other required fields.                  | All fields are populated.                                            |
| 4    | Click the "Save" or "Create" button.                | A validation error "Email already exists" is displayed.              |
| 5    | Verify no new user record is created in the database. | Only one record with email `editor1@simplecms.com` exists.          |

---

#### VAL-USR-003: Non-Admin Access Denied to User Management

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-USR-003                                                           |
| Title            | Non-admin users cannot access user management page                    |
| Type             | Validation (Authorization)                                            |
| Priority         | Critical                                                              |
| Source           | CONSA0009                                                             |
| Preconditions    | User is logged in as `editor1@simplecms.com` (EDITOR role).          |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Attempt to navigate to the Users Management page URL directly. | Access is denied. User is shown a 403 Forbidden page or redirected. |
| 2    | Verify the "Users" or "User Management" link is not visible in the sidebar. | The sidebar does not show a user management navigation item for EDITOR users. |
| 3    | Attempt to access the Create User URL directly.     | Access is denied with 403 Forbidden.                                 |
| 4    | Attempt to access the Edit User URL directly.       | Access is denied with 403 Forbidden.                                 |

---

### 6.5 Pagination Tests

#### PAGE-USR-001: Users List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-USR-001                                                          |
| Title            | Users list supports pagination                                        |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000018                                                             |
| Preconditions    | More than 10 users exist in the database (seed additional records if needed). |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Users Management page.              | The first page of users is displayed with a limited number of records (e.g., 10 per page). |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | The second page of users is displayed with the next set of records.  |
| 4    | Click "Previous" or page 1.                         | The first page is displayed again with the original set of records.  |

---

### 6.6 Seed Verification Tests

#### SEED-USR-001: Verify Admin User Seeded on Startup

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SEED-USR-001                                                          |
| Title            | System seeds an admin user on startup when no users exist             |
| Type             | View/Detail (Seed Verification)                                       |
| Priority         | High                                                                  |
| Source           | NFRA00015                                                             |
| Preconditions    | The `users` table is empty. Application is restarted.                 |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Ensure the `users` table is empty: `DELETE FROM users;` | Table is empty.                                                     |
| 2    | Restart the Admin Portal application.               | Application starts successfully.                                     |
| 3    | Query the `users` table: `SELECT * FROM users;`     | One record exists with email `admin`, role `ADMIN`, and `force_password_change = true`. |
| 4    | Attempt to log in with `admin` / `password`.        | Login succeeds. User is prompted to change the password.             |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete any users created during tests
DELETE FROM users WHERE email = 'newuser@simplecms.com';

-- Delete seeded users
DELETE FROM users WHERE id IN (
  'u1000000-0000-0000-0000-000000000001',
  'u1000000-0000-0000-0000-000000000002',
  'u1000000-0000-0000-0000-000000000003',
  'u1000000-0000-0000-0000-000000000004',
  'u1000000-0000-0000-0000-000000000005'
);
```

## 8. Traceability Matrix

| Scenario ID    | Type           | USA000012 | USA000015 | USA000018 | USA000021 | USA000024 | USA000027 | NFRA00006 | NFRA00009 | NFRA00012 | NFRA00015 | CONSA0003 | CONSA0006 | CONSA0009 |
|----------------|----------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-USR-001    | Navigation     |           |           | X         |           |           |           |           |           |           |           |           |           | X         |
| VIEW-USR-001   | View/Detail    | X         |           |           |           |           |           |           |           |           |           | X         |           |           |
| VIEW-USR-002   | View/Detail    |           | X         |           |           |           |           |           |           |           |           |           |           |           |
| CRUD-USR-001   | CRUD (Update)  | X         |           |           |           |           |           |           |           |           |           | X         |           |           |
| CRUD-USR-002   | CRUD (Update)  |           | X         |           |           |           |           |           |           |           |           |           |           |           |
| CRUD-USR-003   | CRUD (Create)  |           |           |           | X         |           |           | X         | X         | X         |           |           |           | X         |
| CRUD-USR-004   | CRUD (Update)  |           |           |           |           | X         |           |           |           |           |           |           |           | X         |
| CRUD-USR-005   | CRUD (Delete)  |           |           |           |           |           | X         |           |           |           |           |           |           | X         |
| VAL-USR-001    | Validation     |           |           |           | X         |           |           | X         |           |           |           |           |           |           |
| VAL-USR-002    | Validation     |           |           |           | X         |           |           | X         |           |           |           |           |           |           |
| VAL-USR-003    | Validation     |           |           |           |           |           |           |           |           |           |           |           |           | X         |
| PAGE-USR-001   | Pagination     |           |           | X         |           |           |           |           |           |           |           |           |           |           |
| SEED-USR-001   | Seed Verify    |           |           |           |           |           |           |           |           |           | X         |           |           |           |
