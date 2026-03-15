# Test Specification: Product and Service Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Product and Service Section                                           |
| Module Prefix      | PAS                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages marketing content for the product and service showcase section of the landing page. It is a business content management function performed by editors. It depends on L1 modules (authentication/user) for access control but has no dependencies from other business modules.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000036  | User Story | Create/update product and service content                          | v1.0.0  |
| USA000039  | User Story | Set display order of product and service content                   | v1.0.0  |
| USA000042  | User Story | Delete product and service content                                 | v1.0.0  |
| USA000045  | User Story | View list of product and service content with filters              | v1.0.0  |
| NFRA00039  | NFR        | Image must be exactly 400x400 pixels                               | v1.0.0  |
| NFRA00042  | NFR        | Title max 100 chars                                                | v1.0.0  |
| NFRA00045  | NFR        | Description max 500 chars                                          | v1.0.0  |
| NFRA00048  | NFR        | CTA URL must be valid URL format (if provided)                     | v1.0.0  |
| NFRA00051  | NFR        | CTA text max 50 chars (if provided)                                | v1.0.0  |
| NFRA00054  | NFR        | List ordered by display order ascending, then created date         | v1.0.0  |
| NFRA00057  | NFR        | Thumbnail auto-generated at 200x200 pixels                         | v1.0.0  |
| CONSA0015  | Constraint | Status values: DRAFT (default), INACTIVE, ACTIVE                   | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `product_services` table exists with the schema defined in the model.
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
  ('u3000000-0000-0000-0000-000000000001', 'paseditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'PAS', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 12 product_services records with various statuses and display orders
INSERT INTO product_services (id, image_path, thumbnail_path, title, description, cta_url, cta_text, display_order, status, version, created_at, created_by)
VALUES
  ('p1000000-0000-0000-0000-000000000001', '/images/products/web-design.jpg', '/images/products/thumb/web-design.jpg', 'Web Design', 'Professional web design services tailored to your brand identity. Responsive layouts that look great on all devices.', 'https://example.com/web-design', 'Get Started', 1, 'ACTIVE', 0, '2026-03-01 09:00:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000002', '/images/products/seo.jpg', '/images/products/thumb/seo.jpg', 'SEO Optimization', 'Boost your search rankings with our proven SEO strategies. Data-driven approach to organic traffic growth.', 'https://example.com/seo', 'Learn More', 2, 'ACTIVE', 0, '2026-03-01 09:15:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000003', '/images/products/branding.jpg', '/images/products/thumb/branding.jpg', 'Brand Identity', 'Complete branding solutions from logo design to brand guidelines. Make your business stand out from the crowd.', 'https://example.com/branding', 'Explore', 3, 'ACTIVE', 0, '2026-03-01 09:30:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000004', '/images/products/mobile-app.jpg', '/images/products/thumb/mobile-app.jpg', 'Mobile App Development', 'Native and cross-platform mobile applications built with the latest technologies for iOS and Android.', 'https://example.com/mobile', 'Contact Us', 4, 'ACTIVE', 0, '2026-03-01 09:45:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000005', '/images/products/cloud.jpg', '/images/products/thumb/cloud.jpg', 'Cloud Hosting', 'Reliable and scalable cloud hosting solutions with 99.9% uptime guarantee and 24/7 support.', 'https://example.com/cloud', 'View Plans', 5, 'DRAFT', 0, '2026-03-02 10:00:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000006', '/images/products/consulting.jpg', '/images/products/thumb/consulting.jpg', 'IT Consulting', 'Expert technology consulting to help you make informed decisions about your IT infrastructure.', NULL, NULL, 6, 'DRAFT', 0, '2026-03-02 10:15:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000007', '/images/products/ecommerce.jpg', '/images/products/thumb/ecommerce.jpg', 'E-Commerce Solutions', 'End-to-end e-commerce platform development with payment gateway integration and inventory management.', 'https://example.com/ecommerce', 'Start Selling', 7, 'ACTIVE', 0, '2026-03-03 11:00:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000008', '/images/products/social-media.jpg', '/images/products/thumb/social-media.jpg', 'Social Media Marketing', 'Strategic social media management to grow your audience and engage customers across all platforms.', 'https://example.com/social', 'Boost Now', 8, 'INACTIVE', 0, '2026-03-03 11:15:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000009', '/images/products/analytics.jpg', '/images/products/thumb/analytics.jpg', 'Data Analytics', 'Transform your data into actionable insights with our advanced analytics and reporting tools.', 'https://example.com/analytics', 'Analyze', 9, 'ACTIVE', 0, '2026-03-04 08:00:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000010', '/images/products/security.jpg', '/images/products/thumb/security.jpg', 'Cybersecurity', 'Protect your business with comprehensive cybersecurity solutions including penetration testing.', 'https://example.com/security', 'Secure Now', 10, 'DRAFT', 0, '2026-03-04 08:15:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000011', '/images/products/content.jpg', '/images/products/thumb/content.jpg', 'Content Creation', 'Professional copywriting and content creation services to tell your story and engage your audience.', NULL, NULL, 11, 'INACTIVE', 0, '2026-03-05 09:00:00', 'editor'),
  ('p1000000-0000-0000-0000-000000000012', '/images/products/support.jpg', '/images/products/thumb/support.jpg', 'Technical Support', 'Round-the-clock technical support services to keep your systems running smoothly and efficiently.', 'https://example.com/support', 'Get Help', 12, 'ACTIVE', 0, '2026-03-05 09:15:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-PAS-001: Navigate to Product and Service List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-PAS-001                                                           |
| Title            | Navigate to the product and service content list page                 |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000045                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Products & Services" link in the sidebar. | The product and service list page is displayed.                      |
| 2    | Verify the page shows content in a card grid layout (4 columns). | Cards are displayed in a 4-column grid layout.                      |
| 3    | Verify each card shows: thumbnail, title, description, status, edit link, and delete link. | All card elements are visible.                                      |
| 4    | Verify the list is ordered by display order ascending. | Items appear in order 1, 2, 3, etc.                                 |

---

### 6.2 Search/Filter Tests

#### SRCH-PAS-001: Filter by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-PAS-001                                                          |
| Title            | Filter product and service list by status                             |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000045                                                             |
| Preconditions    | Seeded data with DRAFT, INACTIVE, and ACTIVE statuses.                |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the product and service list page.      | All items are displayed.                                             |
| 2    | Select "ACTIVE" from the status filter.             | Only items with ACTIVE status are displayed.                         |
| 3    | Select "DRAFT" from the status filter.              | Only items with DRAFT status are displayed (3 items).                |
| 4    | Select "INACTIVE" from the status filter.           | Only items with INACTIVE status are displayed (2 items).             |
| 5    | Clear the filter.                                   | All items are displayed again.                                       |

---

### 6.3 CRUD Tests

#### CRUD-PAS-001: Create Product and Service

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-PAS-001                                                          |
| Title            | Create a new product and service content                              |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000036, NFRA00039, NFRA00042, NFRA00045, NFRA00057, CONSA0015    |
| Preconditions    | User is logged in as EDITOR. A valid 400x400 pixel image is available. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Create Product/Service" button.              | The create form is displayed.                                        |
| 2    | Upload a 400x400 pixel image file.                  | Image is accepted and preview is shown.                              |
| 3    | Enter "AI Integration Services" in the title field. | Title is populated (23 chars, within 100 limit).                     |
| 4    | Enter "Leverage artificial intelligence to automate workflows and gain competitive advantage." in the description field. | Description is populated (within 500 limit).                        |
| 5    | Enter `https://example.com/ai` in the CTA URL field (optional). | URL field is populated.                                             |
| 6    | Enter "Discover AI" in the CTA text field (optional). | CTA text is populated.                                              |
| 7    | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 8    | Click "Save".                                       | Success message displayed. New item appears in the list.             |
| 9    | Verify thumbnail (200x200) was auto-generated.      | Database record has `thumbnail_path` populated.                      |

---

#### CRUD-PAS-002: Edit Product and Service

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-PAS-002                                                          |
| Title            | Edit an existing product and service content                          |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000036                                                             |
| Preconditions    | Seeded item "Cloud Hosting" (DRAFT) exists.                           |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "Cloud Hosting" card.           | Edit form is displayed with pre-populated data.                      |
| 2    | Change the title to "Premium Cloud Hosting".        | Title is updated.                                                    |
| 3    | Change status from "DRAFT" to "ACTIVE".             | Status is updated.                                                   |
| 4    | Click "Save".                                       | Success message displayed. List shows updated title and status.      |

---

#### CRUD-PAS-003: Delete Product and Service with Confirmation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-PAS-003                                                          |
| Title            | Delete product and service content with confirmation prompt           |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000042                                                             |
| Preconditions    | Seeded item "Cybersecurity" (DRAFT) exists.                           |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Cybersecurity" card.         | A confirmation dialog is displayed asking to confirm deletion.       |
| 2    | Click "Cancel" on the confirmation dialog.          | Dialog closes. Item still appears in the list.                       |
| 3    | Click "Delete" again and confirm the deletion.      | Success message displayed. Item is removed from the list.            |
| 4    | Verify the record is deleted from the database.     | No record exists with title "Cybersecurity" in `product_services`.   |

---

#### CRUD-PAS-004: Reorder Product and Service Content

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-PAS-004                                                          |
| Title            | Set display order of product and service content                      |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000039, NFRA00054                                                  |
| Preconditions    | Multiple seeded items exist with display orders 1-12.                 |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the product and service list.           | Items are ordered by display order ascending.                        |
| 2    | Edit "SEO Optimization" (order 2) and change display order to 1. | Display order is updated.                                           |
| 3    | Edit "Web Design" (order 1) and change display order to 2. | Display order is updated.                                           |
| 4    | Refresh the list page.                              | "SEO Optimization" appears before "Web Design" in the list.         |

---

### 6.4 Validation Tests

#### VAL-PAS-001: Required Fields Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-PAS-001                                                           |
| Title            | Create form validates required fields (image, title, description)     |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | USA000036, NFRA00042, NFRA00045                                      |
| Preconditions    | Create form is displayed.                                             |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Leave all fields empty and click "Save".            | Validation errors for image, title, and description are displayed.   |
| 2    | Fill in title and description but no image.         | Validation error for image remains.                                  |
| 3    | Note: CTA URL and CTA text are optional.            | No validation errors for empty CTA fields.                           |

---

#### VAL-PAS-002: Image Size Validation (400x400)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-PAS-002                                                           |
| Title            | Image upload must be exactly 400x400 pixels                           |
| Type             | Validation                                                            |
| Priority         | Critical                                                              |
| Source           | NFRA00039                                                             |
| Preconditions    | Create form is displayed. Incorrectly sized image is available.       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Upload a 1600x500 pixel image.                      | Validation error "Image must be exactly 400x400 pixels" is displayed.|
| 2    | Upload a 400x400 pixel image.                       | Image is accepted without errors.                                    |

---

### 6.5 Pagination Tests

#### PAGE-PAS-001: Product and Service List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-PAS-001                                                          |
| Title            | Product and service list supports pagination                          |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000045                                                             |
| Preconditions    | 12 items are seeded (enough for at least 2 pages in 4-column grid).   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the product and service list page.      | First page of items is displayed.                                    |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | The remaining items are displayed.                                   |
| 4    | Click "Previous" or page 1.                         | The first page is displayed again.                                   |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded product_services
DELETE FROM product_services WHERE id IN (
  'p1000000-0000-0000-0000-000000000001',
  'p1000000-0000-0000-0000-000000000002',
  'p1000000-0000-0000-0000-000000000003',
  'p1000000-0000-0000-0000-000000000004',
  'p1000000-0000-0000-0000-000000000005',
  'p1000000-0000-0000-0000-000000000006',
  'p1000000-0000-0000-0000-000000000007',
  'p1000000-0000-0000-0000-000000000008',
  'p1000000-0000-0000-0000-000000000009',
  'p1000000-0000-0000-0000-000000000010',
  'p1000000-0000-0000-0000-000000000011',
  'p1000000-0000-0000-0000-000000000012'
);

-- Delete any items created during tests
DELETE FROM product_services WHERE title = 'AI Integration Services';

-- Delete seeded editor user
DELETE FROM users WHERE id = 'u3000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000036 | USA000039 | USA000042 | USA000045 | NFRA00039 | NFRA00042 | NFRA00045 | NFRA00048 | NFRA00051 | NFRA00054 | NFRA00057 | CONSA0015 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-PAS-001    | Navigation    |           |           |           | X         |           |           |           |           |           | X         |           |           |
| SRCH-PAS-001   | Search/Filter |           |           |           | X         |           |           |           |           |           |           |           | X         |
| CRUD-PAS-001   | CRUD (Create) | X         |           |           |           | X         | X         | X         |           |           |           | X         | X         |
| CRUD-PAS-002   | CRUD (Update) | X         |           |           |           |           |           |           |           |           |           |           | X         |
| CRUD-PAS-003   | CRUD (Delete) |           |           | X         |           |           |           |           |           |           |           |           |           |
| CRUD-PAS-004   | CRUD (Update) |           | X         |           |           |           |           |           |           |           | X         |           |           |
| VAL-PAS-001    | Validation    | X         |           |           |           |           | X         | X         |           |           |           |           |           |
| VAL-PAS-002    | Validation    |           |           |           |           | X         |           |           |           |           |           |           |           |
| PAGE-PAS-001   | Pagination    |           |           |           | X         |           |           |           |           |           |           |           |           |
