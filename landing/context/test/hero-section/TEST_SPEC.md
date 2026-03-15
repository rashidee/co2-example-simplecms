# Test Specification: Hero Section

| Field | Value |
|-------|-------|
| Module | Hero Section |
| Prefix | HRS |
| Layer | L2 (Reference Data) |
| Source Stories | USL000003 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Hero Section displays a carousel of hero banners on the landing page. Each slide contains a background image, headline, sub-headline, and a call-to-action button. The carousel auto-slides every 5 seconds and supports manual arrow navigation. Only hero sections with `status = ACTIVE` and within their `effectiveDate`/`expirationDate` range are displayed.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `hero_sections` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000003 | v1.0.0 |
| Data Model | `landing/context/model/hero-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/hero-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Hero Section: 3 ACTIVE (valid dates) + 1 EXPIRED (should not display)
INSERT INTO hero_sections (id, image_path, headline, subheadline, cta_url, cta_text, effective_date, expiration_date, status, created_at, updated_at) VALUES
('a1000001-0000-0000-0000-000000000001', '/images/hero/slide-1.jpg', 'Innovate Your Business Today', 'Discover cutting-edge solutions that drive growth and efficiency for your company.', 'https://example.com/products', 'Explore Products', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000002', '/images/hero/slide-2.jpg', 'Trusted by Industry Leaders', 'Join thousands of businesses that rely on our services every day.', 'https://example.com/services', 'View Services', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000003', '/images/hero/slide-3.jpg', 'Start Your Free Trial', 'Experience the full power of our platform with a 30-day free trial.', 'https://example.com/trial', 'Start Trial', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000004', '/images/hero/slide-expired.jpg', 'Holiday Special Offer', 'This limited-time offer has ended.', 'https://example.com/holiday', 'Shop Now', '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'EXPIRED', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Headline | Status | Effective Date | Expiration Date | Expected Display |
|-------------|----------|--------|----------------|-----------------|------------------|
| ...0001 | Innovate Your Business Today | ACTIVE | 2026-01-01 | 2026-12-31 | Yes |
| ...0002 | Trusted by Industry Leaders | ACTIVE | 2026-01-01 | 2026-12-31 | Yes |
| ...0003 | Start Your Free Trial | ACTIVE | 2026-01-01 | 2026-12-31 | Yes |
| ...0004 | Holiday Special Offer | EXPIRED | 2025-12-01 | 2025-12-31 | No |

---

## 6. Test Scenarios

### NAV-HRS-001: Hero section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-HRS-001 |
| Title | Hero section is visible on the landing page |
| Priority | High |
| Source Story | USL000003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/` | The landing page loads successfully. |
| 2 | Observe the top area of the page (below the navigation bar). | The hero section is visible with a large background image, headline text, sub-headline text, and a CTA button. |

---

### VIEW-HRS-001: Carousel displays all active hero slides

| Field | Value |
|-------|-------|
| ID | VIEW-HRS-001 |
| Title | Carousel displays all active hero slides |
| Priority | High |
| Source Story | USL000003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The hero carousel is displayed. |
| 2 | Observe the first slide. | The first slide shows a background image, headline "Innovate Your Business Today", sub-headline text, and CTA button "Explore Products". |
| 3 | Navigate through all slides using arrow buttons. | A total of 3 slides are displayed, each with unique headline, sub-headline, background image, and CTA button. |

---

### VIEW-HRS-002: Carousel auto-slides every 5 seconds

| Field | Value |
|-------|-------|
| ID | VIEW-HRS-002 |
| Title | Carousel auto-slides every 5 seconds |
| Priority | Medium |
| Source Story | NFRL00003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The hero carousel is displayed on the first slide. |
| 2 | Wait 5 seconds without interacting with the carousel. | The carousel automatically transitions to the second slide. |
| 3 | Wait another 5 seconds. | The carousel automatically transitions to the third slide. |

---

### VIEW-HRS-003: Arrow navigation allows manual slide control

| Field | Value |
|-------|-------|
| ID | VIEW-HRS-003 |
| Title | Arrow navigation allows manual slide control |
| Priority | Medium |
| Source Story | NFRL00003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The hero carousel is displayed on the first slide. |
| 2 | Click the right/next arrow button. | The carousel transitions to the second slide with headline "Trusted by Industry Leaders". |
| 3 | Click the right/next arrow button again. | The carousel transitions to the third slide with headline "Start Your Free Trial". |
| 4 | Click the left/previous arrow button. | The carousel transitions back to the second slide. |

---

### VIEW-HRS-004: CTA button opens link in a new tab

| Field | Value |
|-------|-------|
| ID | VIEW-HRS-004 |
| Title | CTA button opens link in a new tab |
| Priority | High |
| Source Story | USL000003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The hero carousel is displayed. |
| 2 | Inspect the CTA button on the first slide ("Explore Products"). | The CTA button/link element has `target="_blank"` attribute and `href` pointing to `https://example.com/products`. |
| 3 | Click the CTA button. | A new browser tab opens with the URL `https://example.com/products`. |

---

### VAL-HRS-001: Expired hero section is not displayed

| Field | Value |
|-------|-------|
| ID | VAL-HRS-001 |
| Title | Expired hero section is not displayed |
| Priority | High |
| Source Story | USL000003 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The hero carousel is displayed. |
| 2 | Navigate through all carousel slides using arrow buttons. | Only 3 slides are present. The headline "Holiday Special Offer" (EXPIRED status) does NOT appear in any slide. |

---

## 7. Cleanup Script

```sql
DELETE FROM hero_sections WHERE id IN (
    'a1000001-0000-0000-0000-000000000001',
    'a1000001-0000-0000-0000-000000000002',
    'a1000001-0000-0000-0000-000000000003',
    'a1000001-0000-0000-0000-000000000004'
);
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-HRS-001 | USL000003 | — | CONSL0006 | HeroSection |
| VIEW-HRS-001 | USL000003 | — | — | HeroSection |
| VIEW-HRS-002 | USL000003 | NFRL00003 | — | HeroSection |
| VIEW-HRS-003 | USL000003 | NFRL00003 | — | HeroSection |
| VIEW-HRS-004 | USL000003 | — | — | HeroSection |
| VAL-HRS-001 | USL000003 | — | — | HeroSection |
