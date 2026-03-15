# TEST_PLAN.md - Simple CMS Admin Portal E2E Test Plan

| Field              | Value                                           |
|--------------------|-------------------------------------------------|
| Project Code       | SCMS                                            |
| Application        | Admin Portal                                    |
| Application Initials | A                                              |
| Version            | v1.0.0                                          |
| Date               | 2026-03-15                                      |
| Test Type          | End-to-End (E2E) - Playwright                   |

---

## 1. Application Overview

The Simple CMS Admin Portal is a web application built with Spring Boot 3, JTE, Tailwind CSS, Alpine.js, and HTMX. It allows authenticated users to manage marketing page content (hero sections, products and services, features, testimonials, team members, contact information) and blog content (categories and posts).

**Technology Stack:**

| Component       | Technology                        |
|-----------------|-----------------------------------|
| Backend         | Spring Boot 3                     |
| Templating      | JTE (Java Template Engine)        |
| CSS             | Tailwind CSS                      |
| JS Interactivity| Alpine.js, HTMX                   |
| Authentication  | Spring Security (form login)      |
| Database        | PostgreSQL 14                     |

**Roles:**

| Role   | Description                                      |
|--------|--------------------------------------------------|
| ADMIN  | Full access including user management             |
| EDITOR | Content management access                         |
| USER   | Basic read access                                 |

---

## 2. Test Infrastructure

| Component | Type           | CLI Tool | Connection String                                              |
|-----------|----------------|----------|----------------------------------------------------------------|
| Database  | PostgreSQL 14  | `psql`   | `postgresql://cms_user:cms_password@localhost:5432/cms_db`      |

**No Message Queue** - The application does not use a message queue.

**No SSO** - The application manages its own users via Spring Security form login.

### psql CLI Reference

All data seeding and teardown commands use `psql` to interact with the database directly.

```bash
# Connect to the database
psql postgresql://cms_user:cms_password@localhost:5432/cms_db

# Execute a SQL file
psql postgresql://cms_user:cms_password@localhost:5432/cms_db -f <file.sql>

# Execute inline SQL
psql postgresql://cms_user:cms_password@localhost:5432/cms_db -c "<SQL_STATEMENT>"
```

---

## 3. Test Users

All test users are seeded via `psql` into the `users` table before test execution.

| User   | Email                    | Password   | Role   | Purpose                       |
|--------|--------------------------|------------|--------|-------------------------------|
| admin  | admin@simplecms.com      | password   | ADMIN  | User management tests         |
| editor | editor@simplecms.com     | password   | EDITOR | Content management tests      |
| user   | user@simplecms.com       | password   | USER   | Basic access tests            |

---

## 4. Layer Classification Summary

| Module                             | Category | Layer              | Seeding Strategy                                                        | Dependencies |
|------------------------------------|----------|--------------------|-------------------------------------------------------------------------|--------------|
| Authentication and Authorization   | System   | L1: Auth           | psql (seed `password_reset_tokens`)                                     | User         |
| User                               | System   | L1: Auth           | psql (seed `users` table)                                              | None         |
| Hero Section                       | Business | L2: Reference Data | psql insert into `hero_sections`                                       | None         |
| Product and Service Section        | Business | L2: Reference Data | psql insert into `product_services`                                    | None         |
| Features Section                   | Business | L2: Reference Data | psql insert into `features`                                            | None         |
| Testimonials Section               | Business | L2: Reference Data | psql insert into `testimonials`                                        | None         |
| Team Section                       | Business | L2: Reference Data | psql insert into `team_members`                                        | None         |
| Contact Section                    | Business | L2: Reference Data | psql insert into `contact_info`, `contact_messages`, `contact_responses`| None         |
| Blog                               | Business | L2: Reference Data | psql insert into `blog_categories`, `blog_posts`                       | User (author FK) |

---

## 5. Execution Order

Tests are executed in layer order to respect data dependencies. Within each layer, modules without cross-dependencies may run in parallel.

### Layer 1: Auth (System Modules)

| Order | Module                             | Reason                                                |
|-------|------------------------------------|-------------------------------------------------------|
| 1.1   | User                               | Seed users first; no dependencies                     |
| 1.2   | Authentication and Authorization   | Depends on User (requires seeded users to log in)     |

### Layer 2: Reference Data (Business Modules)

