# Test Specification: Features Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Features Section                                                      |
| Module Prefix      | FTS                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages feature highlight content for the landing page. Features section content is marketing material managed by editors, representing business-level concerns rather than system infrastructure. It depends on L1 modules for authentication and has no cross-module data dependencies.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000048  | User Story | Create/update features section content with icon, title, description | v1.0.0  |
| USA000051  | User Story | Set display order of features content                              | v1.0.0  |
| USA000054  | User Story | Delete features section content                                    | v1.0.0  |
| USA000057  | User Story | View list of features content with status filter                   | v1.0.0  |
| NFRA00060  | NFR        | Title max 100 chars                                                | v1.0.0  |
| NFRA00063  | NFR        | Description max 500 chars                                          | v1.0.0  |
| NFRA00066  | NFR        | List ordered by display order ascending, then created date         | v1.0.0  |
| CONSA0018  | Constraint | Status values: DRAFT (default), INACTIVE, ACTIVE                   | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `features` table exists with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated.
- FontAwesome icon library is available in the application.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed an editor user (if not already seeded)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u4000000-0000-0000-0000-000000000001', 'ftseditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FTS', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 12 features records with various statuses and display orders
INSERT INTO features (id, icon, title, description, display_order, status, version, created_at, created_by)
VALUES
  ('f1000000-0000-0000-0000-000000000001', 'fa-solid fa-bolt', 'Lightning Fast', 'Our platform delivers blazing fast performance with sub-second load times, ensuring your visitors never have to wait.', 1, 'ACTIVE', 0, '2026-03-01 09:00:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000002', 'fa-solid fa-shield-halved', 'Enterprise Security', 'Bank-grade encryption and multi-layered security protocols protect your data and your customers around the clock.', 2, 'ACTIVE', 0, '2026-03-01 09:15:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000003', 'fa-solid fa-cloud', 'Cloud Native', 'Built from the ground up for the cloud with auto-scaling, redundancy, and global distribution capabilities.', 3, 'ACTIVE', 0, '2026-03-01 09:30:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000004', 'fa-solid fa-headset', '24/7 Support', 'Our dedicated support team is available around the clock to help you resolve any issue quickly and efficiently.', 4, 'ACTIVE', 0, '2026-03-01 09:45:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000005', 'fa-solid fa-chart-line', 'Analytics Dashboard', 'Comprehensive real-time analytics and reporting tools to help you make data-driven business decisions.', 5, 'DRAFT', 0, '2026-03-02 10:00:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000006', 'fa-solid fa-puzzle-piece', 'Easy Integration', 'Seamlessly integrate with your existing tools and workflows through our extensive API and plugin ecosystem.', 6, 'DRAFT', 0, '2026-03-02 10:15:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000007', 'fa-solid fa-mobile-screen', 'Mobile Optimized', 'Fully responsive design that provides a seamless experience across all devices from desktop to mobile.', 7, 'ACTIVE', 0, '2026-03-03 11:00:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000008', 'fa-solid fa-database', 'Reliable Backups', 'Automated daily backups with point-in-time recovery ensure your data is always safe and recoverable.', 8, 'INACTIVE', 0, '2026-03-03 11:15:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000009', 'fa-solid fa-users', 'Team Collaboration', 'Built-in collaboration features allow your team to work together efficiently with role-based access control.', 9, 'ACTIVE', 0, '2026-03-04 08:00:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000010', 'fa-solid fa-gear', 'Customizable', 'Highly customizable platform that adapts to your unique business requirements without complex coding.', 10, 'DRAFT', 0, '2026-03-04 08:15:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000011', 'fa-solid fa-earth-americas', 'Global CDN', 'Content delivery network spanning 50+ countries ensures your content loads fast for users worldwide.', 11, 'INACTIVE', 0, '2026-03-05 09:00:00', 'editor'),
  ('f1000000-0000-0000-0000-000000000012', 'fa-solid fa-wand-magic-sparkles', 'AI-Powered Insights', 'Machine learning algorithms analyze your data patterns and provide actionable recommendations automatically.', 12, 'ACTIVE', 0, '2026-03-05 09:15:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-FTS-001: Navigate to Features Section List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-FTS-001                                                           |
| Title            | Navigate to the features section content list page                    |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000057                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Features" link in the sidebar navigation. | The features section list page is displayed.                         |
| 2    | Verify the page shows content in a card grid layout (4 columns). | Cards are displayed in a 4-column grid layout.                      |
| 3    | Verify each card shows: icon, title, description, status, edit link, and delete link. | All card elements are visible.                                      |
| 4    | Verify the list is ordered by display order ascending. | Items appear in ascending order: 1, 2, 3, etc.                     |

---

### 6.2 Search/Filter Tests

#### SRCH-FTS-001: Filter by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-FTS-001                                                          |
| Title            | Filter features list by status                                        |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000057                                                             |
| Preconditions    | Seeded data with DRAFT, INACTIVE, and ACTIVE statuses.                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the features list page.                 | All features are displayed.                                          |
| 2    | Select "ACTIVE" from the status filter.             | Only features with ACTIVE status are displayed (7 items).            |
| 3    | Select "DRAFT" from the status filter.              | Only features with DRAFT status are displayed (3 items).             |
| 4    | Select "INACTIVE" from the status filter.           | Only features with INACTIVE status are displayed (2 items).          |
| 5    | Clear the filter.                                   | All 12 features are displayed again.                                 |

---

### 6.3 CRUD Tests

#### CRUD-FTS-001: Create Feature

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-FTS-001                                                          |
| Title            | Create a new feature content                                          |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000048, NFRA00060, NFRA00063, CONSA0018                           |
| Preconditions    | User is logged in as EDITOR. Create form is accessible.               |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click the "Create Feature" button.                  | The create feature form is displayed.                                |
| 2    | Select "fa-solid fa-rocket" from the icon picker.   | Icon is selected and preview is shown.                               |
| 3    | Enter "Rapid Deployment" in the title field.        | Title is populated (16 chars, within 100 limit).                     |
| 4    | Enter "Deploy your applications in minutes with our one-click deployment pipeline and rollback support." in the description field. | Description is populated (within 500 limit).                        |
| 5    | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 6    | Click "Save".                                       | Success message displayed. New feature appears in the list.          |

---

#### CRUD-FTS-002: Edit Feature

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-FTS-002                                                          |
| Title            | Edit an existing feature content                                      |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000048                                                             |
| Preconditions    | Seeded feature "Analytics Dashboard" (DRAFT) exists.                  |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "Analytics Dashboard" card.     | Edit form is displayed with pre-populated data.                      |
| 2    | Change the title to "Advanced Analytics Dashboard". | Title is updated.                                                    |
| 3    | Change status from "DRAFT" to "ACTIVE".             | Status is updated.                                                   |
| 4    | Click "Save".                                       | Success message displayed. List shows updated title and status.      |

---

#### CRUD-FTS-003: Delete Feature with Confirmation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-FTS-003                                                          |
| Title            | Delete feature content with confirmation prompt                       |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000054                                                             |
| Preconditions    | Seeded feature "Global CDN" (INACTIVE) exists.                        |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Global CDN" card.            | A confirmation dialog is displayed.                                  |
| 2    | Click "Cancel".                                     | Dialog closes. Item remains in the list.                             |
| 3    | Click "Delete" again and confirm.                   | Success message displayed. Item is removed from the list.            |
| 4    | Verify deletion in the database.                    | No record with title "Global CDN" in `features` table.              |

---

#### CRUD-FTS-004: Reorder Features

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-FTS-004                                                          |
| Title            | Set display order of features content                                 |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000051, NFRA00066                                                  |
| Preconditions    | Multiple seeded features exist with display orders 1-12.              |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the features list page.                 | Items are ordered by display order ascending.                        |
| 2    | Edit "Enterprise Security" (order 2) and change display order to 1. | Display order is updated.                                           |
| 3    | Edit "Lightning Fast" (order 1) and change display order to 2. | Display order is updated.                                           |
| 4    | Refresh the list page.                              | "Enterprise Security" appears before "Lightning Fast".               |

---

### 6.4 Validation Tests

#### VAL-FTS-001: Required Fields and Title Max Length

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-FTS-001                                                           |
| Title            | Validate required fields and title max 100 characters                 |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000048, NFRA00060, NFRA00063                                      |
| Preconditions    | Create feature form is displayed.                                     |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty and click "Save".            | Validation errors for icon, title, and description are displayed.    |
| 2    | Select an icon, enter a title with 101 characters.  | The 101st character is blocked or a validation error is shown.       |
| 3    | Enter a title with exactly 100 characters.          | Input is accepted without errors.                                    |
| 4    | Enter a description with 501 characters.            | Validation error for description max length is displayed.            |
| 5    | Enter a description with exactly 500 characters.    | Input is accepted without errors.                                    |

---

### 6.5 Pagination Tests

#### PAGE-FTS-001: Features List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-FTS-001                                                          |
| Title            | Features list supports pagination                                     |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000057                                                             |
| Preconditions    | 12 features are seeded (enough for at least 2 pages).                 |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the features list page.                 | First page of features is displayed.                                 |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | Remaining features are displayed.                                    |
| 4    | Click "Previous" or page 1.                         | First page is displayed again.                                       |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded features
DELETE FROM features WHERE id IN (
  'f1000000-0000-0000-0000-000000000001',
  'f1000000-0000-0000-0000-000000000002',
  'f1000000-0000-0000-0000-000000000003',
  'f1000000-0000-0000-0000-000000000004',
  'f1000000-0000-0000-0000-000000000005',
  'f1000000-0000-0000-0000-000000000006',
  'f1000000-0000-0000-0000-000000000007',
  'f1000000-0000-0000-0000-000000000008',
  'f1000000-0000-0000-0000-000000000009',
  'f1000000-0000-0000-0000-000000000010',
  'f1000000-0000-0000-0000-000000000011',
  'f1000000-0000-0000-0000-000000000012'
);

-- Delete any features created during tests
DELETE FROM features WHERE title = 'Rapid Deployment';

-- Delete seeded editor user
DELETE FROM users WHERE id = 'u4000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000048 | USA000051 | USA000054 | USA000057 | NFRA00060 | NFRA00063 | NFRA00066 | CONSA0018 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-FTS-001    | Navigation    |           |           |           | X         |           |           | X         |           |
| SRCH-FTS-001   | Search/Filter |           |           |           | X         |           |           |           | X         |
| CRUD-FTS-001   | CRUD (Create) | X         |           |           |           | X         | X         |           | X         |
| CRUD-FTS-002   | CRUD (Update) | X         |           |           |           |           |           |           | X         |
| CRUD-FTS-003   | CRUD (Delete) |           |           | X         |           |           |           |           |           |
| CRUD-FTS-004   | CRUD (Update) |           | X         |           |           |           |           | X         |           |
| VAL-FTS-001    | Validation    | X         |           |           |           | X         | X         |           |           |
| PAGE-FTS-001   | Pagination    |           |           |           | X         |           |           |           |           |
