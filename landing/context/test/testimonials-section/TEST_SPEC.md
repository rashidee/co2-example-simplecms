# TEST_SPEC: Testimonials Section

**Application**: Landing Page (SCMS)
**Module**: Testimonials Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the testimonials section of the marketing page. Displays customer reviews and ratings in a carousel layout.

### Layer Classification Reasoning

L2 (Reference Data): Content managed by Admin Portal, displayed read-only.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000012 | v1.0.0 |
| NFR | NFRL00033 | v1.0.0 |
| NFR | NFRL00036 | v1.0.0 |
| NFR | NFRL00039 | v1.0.0 |
| NFR | NFRL00042 | v1.0.0 |
| NFR | NFRL00045 | v1.0.0 |
| NFR | NFRL00048 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/testimonials-section/model.md` | Found |
| Specification | `landing/context/specification/testimonials-section/SPEC.md` | Found |
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
INSERT INTO tst_testimonial (id, customer_name, customer_review, customer_rating, display_order, version, created_at, created_by, updated_at, updated_by) VALUES
  ('d4000001-0000-0000-0000-000000000001', 'Sarah Johnson', 'Simple CMS transformed our online presence. We went from having no website to a professional marketing page in just one afternoon.', 5, 1, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('d4000001-0000-0000-0000-000000000002', 'Michael Chen', 'The blog platform is fantastic. Our organic traffic increased by 150% within three months of publishing regular content.', 5, 2, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('d4000001-0000-0000-0000-000000000003', 'Emily Rodriguez', 'As a small bakery owner, I needed something simple and affordable. Simple CMS delivered on both fronts.', 4, 3, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('d4000001-0000-0000-0000-000000000004', 'David Thompson', 'The customer support team is exceptional. They helped me migrate my old website content and set everything up.', 5, 4, 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `tst_testimonial` | 4 | customer_name, customer_review, customer_rating, display_order | "Sarah Johnson", rating: 5, display_order: 1 |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-TST-001: Verify testimonials section renders

- **Source**: USL000012 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 4 testimonials seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to the `#testimonials` section
- **Expected**:
  - Section heading is "What Our Customers Say" (NFRL00033)
  - First testimonial is visible (Sarah Johnson)
  - Star rating shows 5 filled stars
  - Review text is displayed in quotes
  - Customer name is displayed below review
  - White background (NFRL00048)
- **Selectors** (from mockup):
  - Section: `#testimonials`
  - Heading: `#testimonials h2`

### 4b. Carousel Tests

#### CAR-TST-001: Navigate between testimonials with arrows

- **Source**: NFRL00036 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to home page, scroll to `#testimonials`
  2. Note current customer name (Sarah Johnson)
  3. Click right arrow (next)
  4. Verify customer name changed to Michael Chen
  5. Click left arrow (previous)
  6. Verify customer name returned to Sarah Johnson
- **Expected**:
  - 1 testimonial per slide (NFRL00036)
  - Arrow navigation works
  - Dot indicators update to reflect current slide
  - Transition animation plays
- **Selectors** (from mockup):
  - Right arrow: `button[aria-label="Next testimonial"]`
  - Left arrow: `button[aria-label="Previous testimonial"]`

### 4c. Content Tests

#### CONT-TST-001: Star rating and text formatting

- **Source**: USL000012 [v1.0.0], NFRL00039 [v1.0.0], NFRL00042 [v1.0.0], NFRL00045 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to home page, scroll to `#testimonials`
  2. Inspect the first testimonial slide
- **Expected**:
  - Star rating: 5 filled star icons (gold/warning color), each at least 24px (NFRL00045)
  - Customer name: bold, at least 18px font size (NFRL00039)
  - Review text: regular font, at least 16px (NFRL00042)
  3. Click next to navigate to Emily Rodriguez (4 stars)
  4. Verify only 4 filled stars, 1 empty star

---

## 5. Data Cleanup

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM tst_testimonial WHERE created_by = 'test-seed';
"
```

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-TST-001 | USL000012 | v1.0.0 | NFRL00033, NFRL00048 | — | View |
| CAR-TST-001 | USL000012 | v1.0.0 | NFRL00036 | — | Carousel |
| CONT-TST-001 | USL000012 | v1.0.0 | NFRL00039, NFRL00042, NFRL00045 | — | Content |