| Order | Module                          | Reason                                                     |
|-------|---------------------------------|------------------------------------------------------------|
| 2.1   | Hero Section                    | No dependencies; can run in parallel with 2.2-2.6          |
| 2.2   | Product and Service Section     | No dependencies; can run in parallel with 2.1, 2.3-2.6    |
| 2.3   | Features Section                | No dependencies; can run in parallel with 2.1-2.2, 2.4-2.6|
| 2.4   | Testimonials Section            | No dependencies; can run in parallel with 2.1-2.3, 2.5-2.6|
| 2.5   | Team Section                    | No dependencies; can run in parallel with 2.1-2.4, 2.6    |
| 2.6   | Contact Section                 | No dependencies; can run in parallel with 2.1-2.5         |
| 2.7   | Blog                            | Depends on User (author FK); must run after L1 completes   |

---

## 6. Table of Contents

Each module has a dedicated `TEST_SPEC.md` file containing the full E2E test scenarios.

| # | Module                             | Layer              | Test Spec Path                                                          |
|---|------------------------------------|--------------------|-------------------------------------------------------------------------|
| 1 | User                               | L1: Auth           | [TEST_SPEC.md](user/TEST_SPEC.md)                                      |
| 2 | Authentication and Authorization   | L1: Auth           | [TEST_SPEC.md](authentication-and-authorization/TEST_SPEC.md)           |
| 3 | Hero Section                       | L2: Reference Data | [TEST_SPEC.md](hero-section/TEST_SPEC.md)                              |
| 4 | Product and Service Section        | L2: Reference Data | [TEST_SPEC.md](product-and-service-section/TEST_SPEC.md)               |
| 5 | Features Section                   | L2: Reference Data | [TEST_SPEC.md](features-section/TEST_SPEC.md)                          |
| 6 | Testimonials Section               | L2: Reference Data | [TEST_SPEC.md](testimonials-section/TEST_SPEC.md)                      |
| 7 | Team Section                       | L2: Reference Data | [TEST_SPEC.md](team-section/TEST_SPEC.md)                              |
| 8 | Contact Section                    | L2: Reference Data | [TEST_SPEC.md](contact-section/TEST_SPEC.md)                           |
| 9 | Blog                               | L2: Reference Data | [TEST_SPEC.md](blog/TEST_SPEC.md)                                      |

---

## 7. Module Summaries

### 7.1 User

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | User                                                                   |
| Category            | System                                                                 |
| Layer               | L1: Auth                                                               |
| Stories             | 6 US, 4 NFR, 3 CONS                                                   |
| Estimated Scenarios | ~18                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `users` table (admin, editor, user accounts with hashed passwords)     |
| Roles Tested        | ADMIN, EDITOR, USER                                                    |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to user management section                                    |
| CRUD       | ~8    | Profile view/edit, user list, create user, edit user, delete user      |
| Validation | ~4    | Required fields, email format, password strength, duplicate email      |
| Security   | ~3    | Role-based access, password change requirements, account management    |
| Pagination | ~2    | User list pagination, sorting                                          |

**Key Assertions:**
- ADMIN can create, edit, and delete users
- EDITOR and USER cannot access user management
- Password change requires current password confirmation
- Profile edit persists changes correctly
- Duplicate email addresses are rejected

---

### 7.2 Authentication and Authorization

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Authentication and Authorization                                       |
| Category            | System                                                                 |
| Layer               | L1: Auth                                                               |
| Stories             | 3 US, 1 NFR                                                           |
| Estimated Scenarios | ~8                                                                     |
| Dependencies        | User (requires seeded user accounts)                                   |
| Seeded Data         | `password_reset_tokens` table                                          |
| Roles Tested        | ADMIN, EDITOR, USER                                                    |

**Scenario Breakdown:**

| Type             | Count | Key Areas                                                         |
|------------------|-------|-------------------------------------------------------------------|
| Navigation       | ~1    | Navigate to login page                                            |
| Login            | ~2    | Successful login, invalid credentials                             |
| Logout           | ~1    | Successful logout and session invalidation                        |
| Forgot Password  | ~2    | Request password reset, invalid email handling                    |
| Reset Password   | ~1    | Reset password via token                                          |
| Validation       | ~1    | Login form validation (empty fields, format)                      |

**Key Assertions:**
- Valid credentials grant access and redirect to dashboard
- Invalid credentials display error message and remain on login page
- Logout invalidates session and redirects to login page
- Forgot password sends reset email for valid accounts
- Password reset token expires after use
- Unauthenticated users are redirected to login page

---

### 7.3 Hero Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Hero Section                                                           |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 2 US, 7 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~10                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `hero_sections` table                                                  |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to hero section management                                    |
| List/Filter| ~2    | View hero sections list, filter/search                                 |
| Create     | ~2    | Create hero section with all fields, with image upload                 |
| Edit       | ~2    | Edit hero section content, update image                                |
| Validation | ~2    | Required fields, image format/size constraints                         |
| Pagination | ~1    | Hero sections list pagination                                          |

