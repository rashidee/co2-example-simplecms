# Test Specification: Testimonials Section

| Field | Value |
|-------|-------|
| Module | Testimonials Section |
| Prefix | TST |
| Layer | L2 (Reference Data) |
| Source Stories | USL000012 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Testimonials Section displays a carousel of customer reviews and ratings. Each testimonial includes the customer name, review text, and a star rating (1-5). The carousel displays one testimonial per slide. Only items with `status = ACTIVE` are displayed, ordered by `display_order` ascending.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `testimonials` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000012 | v1.0.0 |
| Data Model | `landing/context/model/testimonials-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/testimonials-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Testimonials Section: 5 ACTIVE testimonials with varying ratings
INSERT INTO testimonials (id, customer_name, customer_review, customer_rating, display_order, status, created_at, updated_at) VALUES
('d4000001-0000-0000-0000-000000000001', 'Sarah Johnson', 'Absolutely transformed our business operations. The platform is intuitive and powerful.', 5, 1, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000002', 'Michael Chen', 'Great value for money. The support team is responsive and knowledgeable.', 4, 2, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000003', 'Emily Rodriguez', 'We migrated our entire infrastructure in just two weeks. Highly recommended.', 5, 3, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000004', 'David Kim', 'The analytics features alone made it worth switching from our previous provider.', 4, 4, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000005', 'Lisa Thompson', 'Good product overall. Some features could use more customization options.', 3, 5, 'ACTIVE', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Customer Name | Rating | Status | Display Order | Expected Display |
|-------------|---------------|--------|--------|---------------|------------------|
| ...0001 | Sarah Johnson | 5 | ACTIVE | 1 | Yes |
| ...0002 | Michael Chen | 4 | ACTIVE | 2 | Yes |
| ...0003 | Emily Rodriguez | 5 | ACTIVE | 3 | Yes |
| ...0004 | David Kim | 4 | ACTIVE | 4 | Yes |
| ...0005 | Lisa Thompson | 3 | ACTIVE | 5 | Yes |

---

## 6. Test Scenarios

### NAV-TST-001: Testimonials section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-TST-001 |
| Title | Testimonials section is visible on the landing page |
| Priority | High |
| Source Story | USL000012 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Testimonials section. | The section is visible with the title "What Our Customers Say". |

---

### VIEW-TST-001: Carousel displays testimonials one per slide

| Field | Value |
|-------|-------|
| ID | VIEW-TST-001 |
| Title | Carousel displays testimonials one per slide |
| Priority | High |
| Source Story | USL000012, NFRL00036 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Testimonials section. | The testimonials carousel is displayed showing one testimonial at a time. |
| 3 | Navigate through all slides. | A total of 5 testimonials are accessible via the carousel, each displayed individually on its own slide. |

---

### VIEW-TST-002: Testimonial shows customer name, review, and star rating

| Field | Value |
|-------|-------|
| ID | VIEW-TST-002 |
| Title | Testimonial shows customer name, review, and star rating |
| Priority | High |
| Source Story | USL000012 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Testimonials section and observe the first slide. | The first testimonial displays: customer name "Sarah Johnson" in bold (at least 18px), review text "Absolutely transformed our business operations. The platform is intuitive and powerful." (at least 16px), and a star rating visual. |
| 3 | Navigate to the second slide. | The testimonial for "Michael Chen" is displayed with the corresponding review text and star rating. |

---

### VIEW-TST-003: Star rating visual matches the numeric rating value

| Field | Value |
|-------|-------|
| ID | VIEW-TST-003 |
| Title | Star rating visual matches the numeric rating value |
| Priority | High |
| Source Story | USL000012, NFRL00045 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Testimonials section and observe the first slide (Sarah Johnson, rating: 5). | 5 filled star icons are displayed. Star icons are at least 24px in size. |
| 3 | Navigate to the second slide (Michael Chen, rating: 4). | 4 filled star icons and 1 empty/unfilled star icon are displayed. |
| 4 | Navigate to the fifth slide (Lisa Thompson, rating: 3). | 3 filled star icons and 2 empty/unfilled star icons are displayed. |

---

## 7. Cleanup Script

```sql
DELETE FROM testimonials WHERE id IN (
    'd4000001-0000-0000-0000-000000000001',
    'd4000001-0000-0000-0000-000000000002',
    'd4000001-0000-0000-0000-000000000003',
    'd4000001-0000-0000-0000-000000000004',
    'd4000001-0000-0000-0000-000000000005'
);
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-TST-001 | USL000012 | NFRL00033 | — | Testimonial |
| VIEW-TST-001 | USL000012 | NFRL00036 | — | Testimonial |
| VIEW-TST-002 | USL000012 | NFRL00039, NFRL00042 | — | Testimonial |
| VIEW-TST-003 | USL000012 | NFRL00045 | — | Testimonial |
