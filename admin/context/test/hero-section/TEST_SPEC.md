# Test Specification: Hero Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Hero Section                                                          |
| Module Prefix      | HRS                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages marketing content for the hero banner section of the landing page. Creating, editing, and listing hero section content is a business content management function performed by editors. It does not provide system-level infrastructure and depends on the L1 authentication/user modules for access control.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000030  | User Story | Create/update multiple hero section content                        | v1.0.0  |
| USA000033  | User Story | View list of hero section content with filters                     | v1.0.0  |
| NFRA00018  | NFR        | Expired hero content auto-changes status to EXPIRED                | v1.0.0  |
| NFRA00021  | NFR        | Image must be exactly 1600x500 pixels                              | v1.0.0  |
| NFRA00024  | NFR        | Headline max 100 chars, subheadline max 200 chars                  | v1.0.0  |
| NFRA00027  | NFR        | CTA URL must be valid URL format                                   | v1.0.0  |
| NFRA00030  | NFR        | CTA text max 50 chars                                              | v1.0.0  |
| NFRA00033  | NFR        | List ordered by effective date descending                          | v1.0.0  |
| NFRA00036  | NFR        | Thumbnail auto-generated at 400x125 pixels                         | v1.0.0  |
| CONSA0012  | Constraint | Status values: DRAFT (default), READY, ACTIVE, EXPIRED            | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `hero_sections` table exists with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated for content management operations.
- Test image files of 1600x500 pixels and incorrect sizes are available for upload testing.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed an editor user for hero section tests (if not already seeded by User module)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u2000000-0000-0000-0000-000000000001', 'heroeditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hero', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 12 hero_sections with various statuses and dates
INSERT INTO hero_sections (id, image_path, thumbnail_path, headline, subheadline, cta_url, cta_text, effective_date, expiration_date, status, version, created_at, created_by)
VALUES
  ('h1000000-0000-0000-0000-000000000001', '/images/hero/hero-spring-sale.jpg', '/images/hero/thumb/hero-spring-sale.jpg', 'Spring Sale - Up to 50% Off', 'Discover amazing deals on our entire spring collection. Limited time offer!', 'https://example.com/spring-sale', 'Shop Now', '2026-03-01 00:00:00', '2026-03-31 23:59:59', 'ACTIVE', 0, '2026-02-25 10:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000002', '/images/hero/hero-new-arrivals.jpg', '/images/hero/thumb/hero-new-arrivals.jpg', 'New Arrivals for 2026', 'Check out our latest products fresh off the production line.', 'https://example.com/new-arrivals', 'Explore', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 'READY', 0, '2026-02-28 14:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000003', '/images/hero/hero-summer-preview.jpg', '/images/hero/thumb/hero-summer-preview.jpg', 'Summer Preview Collection', 'Get a sneak peek at our upcoming summer lineup. Pre-orders available.', 'https://example.com/summer-preview', 'Pre-Order', '2026-06-01 00:00:00', '2026-08-31 23:59:59', 'DRAFT', 0, '2026-03-05 09:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000004', '/images/hero/hero-winter-clearance.jpg', '/images/hero/thumb/hero-winter-clearance.jpg', 'Winter Clearance Event', 'Final markdowns on all winter inventory. Everything must go!', 'https://example.com/winter-clearance', 'Shop Clearance', '2026-01-15 00:00:00', '2026-02-28 23:59:59', 'EXPIRED', 0, '2026-01-10 08:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000005', '/images/hero/hero-loyalty-program.jpg', '/images/hero/thumb/hero-loyalty-program.jpg', 'Join Our Loyalty Program', 'Earn points on every purchase and unlock exclusive rewards.', 'https://example.com/loyalty', 'Join Free', '2026-03-10 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 0, '2026-03-08 11:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000006', '/images/hero/hero-free-shipping.jpg', '/images/hero/thumb/hero-free-shipping.jpg', 'Free Shipping on Orders $50+', 'No minimum hassle. Fast and free delivery on qualifying orders.', 'https://example.com/shipping', 'Learn More', '2026-03-15 00:00:00', '2026-06-15 23:59:59', 'ACTIVE', 0, '2026-03-12 16:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000007', '/images/hero/hero-holiday-draft.jpg', '/images/hero/thumb/hero-holiday-draft.jpg', 'Holiday Gift Guide', 'Find the perfect gift for everyone on your list this holiday season.', 'https://example.com/gift-guide', 'Browse Gifts', '2026-11-15 00:00:00', '2026-12-31 23:59:59', 'DRAFT', 0, '2026-03-01 10:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000008', '/images/hero/hero-back-to-school.jpg', '/images/hero/thumb/hero-back-to-school.jpg', 'Back to School Essentials', 'Stock up on everything you need for a successful school year.', 'https://example.com/back-to-school', 'Shop Now', '2026-08-01 00:00:00', '2026-09-15 23:59:59', 'READY', 0, '2026-03-03 12:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000009', '/images/hero/hero-black-friday.jpg', '/images/hero/thumb/hero-black-friday.jpg', 'Black Friday Deals', 'Our biggest sale event of the year. Doorbusters start at midnight!', 'https://example.com/black-friday', 'See Deals', '2025-11-28 00:00:00', '2025-12-01 23:59:59', 'EXPIRED', 0, '2025-11-20 09:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000010', '/images/hero/hero-valentines.jpg', '/images/hero/thumb/hero-valentines.jpg', 'Valentine''s Day Specials', 'Show your love with our curated collection of gifts and treats.', 'https://example.com/valentines', 'Shop Love', '2026-02-01 00:00:00', '2026-02-15 23:59:59', 'EXPIRED', 0, '2026-01-25 14:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000011', '/images/hero/hero-earth-day.jpg', '/images/hero/thumb/hero-earth-day.jpg', 'Earth Day Sustainability Sale', 'Eco-friendly products at special prices. Save the planet while you shop.', 'https://example.com/earth-day', 'Go Green', '2026-04-15 00:00:00', '2026-04-30 23:59:59', 'DRAFT', 0, '2026-03-10 08:00:00', 'editor'),
  ('h1000000-0000-0000-0000-000000000012', '/images/hero/hero-flash-sale.jpg', '/images/hero/thumb/hero-flash-sale.jpg', 'Flash Sale - 24 Hours Only', 'Unbeatable prices for one day only. Don''t miss out on these steals!', 'https://example.com/flash-sale', 'Hurry!', '2026-03-20 00:00:00', '2026-03-20 23:59:59', 'READY', 0, '2026-03-14 17:00:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-HRS-001: Navigate to Hero Section List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-HRS-001                                                           |
| Title            | Navigate to the hero section content list page                        |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000033                                                             |
| Preconditions    | User is logged in as `heroeditor@simplecms.com` (EDITOR role).       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Hero Section" link in the sidebar navigation. | The hero section list page is displayed.                            |
| 2    | Verify the page shows hero content in a card grid layout (3 columns). | Cards are displayed in a 3-column grid layout.                      |
| 3    | Verify each card shows: thumbnail image, headline, subheadline, effective/expiration dates, status, and edit link. | All card elements are visible.                                      |
| 4    | Verify a "Create Hero Section" button is present.   | A button to create new hero content is visible.                      |
| 5    | Verify the list is ordered by effective date in descending order. | The most recent effective date appears first.                       |

---

### 6.2 Search/Filter Tests

#### SRCH-HRS-001: Filter by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-HRS-001                                                          |
| Title            | Filter hero section list by status                                    |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000033                                                             |
| Preconditions    | Seeded data with multiple statuses (DRAFT, READY, ACTIVE, EXPIRED).   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the hero section list page.             | All hero sections are displayed.                                     |
| 2    | Select "ACTIVE" from the status filter dropdown.    | Only hero sections with ACTIVE status are displayed.                 |
| 3    | Verify the displayed cards all show "ACTIVE" status. | All visible cards have status badge "ACTIVE".                       |
| 4    | Select "DRAFT" from the status filter dropdown.     | Only hero sections with DRAFT status are displayed.                  |
| 5    | Select "EXPIRED" from the status filter dropdown.   | Only hero sections with EXPIRED status are displayed.                |
| 6    | Clear the filter or select "All".                   | All hero sections are displayed again.                               |

---

#### SRCH-HRS-002: Filter by Date

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-HRS-002                                                          |
| Title            | Filter hero section list by effective and expiration dates            |
| Type             | Search/Filter                                                         |
| Priority         | Medium                                                                |
| Source           | USA000033                                                             |
| Preconditions    | Seeded data with various effective and expiration dates.               |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the hero section list page.             | All hero sections are displayed.                                     |
| 2    | Set the effective date filter to `2026-03-01`.      | Only hero sections with effective date on or after 2026-03-01 are shown. |
| 3    | Set the expiration date filter to `2026-06-30`.     | Only hero sections with expiration date on or before 2026-06-30 are shown. |
| 4    | Clear all date filters.                             | All hero sections are displayed again.                               |

---

### 6.3 CRUD Tests

#### CRUD-HRS-001: Create Hero Section

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-HRS-001                                                          |
| Title            | Create a new hero section content                                     |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000030, NFRA00021, NFRA00024, NFRA00027, NFRA00030, NFRA00036, CONSA0012 |
| Preconditions    | User is logged in as EDITOR. A valid 1600x500 pixel image file is available. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the hero section list page.             | Hero section list is displayed.                                      |
| 2    | Click the "Create Hero Section" button.             | The create hero section form is displayed.                           |
| 3    | Upload a 1600x500 pixel image file.                 | Image is accepted and preview is shown.                              |
| 4    | Enter "Grand Opening Celebration" in the headline field. | Headline field is populated (30 chars, within 100 limit).           |
| 5    | Enter "Join us for the grand opening of our newest location with exclusive deals and surprises." in the subheadline field. | Subheadline is populated (within 200 char limit).                   |
| 6    | Enter `https://example.com/grand-opening` in the CTA URL field. | URL field is populated with valid URL.                              |
| 7    | Enter "Celebrate" in the CTA text field.            | CTA text is populated (9 chars, within 50 limit).                   |
| 8    | Set effective date to `2026-04-01`.                 | Effective date is set.                                               |
| 9    | Set expiration date to `2026-04-30`.                | Expiration date is set.                                              |
| 10   | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 11   | Click the "Save" button.                            | A success message "Hero section created successfully" is displayed.  |
| 12   | Verify the new hero section appears in the list.    | Card for "Grand Opening Celebration" is visible in the list.         |
| 13   | Verify a thumbnail (400x125) was auto-generated.    | The card shows a thumbnail image. Database record has `thumbnail_path` populated. |

---

#### CRUD-HRS-002: Edit Hero Section

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-HRS-002                                                          |
| Title            | Edit an existing hero section content                                 |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000030                                                             |
| Preconditions    | Seeded hero section "Summer Preview Collection" (DRAFT) exists.       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the hero section list page.             | Hero section list is displayed.                                      |
| 2    | Click the "Edit" link on the "Summer Preview Collection" card. | The edit form is displayed with pre-populated data.                 |
| 3    | Change the headline to "Summer Collection Launch".  | Headline field is updated.                                           |
| 4    | Change the status from "DRAFT" to "READY".          | Status dropdown is updated.                                         |
| 5    | Click the "Save" button.                            | A success message "Hero section updated successfully" is displayed.  |
| 6    | Verify the changes in the list.                     | Card shows "Summer Collection Launch" with status "READY".           |

---

### 6.4 Validation Tests

#### VAL-HRS-001: Required Fields Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-HRS-001                                                           |
| Title            | Create hero section validates required fields                         |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000030                                                             |
| Preconditions    | Create hero section form is displayed.                                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty on the create form.          | All fields are empty.                                                |
| 2    | Click the "Save" button.                            | Validation errors are displayed for: image, headline, subheadline, CTA URL, CTA text, effective date, and expiration date. |
| 3    | Fill in only the headline and submit.               | Validation errors remain for all other required fields.              |

---

#### VAL-HRS-002: Image Size Validation (1600x500)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-HRS-002                                                           |
| Title            | Image upload must be exactly 1600x500 pixels                          |
| Type             | Validation                                                            |
| Priority         | Critical                                                              |
| Source           | NFRA00021                                                             |
| Preconditions    | Create hero section form is displayed. An image file of incorrect size (e.g., 800x300) is available. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Upload an image file that is 800x300 pixels.        | A validation error "Image must be exactly 1600x500 pixels" is displayed. |
| 2    | Upload an image file that is 1600x500 pixels.       | Image is accepted without validation errors.                         |

---

#### VAL-HRS-003: Headline Max Length Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-HRS-003                                                           |
| Title            | Headline text must not exceed 100 characters                          |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00024                                                             |
| Preconditions    | Create hero section form is displayed.                                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter a headline with exactly 101 characters.       | The 101st character is either blocked or a validation error is shown. |
| 2    | Enter a headline with exactly 100 characters.       | Input is accepted without validation errors.                         |

---

#### VAL-HRS-004: CTA URL Format Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-HRS-004                                                           |
| Title            | CTA URL must be a valid URL format                                    |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00027                                                             |
| Preconditions    | Create hero section form is displayed.                                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter `not-a-valid-url` in the CTA URL field.       | A validation error "Please enter a valid URL" is displayed.          |
| 2    | Enter `https://example.com/valid-page` in the CTA URL field. | Input is accepted without validation errors.                        |

---

### 6.5 Pagination Tests

#### PAGE-HRS-001: Hero Section List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-HRS-001                                                          |
| Title            | Hero section list supports pagination                                 |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000033                                                             |
| Preconditions    | 12 hero sections are seeded (enough for at least 2 pages).            |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the hero section list page.             | The first page of hero sections is displayed (e.g., 9 cards for 3-col grid). |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | The remaining hero sections are displayed.                           |
| 4    | Click "Previous" or page 1.                         | The first page is displayed again.                                   |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded hero sections
DELETE FROM hero_sections WHERE id IN (
  'h1000000-0000-0000-0000-000000000001',
  'h1000000-0000-0000-0000-000000000002',
  'h1000000-0000-0000-0000-000000000003',
  'h1000000-0000-0000-0000-000000000004',
  'h1000000-0000-0000-0000-000000000005',
  'h1000000-0000-0000-0000-000000000006',
  'h1000000-0000-0000-0000-000000000007',
  'h1000000-0000-0000-0000-000000000008',
  'h1000000-0000-0000-0000-000000000009',
  'h1000000-0000-0000-0000-000000000010',
  'h1000000-0000-0000-0000-000000000011',
  'h1000000-0000-0000-0000-000000000012'
);

-- Delete any hero sections created during tests
DELETE FROM hero_sections WHERE headline = 'Grand Opening Celebration';

-- Delete seeded editor user (if not shared with other tests)
DELETE FROM users WHERE id = 'u2000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000030 | USA000033 | NFRA00018 | NFRA00021 | NFRA00024 | NFRA00027 | NFRA00030 | NFRA00033 | NFRA00036 | CONSA0012 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-HRS-001    | Navigation    |           | X         |           |           |           |           |           | X         |           |           |
| SRCH-HRS-001   | Search/Filter |           | X         |           |           |           |           |           |           |           | X         |
| SRCH-HRS-002   | Search/Filter |           | X         |           |           |           |           |           |           |           |           |
| CRUD-HRS-001   | CRUD (Create) | X         |           |           | X         | X         | X         | X         |           | X         | X         |
| CRUD-HRS-002   | CRUD (Update) | X         |           |           |           |           |           |           |           |           | X         |
| VAL-HRS-001    | Validation    | X         |           |           |           |           |           |           |           |           |           |
| VAL-HRS-002    | Validation    |           |           |           | X         |           |           |           |           |           |           |
| VAL-HRS-003    | Validation    |           |           |           |           | X         |           |           |           |           |           |
| VAL-HRS-004    | Validation    |           |           |           |           |           | X         |           |           |           |           |
| PAGE-HRS-001   | Pagination    |           | X         |           |           |           |           |           |           |           |           |