**Key Assertions:**
- Hero section displays title, description, and CTA button text
- Image upload accepts valid formats and rejects invalid ones
- Required fields are enforced on create and edit forms
- List displays hero sections with correct content
- Edit persists all field changes including image replacement

---

### 7.4 Product and Service Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Product and Service Section                                            |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 4 US, 7 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~12                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `product_services` table                                               |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to products and services management                           |
| List/Filter| ~2    | View list, filter/search products and services                         |
| Create     | ~2    | Create with all fields, with photo upload                              |
| Edit       | ~2    | Edit content, update photo                                             |
| Delete     | ~2    | Delete product/service, confirmation dialog                            |
| Validation | ~2    | Required fields, photo format/size constraints                         |
| Pagination | ~1    | Products and services list pagination                                  |

**Key Assertions:**
- Products and services display name, description, and photo
- CRUD operations persist and reflect in the list view
- Photo upload validates format and size constraints
- Delete requires confirmation before removal
- List supports pagination for large datasets

---

### 7.5 Features Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Features Section                                                       |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 4 US, 3 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~10                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `features` table                                                       |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to features management                                        |
| List/Filter| ~2    | View features list, filter/search                                      |
| Create     | ~2    | Create feature with title, description, icon                           |
| Edit       | ~2    | Edit feature content, update icon                                      |
| Delete     | ~1    | Delete feature with confirmation                                       |
| Validation | ~2    | Required fields, icon selection constraints                            |

**Key Assertions:**
- Features display title, description, and icon
- CRUD operations persist and reflect in the list view
- Icon selection works correctly on create and edit
- Required fields are enforced
- Delete removes the feature from the list

---

### 7.6 Testimonials Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Testimonials Section                                                   |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 4 US, 4 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~10                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `testimonials` table                                                   |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to testimonials management                                    |
| List/Filter| ~2    | View testimonials list, filter/search                                  |
| Create     | ~2    | Create testimonial with customer name, quote, photo                    |
| Edit       | ~2    | Edit testimonial content, update photo                                 |
| Delete     | ~1    | Delete testimonial with confirmation                                   |
| Validation | ~2    | Required fields, photo format/size constraints                         |

**Key Assertions:**
- Testimonials display customer name, quote, and photo
- CRUD operations persist and reflect in the list view
- Photo upload validates format and size constraints
- Required fields are enforced on create and edit
- Delete removes the testimonial from the list

---

### 7.7 Team Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Team Section                                                           |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 4 US, 5 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~10                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `team_members` table                                                   |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type       | Count | Key Areas                                                              |
|------------|-------|------------------------------------------------------------------------|
| Navigation | ~1    | Navigate to team members management                                    |
| List/Filter| ~2    | View team members list, filter/search                                  |
| Create     | ~2    | Create team member with name, role, bio, photo                         |
| Edit       | ~2    | Edit team member content, update photo                                 |
| Delete     | ~1    | Delete team member with confirmation                                   |
| Validation | ~2    | Required fields, photo format/size constraints                         |

**Key Assertions:**
- Team members display name, role, bio, and photo
- CRUD operations persist and reflect in the list view
- Photo upload validates format and size constraints
- Required fields are enforced on create and edit
- Delete removes the team member from the list

---

### 7.8 Contact Section

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Contact Section                                                        |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 4 US, 5 NFR, 1 CONS                                                   |
| Estimated Scenarios | ~12                                                                    |
| Dependencies        | None                                                                   |
| Seeded Data         | `contact_info`, `contact_messages`, `contact_responses` tables         |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type             | Count | Key Areas                                                         |
|------------------|-------|-------------------------------------------------------------------|
| Navigation       | ~1    | Navigate to contact section management                            |
| Contact Info     | ~2    | View and edit contact information (address, phone, email, map)    |
| Messages List    | ~2    | View contact messages list, filter/search                         |
| Message Detail   | ~2    | View message detail, mark as read                                 |
| Respond          | ~2    | Respond to contact message, view response history                 |
| Validation       | ~2    | Required fields on contact info edit, response form               |
| Pagination       | ~1    | Contact messages list pagination                                  |

**Key Assertions:**
- Contact info displays address, phone, email, and map location
- Contact messages list shows sender, subject, and status
- Message detail view displays full message content
- Response is sent and recorded in response history
- Required fields are enforced on contact info edit and response forms

---

### 7.9 Blog

