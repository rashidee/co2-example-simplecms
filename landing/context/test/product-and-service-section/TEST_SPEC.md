# Test Specification: Product and Service Section

| Field | Value |
|-------|-------|
| Module | Product and Service Section |
| Prefix | PAS |
| Layer | L2 (Reference Data) |
| Source Stories | USL000006 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Product and Service Section displays a grid of cards showcasing the company's products and services. Each card contains an image, title, short description, and an optional CTA link. Only items with `status = ACTIVE` are displayed, ordered by `display_order` ascending. The layout uses a 3-column grid on desktop and a single column on mobile.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `product_services` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000006 | v1.0.0 |
| Data Model | `landing/context/model/product-and-service-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/product-and-service-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Product & Service Section: 4 ACTIVE + 1 INACTIVE (should not display)
INSERT INTO product_services (id, image_path, title, description, cta_url, cta_text, display_order, status, created_at, updated_at) VALUES
('b2000001-0000-0000-0000-000000000001', '/images/products/web-dev.jpg', 'Web Development', 'Custom web applications built with modern technologies to meet your business needs.', 'https://example.com/web-dev', 'Learn More', 1, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000002', '/images/products/mobile-app.jpg', 'Mobile Applications', 'Native and cross-platform mobile apps for iOS and Android devices.', 'https://example.com/mobile', 'Learn More', 2, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000003', '/images/products/cloud.jpg', 'Cloud Solutions', 'Scalable cloud infrastructure and migration services for enterprises.', 'https://example.com/cloud', 'Learn More', 3, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000004', '/images/products/consulting.jpg', 'IT Consulting', 'Expert technology consulting to align your IT strategy with business goals.', 'https://example.com/consulting', 'Learn More', 4, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000005', '/images/products/legacy.jpg', 'Legacy System Support', 'This service is temporarily unavailable.', NULL, NULL, 5, 'INACTIVE', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Title | Status | Display Order | Expected Display |
|-------------|-------|--------|---------------|------------------|
| ...0001 | Web Development | ACTIVE | 1 | Yes |
| ...0002 | Mobile Applications | ACTIVE | 2 | Yes |
| ...0003 | Cloud Solutions | ACTIVE | 3 | Yes |
| ...0004 | IT Consulting | ACTIVE | 4 | Yes |
| ...0005 | Legacy System Support | INACTIVE | 5 | No |

---

## 6. Test Scenarios

### NAV-PAS-001: Product and Service section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-PAS-001 |
| Title | Product and Service section is visible on the landing page |
| Priority | High |
| Source Story | USL000006 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Product and Service section. | The section is visible with the title "Our Products and Services". |

---

### VIEW-PAS-001: Product cards display image, title, and description

| Field | Value |
|-------|-------|
| ID | VIEW-PAS-001 |
| Title | Product cards display image, title, and description |
| Priority | High |
| Source Story | USL000006 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Product and Service section. | 4 product/service cards are displayed. |
| 3 | Inspect the first card. | The card shows an image, the title "Web Development", and the description "Custom web applications built with modern technologies to meet your business needs." |
| 4 | Inspect each of the remaining cards. | Each card displays its respective image, title, and description text. |

---

### VIEW-PAS-002: Cards are displayed in a 3-column grid on desktop

| Field | Value |
|-------|-------|
| ID | VIEW-PAS-002 |
| Title | Cards are displayed in a 3-column grid on desktop |
| Priority | Medium |
| Source Story | NFRL00012 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/` on a desktop viewport (width >= 1024px). | The landing page loads successfully. |
| 2 | Scroll to the Product and Service section. | The product/service cards are arranged in a grid layout with 3 columns. The first row contains 3 cards, and the second row contains the 4th card. |

---

### VIEW-PAS-003: CTA links open in a new tab

| Field | Value |
|-------|-------|
| ID | VIEW-PAS-003 |
| Title | CTA links open in a new tab |
| Priority | High |
| Source Story | USL000006 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Product and Service section. | Product/service cards with CTA links are displayed. |
| 3 | Inspect the CTA link on the "Web Development" card. | The link element has `target="_blank"` attribute and `href` pointing to `https://example.com/web-dev`. |
| 4 | Click the CTA link. | A new browser tab opens with the target URL. |

---

### VAL-PAS-001: Inactive products are not displayed

| Field | Value |
|-------|-------|
| ID | VAL-PAS-001 |
| Title | Inactive products are not displayed |
| Priority | High |
| Source Story | USL000006 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Product and Service section. | Only 4 product/service cards are displayed. |
| 3 | Verify content of all displayed cards. | The title "Legacy System Support" (INACTIVE status) does NOT appear anywhere in the section. |

---

## 7. Cleanup Script

```sql
DELETE FROM product_services WHERE id IN (
    'b2000001-0000-0000-0000-000000000001',
    'b2000001-0000-0000-0000-000000000002',
    'b2000001-0000-0000-0000-000000000003',
    'b2000001-0000-0000-0000-000000000004',
    'b2000001-0000-0000-0000-000000000005'
);
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-PAS-001 | USL000006 | NFRL00009 | — | ProductService |
| VIEW-PAS-001 | USL000006 | — | — | ProductService |
| VIEW-PAS-002 | USL000006 | NFRL00012 | — | ProductService |
| VIEW-PAS-003 | USL000006 | — | — | ProductService |
| VAL-PAS-001 | USL000006 | — | — | ProductService |
