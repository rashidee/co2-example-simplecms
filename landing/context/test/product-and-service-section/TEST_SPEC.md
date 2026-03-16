# TEST_SPEC: Product and Service Section

**Application**: Landing Page (SCMS)
**Module**: Product and Service Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the product and service section of the marketing page. Displays products and services in a card grid layout.

### Layer Classification Reasoning

L2 (Reference Data): Content managed by Admin Portal, displayed read-only. No auth, no MQ, no auto-generated data.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000006 | v1.0.0 |
| NFR | NFRL00009 | v1.0.0 |
| NFR | NFRL00012 | v1.0.0 |
| NFR | NFRL00015 | v1.0.0 |
| NFR | NFRL00018 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/product-and-service-section/model.md` | Found |
| Specification | `landing/context/specification/product-and-service-section/SPEC.md` | Found |
| Mockup | `landing/context/mockup/pages/home.html` | Found |

### Removed / Replaced

_None._

---

## 2. Prerequisites

| Prerequisite | Module | Layer | How to Verify |
|-------------|--------|-------|--------------|
| PostgreSQL running | — | Infra | `pg_isready -h localhost -p 5432` |
| Landing page app running | — | Infra | `curl -s http://localhost:8000/` |

---

## 3. Data Seeding

### 3a. Seeding Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
INSERT INTO pas_product_service (id, image_path, title, description, cta_url, cta_text, display_order, version, created_at, created_by, updated_at, updated_by) VALUES
  ('b2000001-0000-0000-0000-000000000001', '/test/product1.jpg', 'Website Builder', 'Drag-and-drop page builder to create beautiful marketing pages without any coding knowledge.', 'https://example.com/products/website-builder', 'Learn More', 1, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('b2000001-0000-0000-0000-000000000002', '/test/product2.jpg', 'Blog Platform', 'Full-featured blog engine with categories, tags, comments and search to engage your audience.', 'https://example.com/products/blog-platform', 'Learn More', 2, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('b2000001-0000-0000-0000-000000000003', '/test/product3.jpg', 'SEO Tools', 'Built-in SEO optimization tools to help your pages rank higher on search engines.', 'https://example.com/products/seo-tools', 'Learn More', 3, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('b2000001-0000-0000-0000-000000000004', '/test/product4.jpg', 'Analytics Dashboard', 'Track visitor behaviour, page views and conversions with a real-time analytics dashboard.', 'https://example.com/products/analytics', 'Learn More', 4, 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `pas_product_service` | 4 | title, description, cta_url, display_order | "Website Builder", display_order: 1 |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-PAS-001: Verify products/services section renders all items

- **Source**: USL000006 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 4 product/service items seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to the `#products` section
  3. Count the visible product cards
- **Expected**:
  - Section heading is "Our Products and Services" (NFRL00009)
  - 4 product cards are visible
  - Each card contains: image, title, description, CTA link
  - Cards ordered by display_order (Website Builder first, Analytics Dashboard last)
- **Selectors** (from mockup):
  - Section: `#products`
  - Section heading: `#products h2`
  - Card grid: `#products .grid`
  - Individual cards: `#products .grid > div` or `.rounded-card`

### 4b. Layout Tests

#### LAY-PAS-001: 3-column desktop, 1-column mobile layout

- **Source**: NFRL00012 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/` at desktop viewport (1280x720)
  2. Verify grid is 3 columns wide
  3. Resize viewport to mobile (375x667)
  4. Verify grid is 1 column wide
- **Expected**:
  - Desktop: 3 cards per row (`grid-cols-3`)
  - Mobile: 1 card per row (`grid-cols-1`)
  - White background for section (NFRL00018)
  - Cards have shadow effect (NFRL00018)

### 4c. Content Tests

#### CONT-PAS-001: Product card contains all required elements

- **Source**: USL000006 [v1.0.0], NFRL00015 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to `#products` section
  3. Inspect the first product card
- **Expected**:
  - Image is present and visible
  - Title "Website Builder" is displayed in bold
  - Description text is visible
  - CTA link "Learn More" is present
  - CTA link opens in new tab (`target="_blank"`)
- **Selectors** (from mockup):
  - Card image: `#products .grid > div:first-child img`
  - Card title: `#products .grid > div:first-child h3`
  - Card description: `#products .grid > div:first-child p`
  - CTA link: `#products .grid > div:first-child a`

---

## 5. Data Cleanup

### 5a. Cleanup Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM pas_product_service WHERE created_by = 'test-seed';
"
```

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-PAS-001 | USL000006 | v1.0.0 | NFRL00009 | — | View |
| LAY-PAS-001 | — | v1.0.0 | NFRL00012, NFRL00018 | — | Layout |
| CONT-PAS-001 | USL000006 | v1.0.0 | NFRL00015 | — | Content |