| Field               | Value                                                                  |
|---------------------|------------------------------------------------------------------------|
| Module              | Blog                                                                   |
| Category            | Business                                                               |
| Layer               | L2: Reference Data                                                     |
| Stories             | 5 US, 8 NFR, 4 CONS                                                   |
| Estimated Scenarios | ~16                                                                    |
| Dependencies        | User (author FK in `blog_posts`)                                       |
| Seeded Data         | `blog_categories`, `blog_posts` tables                                 |
| Roles Tested        | ADMIN, EDITOR                                                          |

**Scenario Breakdown:**

| Type                    | Count | Key Areas                                                    |
|-------------------------|-------|--------------------------------------------------------------|
| Navigation              | ~1    | Navigate to blog management                                  |
| Categories CRUD         | ~4    | List, create, edit, delete categories                        |
| Posts CRUD              | ~5    | List, create, edit, delete posts with category assignment     |
| Slug Validation         | ~2    | Auto-generation, uniqueness enforcement, manual override      |
| Category Delete Restrict| ~1    | Prevent deletion of category with associated posts            |
| Validation              | ~2    | Required fields on category and post forms                   |
| Pagination              | ~1    | Blog posts list pagination                                   |

**Key Assertions:**
- Blog categories support full CRUD operations
- Blog posts display title, content, author, category, and publication date
- Slug is auto-generated from title and must be unique
- Category with associated posts cannot be deleted (restriction enforced)
- Post creation assigns the logged-in user as author
- Required fields are enforced on both category and post forms

---

## 8. Global Setup

The following `psql` commands seed the test users required by all modules. These must be executed before any test suite runs.

> **Note:** Passwords are stored as BCrypt hashes. The hash below corresponds to the plaintext password `password`.

```sql
-- Global Setup: Seed test users
-- Execute via: psql postgresql://cms_user:cms_password@localhost:5432/cms_db -c "<SQL>"

-- Clean existing test users (idempotent)
DELETE FROM users WHERE email IN ('admin@simplecms.com', 'editor@simplecms.com', 'user@simplecms.com');

-- Insert test users with BCrypt-hashed password ('password')
INSERT INTO users (email, password, role, created_at, updated_at)
VALUES
  ('admin@simplecms.com',  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN',  NOW(), NOW()),
  ('editor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EDITOR', NOW(), NOW()),
  ('user@simplecms.com',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER',   NOW(), NOW());
```

---

## 9. Global Teardown

Teardown removes all test data in reverse dependency order to avoid foreign key violations. Execute after all test suites have completed.

```sql
-- Global Teardown: Remove all test data
-- Execute via: psql postgresql://cms_user:cms_password@localhost:5432/cms_db -c "<SQL>"

-- L2: Blog (depends on User via author FK) - clean first
DELETE FROM blog_posts WHERE author_id IN (SELECT id FROM users WHERE email IN ('admin@simplecms.com', 'editor@simplecms.com', 'user@simplecms.com'));
DELETE FROM blog_categories;

-- L2: Contact Section
DELETE FROM contact_responses;
DELETE FROM contact_messages;
DELETE FROM contact_info;

-- L2: Team Section
DELETE FROM team_members;

-- L2: Testimonials Section
DELETE FROM testimonials;

-- L2: Features Section
DELETE FROM features;

-- L2: Product and Service Section
DELETE FROM product_services;

-- L2: Hero Section
DELETE FROM hero_sections;

-- L1: Authentication and Authorization
DELETE FROM password_reset_tokens WHERE user_id IN (SELECT id FROM users WHERE email IN ('admin@simplecms.com', 'editor@simplecms.com', 'user@simplecms.com'));

-- L1: User (clean last - other tables may reference users)
DELETE FROM users WHERE email IN ('admin@simplecms.com', 'editor@simplecms.com', 'user@simplecms.com');
```

---

## 10. Total Scenario Summary

| #  | Module                             | Layer              | Estimated Scenarios |
|----|------------------------------------|--------------------|---------------------|
| 1  | User                               | L1: Auth           | ~18                 |
| 2  | Authentication and Authorization   | L1: Auth           | ~8                  |
| 3  | Hero Section                       | L2: Reference Data | ~10                 |
| 4  | Product and Service Section        | L2: Reference Data | ~12                 |
| 5  | Features Section                   | L2: Reference Data | ~10                 |
| 6  | Testimonials Section               | L2: Reference Data | ~10                 |
| 7  | Team Section                       | L2: Reference Data | ~10                 |
| 8  | Contact Section                    | L2: Reference Data | ~12                 |
| 9  | Blog                               | L2: Reference Data | ~16                 |
|    | **Total**                          |                    | **~106**            |
