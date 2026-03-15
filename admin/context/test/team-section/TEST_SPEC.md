# Test Specification: Team Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Team Section                                                          |
| Module Prefix      | TMS                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages team member profile content for the landing page. Team section content is marketing material that introduces team members to website visitors. It depends on L1 modules for authentication and has no cross-module data dependencies with other business modules.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000072  | User Story | Create/update team member content with photo, name, role, LinkedIn | v1.0.0  |
| USA000075  | User Story | Set display order of team members                                  | v1.0.0  |
| USA000078  | User Story | Delete team member content                                         | v1.0.0  |
| USA000081  | User Story | View list of team members with status filter                       | v1.0.0  |
| NFRA00081  | NFR        | Profile picture must be exactly 400x400 pixels (square)            | v1.0.0  |
| NFRA00084  | NFR        | Name max 100 chars                                                 | v1.0.0  |
| NFRA00087  | NFR        | Role max 100 chars                                                 | v1.0.0  |
| NFRA00090  | NFR        | LinkedIn URL must be valid URL format                              | v1.0.0  |
| NFRA00093  | NFR        | List ordered by display order ascending, then created date         | v1.0.0  |
| CONSA0024  | Constraint | Status values: DRAFT (default), INACTIVE, ACTIVE                   | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `team_members` table exists with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated.
- Test image files of 400x400 pixels and incorrect sizes are available for upload testing.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed an editor user (if not already seeded)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u6000000-0000-0000-0000-000000000001', 'tmseditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'TMS', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 12 team_members records with various statuses and display orders
INSERT INTO team_members (id, profile_picture_path, name, role, linkedin_url, display_order, status, version, created_at, created_by)
VALUES
  ('m1000000-0000-0000-0000-000000000001', '/images/team/john-doe.jpg', 'John Doe', 'Chief Executive Officer', 'https://linkedin.com/in/johndoe', 1, 'ACTIVE', 0, '2026-03-01 09:00:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000002', '/images/team/jane-smith.jpg', 'Jane Smith', 'Chief Technology Officer', 'https://linkedin.com/in/janesmith', 2, 'ACTIVE', 0, '2026-03-01 09:15:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000003', '/images/team/mike-johnson.jpg', 'Mike Johnson', 'VP of Engineering', 'https://linkedin.com/in/mikejohnson', 3, 'ACTIVE', 0, '2026-03-01 09:30:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000004', '/images/team/sarah-williams.jpg', 'Sarah Williams', 'Head of Design', 'https://linkedin.com/in/sarahwilliams', 4, 'ACTIVE', 0, '2026-03-01 09:45:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000005', '/images/team/david-brown.jpg', 'David Brown', 'Lead Developer', 'https://linkedin.com/in/davidbrown', 5, 'DRAFT', 0, '2026-03-02 10:00:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000006', '/images/team/emily-davis.jpg', 'Emily Davis', 'Marketing Director', 'https://linkedin.com/in/emilydavis', 6, 'DRAFT', 0, '2026-03-02 10:15:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000007', '/images/team/chris-wilson.jpg', 'Chris Wilson', 'Senior Backend Engineer', 'https://linkedin.com/in/chriswilson', 7, 'ACTIVE', 0, '2026-03-03 11:00:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000008', '/images/team/lisa-taylor.jpg', 'Lisa Taylor', 'UX Researcher', 'https://linkedin.com/in/lisataylor', 8, 'INACTIVE', 0, '2026-03-03 11:15:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000009', '/images/team/robert-anderson.jpg', 'Robert Anderson', 'DevOps Engineer', 'https://linkedin.com/in/robertanderson', 9, 'ACTIVE', 0, '2026-03-04 08:00:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000010', '/images/team/amanda-thomas.jpg', 'Amanda Thomas', 'Product Manager', 'https://linkedin.com/in/amandathomas', 10, 'DRAFT', 0, '2026-03-04 08:15:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000011', '/images/team/kevin-jackson.jpg', 'Kevin Jackson', 'QA Lead', 'https://linkedin.com/in/kevinjackson', 11, 'INACTIVE', 0, '2026-03-05 09:00:00', 'editor'),
  ('m1000000-0000-0000-0000-000000000012', '/images/team/rachel-white.jpg', 'Rachel White', 'Data Scientist', 'https://linkedin.com/in/rachelwhite', 12, 'ACTIVE', 0, '2026-03-05 09:15:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-TMS-001: Navigate to Team Section List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-TMS-001                                                           |
| Title            | Navigate to the team section content list page                        |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000081                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Team" link in the sidebar navigation.     | The team members list page is displayed.                             |
| 2    | Verify the page shows content in a card grid layout (4 columns). | Cards are displayed in a 4-column grid.                             |
| 3    | Verify each card shows: profile picture, name, role, LinkedIn link, status, edit link, and delete link. | All card elements are visible.                                      |
| 4    | Verify the list is ordered by display order ascending. | Items appear in ascending order.                                    |

---

### 6.2 Search/Filter Tests

#### SRCH-TMS-001: Filter by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-TMS-001                                                          |
| Title            | Filter team members list by status                                    |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000081                                                             |
| Preconditions    | Seeded data with DRAFT, INACTIVE, and ACTIVE statuses.                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the team members list page.             | All team members are displayed.                                      |
| 2    | Select "ACTIVE" from the status filter.             | Only members with ACTIVE status are displayed (7 members).           |
| 3    | Select "DRAFT" from the status filter.              | Only members with DRAFT status are displayed (3 members).            |
| 4    | Select "INACTIVE" from the status filter.           | Only members with INACTIVE status are displayed (2 members).         |
| 5    | Clear the filter.                                   | All 12 team members are displayed.                                   |

---

### 6.3 CRUD Tests

#### CRUD-TMS-001: Create Team Member

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TMS-001                                                          |
| Title            | Create a new team member                                              |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000072, NFRA00081, NFRA00084, NFRA00087, NFRA00090, CONSA0024    |
| Preconditions    | User is logged in as EDITOR. A valid 400x400 pixel image is available. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Create Team Member" button.                  | The create team member form is displayed.                            |
| 2    | Upload a 400x400 pixel profile picture.             | Image is accepted and preview is shown.                              |
| 3    | Enter "Natalie Cooper" in the name field.           | Name is populated (15 chars, within 100 limit).                      |
| 4    | Enter "Frontend Developer" in the role field.       | Role is populated (18 chars, within 100 limit).                      |
| 5    | Enter `https://linkedin.com/in/nataliecooper` in the LinkedIn URL field. | URL is populated with valid format.                                 |
| 6    | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 7    | Click "Save".                                       | Success message displayed. New member appears in the list.           |

---

#### CRUD-TMS-002: Edit Team Member

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TMS-002                                                          |
| Title            | Edit an existing team member                                          |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000072                                                             |
| Preconditions    | Seeded member "David Brown" (DRAFT) exists.                           |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "David Brown" card.             | Edit form is displayed with pre-populated data.                      |
| 2    | Change the role from "Lead Developer" to "Principal Engineer". | Role is updated.                                                    |
| 3    | Change status from "DRAFT" to "ACTIVE".             | Status is updated.                                                   |
| 4    | Click "Save".                                       | Success message displayed. List shows updated role and status.       |

---

#### CRUD-TMS-003: Delete Team Member with Confirmation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TMS-003                                                          |
| Title            | Delete team member with confirmation prompt                           |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000078                                                             |
| Preconditions    | Seeded member "Kevin Jackson" (INACTIVE) exists.                      |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Kevin Jackson" card.         | A confirmation dialog is displayed.                                  |
| 2    | Click "Cancel".                                     | Dialog closes. Item remains in the list.                             |
| 3    | Click "Delete" again and confirm.                   | Success message displayed. Item is removed from the list.            |
| 4    | Verify deletion in the database.                    | No record with name "Kevin Jackson" in `team_members` table.        |

---

#### CRUD-TMS-004: Reorder Team Members

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TMS-004                                                          |
| Title            | Set display order of team members                                     |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000075, NFRA00093                                                  |
| Preconditions    | Multiple seeded members exist with display orders 1-12.               |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the team members list.                  | Items are ordered by display order ascending.                        |
| 2    | Edit "Jane Smith" (order 2) and change display order to 1. | Display order is updated.                                           |
| 3    | Edit "John Doe" (order 1) and change display order to 2. | Display order is updated.                                           |
| 4    | Refresh the list page.                              | "Jane Smith" appears before "John Doe".                              |

---

### 6.4 Validation Tests

#### VAL-TMS-001: Image Size, Name/Role Max Length, and LinkedIn URL Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-TMS-001                                                           |
| Title            | Validate image 400x400, name max 100, role max 100, LinkedIn URL format |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000072, NFRA00081, NFRA00084, NFRA00087, NFRA00090               |
| Preconditions    | Create team member form is displayed.                                 |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty and click "Save".            | Validation errors for profile picture, name, role, and LinkedIn URL. |
| 2    | Upload an image of 800x600 pixels.                  | Validation error "Image must be exactly 400x400 pixels" is displayed.|
| 3    | Upload a valid 400x400 pixel image.                 | Image is accepted.                                                   |
| 4    | Enter a name with 101 characters.                   | Validation error for name max length is displayed.                   |
| 5    | Enter a name with exactly 100 characters.           | Input is accepted.                                                   |
| 6    | Enter a role with 101 characters.                   | Validation error for role max length is displayed.                   |
| 7    | Enter a role with exactly 100 characters.           | Input is accepted.                                                   |
| 8    | Enter `not-a-valid-url` in the LinkedIn URL field.  | Validation error "Please enter a valid URL" is displayed.            |
| 9    | Enter `https://linkedin.com/in/validprofile` in the LinkedIn URL field. | Input is accepted.                                                  |

---

### 6.5 Pagination Tests

#### PAGE-TMS-001: Team Members List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-TMS-001                                                          |
| Title            | Team members list supports pagination                                 |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000081                                                             |
| Preconditions    | 12 team members are seeded (enough for at least 2 pages).             |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the team members list page.             | First page of team members is displayed.                             |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | Remaining team members are displayed.                                |
| 4    | Click "Previous" or page 1.                         | First page is displayed again.                                       |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded team members
DELETE FROM team_members WHERE id IN (
  'm1000000-0000-0000-0000-000000000001',
  'm1000000-0000-0000-0000-000000000002',
  'm1000000-0000-0000-0000-000000000003',
  'm1000000-0000-0000-0000-000000000004',
  'm1000000-0000-0000-0000-000000000005',
  'm1000000-0000-0000-0000-000000000006',
  'm1000000-0000-0000-0000-000000000007',
  'm1000000-0000-0000-0000-000000000008',
  'm1000000-0000-0000-0000-000000000009',
  'm1000000-0000-0000-0000-000000000010',
  'm1000000-0000-0000-0000-000000000011',
  'm1000000-0000-0000-0000-000000000012'
);

-- Delete any team members created during tests
DELETE FROM team_members WHERE name = 'Natalie Cooper';

-- Delete seeded editor user
DELETE FROM users WHERE id = 'u6000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000072 | USA000075 | USA000078 | USA000081 | NFRA00081 | NFRA00084 | NFRA00087 | NFRA00090 | NFRA00093 | CONSA0024 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-TMS-001    | Navigation    |           |           |           | X         |           |           |           |           | X         |           |
| SRCH-TMS-001   | Search/Filter |           |           |           | X         |           |           |           |           |           | X         |
| CRUD-TMS-001   | CRUD (Create) | X         |           |           |           | X         | X         | X         | X         |           | X         |
| CRUD-TMS-002   | CRUD (Update) | X         |           |           |           |           |           |           |           |           | X         |
| CRUD-TMS-003   | CRUD (Delete) |           |           | X         |           |           |           |           |           |           |           |
| CRUD-TMS-004   | CRUD (Update) |           | X         |           |           |           |           |           |           | X         |           |
| VAL-TMS-001    | Validation    | X         |           |           |           | X         | X         | X         | X         |           |           |
| PAGE-TMS-001   | Pagination    |           |           |           | X         |           |           |           |           |           |           |
