# Test Specification: Testimonials Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Testimonials Section                                                  |
| Module Prefix      | TST                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages customer testimonial content for the landing page. Testimonials are marketing content entered by editors to build social proof. It depends on L1 modules for authentication and has no cross-module data dependencies with other business modules.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000060  | User Story | Create/update testimonials with name, review, and rating           | v1.0.0  |
| USA000063  | User Story | Set display order of testimonials content                          | v1.0.0  |
| USA000066  | User Story | Delete testimonials content                                        | v1.0.0  |
| USA000069  | User Story | View list of testimonials with status filter                       | v1.0.0  |
| NFRA00069  | NFR        | Customer name max 100 chars                                        | v1.0.0  |
| NFRA00072  | NFR        | Customer review max 1000 chars                                     | v1.0.0  |
| NFRA00075  | NFR        | Customer rating must be integer between 1 and 5                    | v1.0.0  |
| NFRA00078  | NFR        | List ordered by display order ascending, then created date         | v1.0.0  |
| CONSA0021  | Constraint | Status values: DRAFT (default), INACTIVE, ACTIVE                   | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `testimonials` table exists with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed an editor user (if not already seeded)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u5000000-0000-0000-0000-000000000001', 'tsteditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'TST', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 12 testimonials records with various statuses, ratings, and display orders
INSERT INTO testimonials (id, customer_name, customer_review, customer_rating, display_order, status, version, created_at, created_by)
VALUES
  ('t1000000-0000-0000-0000-000000000001', 'Sarah Mitchell', 'Absolutely phenomenal service! The team went above and beyond to deliver our website on time and within budget. The design is stunning and our customers love it.', 5, 1, 'ACTIVE', 0, '2026-03-01 09:00:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000002', 'James Rodriguez', 'Great experience working with the team. They understood our vision perfectly and translated it into a beautiful digital presence for our brand.', 5, 2, 'ACTIVE', 0, '2026-03-01 09:15:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000003', 'Emily Chen', 'The SEO results speak for themselves. Within three months, our organic traffic increased by 150%. Highly recommend their services.', 4, 3, 'ACTIVE', 0, '2026-03-01 09:30:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000004', 'Michael Thompson', 'Professional and reliable. They delivered exactly what was promised and were always responsive to our questions and feedback throughout the process.', 4, 4, 'ACTIVE', 0, '2026-03-01 09:45:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000005', 'Lisa Anderson', 'The mobile app they built for us has received fantastic reviews from our users. Clean design and smooth performance on both iOS and Android.', 5, 5, 'DRAFT', 0, '2026-03-02 10:00:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000006', 'Robert Kim', 'Good work overall. The website looks great but the initial timeline was a bit optimistic. Communication could have been slightly better during the middle phase.', 3, 6, 'DRAFT', 0, '2026-03-02 10:15:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000007', 'Amanda Foster', 'Their cloud hosting has been rock-solid for us. Zero downtime in six months and their support team resolves tickets within hours, not days.', 5, 7, 'ACTIVE', 0, '2026-03-03 11:00:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000008', 'David Park', 'The analytics dashboard they implemented gives us real-time insights we never had before. It has completely changed how we make business decisions.', 4, 8, 'INACTIVE', 0, '2026-03-03 11:15:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000009', 'Jennifer White', 'We have been using their services for over two years now and the quality has been consistently excellent. They are a true long-term partner.', 5, 9, 'ACTIVE', 0, '2026-03-04 08:00:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000010', 'Christopher Lee', 'Decent service but nothing extraordinary. The end result met our requirements but I expected more creativity in the design proposals.', 3, 10, 'DRAFT', 0, '2026-03-04 08:15:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000011', 'Maria Garcia', 'The branding package they created for our startup was exactly what we needed. Professional, modern, and perfectly aligned with our target audience.', 4, 11, 'INACTIVE', 0, '2026-03-05 09:00:00', 'editor'),
  ('t1000000-0000-0000-0000-000000000012', 'Daniel Brown', 'Outstanding e-commerce solution! Sales increased by 40% in the first quarter after launch. The payment integration works flawlessly.', 5, 12, 'ACTIVE', 0, '2026-03-05 09:15:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-TST-001: Navigate to Testimonials Section List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-TST-001                                                           |
| Title            | Navigate to the testimonials section content list page                |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000069                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Testimonials" link in the sidebar navigation. | The testimonials list page is displayed.                            |
| 2    | Verify the page shows content in a card grid layout (4 columns). | Cards are displayed in a 4-column grid.                             |
| 3    | Verify each card shows: customer name, review, rating, status, edit link, and delete link. | All card elements are visible.                                      |
| 4    | Verify the list is ordered by display order ascending. | Items appear in ascending order.                                    |

---

### 6.2 Search/Filter Tests

#### SRCH-TST-001: Filter by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-TST-001                                                          |
| Title            | Filter testimonials list by status                                    |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000069                                                             |
| Preconditions    | Seeded data with DRAFT, INACTIVE, and ACTIVE statuses.                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the testimonials list page.             | All testimonials are displayed.                                      |
| 2    | Select "ACTIVE" from the status filter.             | Only testimonials with ACTIVE status are displayed (7 items).        |
| 3    | Select "DRAFT" from the status filter.              | Only testimonials with DRAFT status are displayed (3 items).         |
| 4    | Select "INACTIVE" from the status filter.           | Only testimonials with INACTIVE status are displayed (2 items).      |
| 5    | Clear the filter.                                   | All 12 testimonials are displayed.                                   |

---

### 6.3 CRUD Tests

#### CRUD-TST-001: Create Testimonial

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TST-001                                                          |
| Title            | Create a new testimonial content                                      |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000060, NFRA00069, NFRA00072, NFRA00075, CONSA0021               |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Create Testimonial" button.                  | The create testimonial form is displayed.                            |
| 2    | Enter "Patricia Wilson" in the customer name field.  | Name is populated (16 chars, within 100 limit).                     |
| 3    | Enter "Their technical support team is incredibly responsive and knowledgeable. Every issue we reported was resolved within hours." in the review field. | Review is populated (within 1000 limit).                            |
| 4    | Select rating of 4 stars.                           | Rating is set to 4.                                                  |
| 5    | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 6    | Click "Save".                                       | Success message displayed. New testimonial appears in the list.      |

---

#### CRUD-TST-002: Edit Testimonial

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TST-002                                                          |
| Title            | Edit an existing testimonial content                                  |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000060                                                             |
| Preconditions    | Seeded testimonial "Lisa Anderson" (DRAFT) exists.                    |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "Lisa Anderson" testimonial card. | Edit form is displayed with pre-populated data.                     |
| 2    | Change the rating from 5 to 4.                      | Rating is updated.                                                   |
| 3    | Change status from "DRAFT" to "ACTIVE".             | Status is updated.                                                   |
| 4    | Click "Save".                                       | Success message displayed. List shows updated rating and status.     |

---

#### CRUD-TST-003: Delete Testimonial with Confirmation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TST-003                                                          |
| Title            | Delete testimonial with confirmation prompt                           |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000066                                                             |
| Preconditions    | Seeded testimonial "Christopher Lee" (DRAFT) exists.                  |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Christopher Lee" card.       | A confirmation dialog is displayed.                                  |
| 2    | Click "Cancel".                                     | Dialog closes. Item remains in the list.                             |
| 3    | Click "Delete" again and confirm.                   | Success message displayed. Item is removed from the list.            |
| 4    | Verify deletion in the database.                    | No record with name "Christopher Lee" in `testimonials` table.      |

---

#### CRUD-TST-004: Reorder Testimonials

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-TST-004                                                          |
| Title            | Set display order of testimonials content                             |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000063, NFRA00078                                                  |
| Preconditions    | Multiple seeded testimonials exist with display orders 1-12.          |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the testimonials list.                  | Items are ordered by display order ascending.                        |
| 2    | Edit "Emily Chen" (order 3) and change display order to 1. | Display order is updated.                                           |
| 3    | Edit "Sarah Mitchell" (order 1) and change display order to 3. | Display order is updated.                                           |
| 4    | Refresh the list page.                              | "Emily Chen" appears before "Sarah Mitchell".                        |

---

### 6.4 Validation Tests

#### VAL-TST-001: Required Fields, Rating Range, and Max Length Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-TST-001                                                           |
| Title            | Validate required fields, rating 1-5, name max 100, review max 1000  |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000060, NFRA00069, NFRA00072, NFRA00075                           |
| Preconditions    | Create testimonial form is displayed.                                 |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty and click "Save".            | Validation errors for customer name, review, and rating.             |
| 2    | Enter a customer name with 101 characters.          | Validation error "Name must not exceed 100 characters" is displayed. |
| 3    | Enter a customer name with exactly 100 characters.  | Input is accepted.                                                   |
| 4    | Enter a review with 1001 characters.                | Validation error "Review must not exceed 1000 characters" is displayed. |
| 5    | Enter a review with exactly 1000 characters.        | Input is accepted.                                                   |
| 6    | Attempt to set rating to 0.                         | Validation error "Rating must be between 1 and 5" is displayed.      |
| 7    | Attempt to set rating to 6.                         | Validation error "Rating must be between 1 and 5" is displayed.      |
| 8    | Set rating to 3.                                    | Rating is accepted.                                                  |

---

### 6.5 Pagination Tests

#### PAGE-TST-001: Testimonials List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-TST-001                                                          |
| Title            | Testimonials list supports pagination                                 |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000069                                                             |
| Preconditions    | 12 testimonials are seeded (enough for at least 2 pages).             |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the testimonials list page.             | First page of testimonials is displayed.                             |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | Remaining testimonials are displayed.                                |
| 4    | Click "Previous" or page 1.                         | First page is displayed again.                                       |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded testimonials
DELETE FROM testimonials WHERE id IN (
  't1000000-0000-0000-0000-000000000001',
  't1000000-0000-0000-0000-000000000002',
  't1000000-0000-0000-0000-000000000003',
  't1000000-0000-0000-0000-000000000004',
  't1000000-0000-0000-0000-000000000005',
  't1000000-0000-0000-0000-000000000006',
  't1000000-0000-0000-0000-000000000007',
  't1000000-0000-0000-0000-000000000008',
  't1000000-0000-0000-0000-000000000009',
  't1000000-0000-0000-0000-000000000010',
  't1000000-0000-0000-0000-000000000011',
  't1000000-0000-0000-0000-000000000012'
);

-- Delete any testimonials created during tests
DELETE FROM testimonials WHERE customer_name = 'Patricia Wilson';

-- Delete seeded editor user
DELETE FROM users WHERE id = 'u5000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000060 | USA000063 | USA000066 | USA000069 | NFRA00069 | NFRA00072 | NFRA00075 | NFRA00078 | CONSA0021 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-TST-001    | Navigation    |           |           |           | X         |           |           |           | X         |           |
| SRCH-TST-001   | Search/Filter |           |           |           | X         |           |           |           |           | X         |
| CRUD-TST-001   | CRUD (Create) | X         |           |           |           | X         | X         | X         |           | X         |
| CRUD-TST-002   | CRUD (Update) | X         |           |           |           |           |           |           |           | X         |
| CRUD-TST-003   | CRUD (Delete) |           |           | X         |           |           |           |           |           |           |
| CRUD-TST-004   | CRUD (Update) |           | X         |           |           |           |           |           | X         |           |
| VAL-TST-001    | Validation    | X         |           |           |           | X         | X         | X         |           |           |
| PAGE-TST-001   | Pagination    |           |           |           | X         |           |           |           |           |           |
