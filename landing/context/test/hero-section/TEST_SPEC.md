# TEST_SPEC: Hero Section

**Application**: Landing Page (SCMS)
**Module**: Hero Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the hero section of the marketing page. Displays a carousel with multiple slides containing background images, headlines, subheadlines, and call-to-action buttons.

### Layer Classification Reasoning

L2 (Reference Data): Content is managed by Admin Portal and displayed read-only on the landing page. No authentication, no message queue triggers, no auto-generated data.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000003 | v1.0.0 |
| NFR | NFRL00003 | v1.0.0 |
| NFR | NFRL00006 | v1.0.0 |
| Constraint | CONSL0006 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/hero-section/model.md` | Found |
| Specification | `landing/context/specification/hero-section/SPEC.md` | Found |
| Mockup | `landing/context/mockup/pages/home.html` | Found |

### Removed / Replaced

_None._

---

## 2. Prerequisites

| Prerequisite | Module | Layer | How to Verify |
|-------------|--------|-------|--------------|
| PostgreSQL running | — | Infra | `pg_isready -h localhost -p 5432` |
| Landing page app running | — | Infra | `curl -s http://localhost:8000/ | head -1` |

---

## 3. Data Seeding

### 3a. Seeding Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
INSERT INTO hrs_hero_section (id, image_path, headline, subheadline, cta_url, cta_text, effective_date, expiration_date, status, version, created_at, created_by, updated_at, updated_by) VALUES
  ('a1000001-0000-0000-0000-000000000001', '/test/hero1.jpg', 'Build Your Online Presence', 'Create stunning marketing pages in minutes with Simple CMS.', 'https://example.com/get-started', 'Get Started', '2026-01-01 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('a1000001-0000-0000-0000-000000000002', '/test/hero2.jpg', 'Engage Your Audience', 'Publish blog content that drives traffic and builds trust.', 'https://example.com/learn-more', 'Learn More', '2026-01-01 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('a1000001-0000-0000-0000-000000000003', '/test/hero3.jpg', 'Grow Your Business', 'Everything you need to market your small business online.', 'https://example.com/trial', 'Start Free Trial', '2026-01-01 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('a1000001-0000-0000-0000-000000000004', '/test/hero-expired.jpg', 'Expired Slide', 'This should not be visible.', 'https://example.com/expired', 'Expired', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'EXPIRED', 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `hrs_hero_section` | 4 (3 active + 1 expired) | headline, cta_url, effective_date, expiration_date, status | "Build Your Online Presence", ACTIVE, 2026-01-01 to 2027-12-31 |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-HRS-001: Verify hero carousel renders active slides

- **Source**: USL000003 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 3 active + 1 expired hero slides seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Locate the hero section (`#hero`)
  3. Wait for the first slide to render
- **Expected**:
  - Hero section is visible at the top of the page
  - First slide shows headline "Build Your Online Presence"
  - First slide shows subheadline text
  - CTA button is visible with text "Get Started"
  - Expired slide ("Expired Slide") is NOT visible at any point
- **Selectors** (from mockup):
  - Hero section: `#hero`
  - Headline: `h1` within `#hero`
  - CTA button: `a` with class containing `bg-primary` within `#hero`

### 4b. Carousel Tests

#### CAR-HRS-001: Carousel auto-advances every 5 seconds

- **Source**: NFRL00003 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 3 active hero slides seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Note the current headline text (slide 1)
  3. Wait 5500ms (5s auto-slide + 500ms buffer)
  4. Check the headline text again
- **Expected**:
  - Headline text has changed from slide 1 to slide 2
  - "Build Your Online Presence" → "Engage Your Audience"

#### CAR-HRS-002: Manual navigation with arrow buttons

- **Source**: NFRL00003 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 3 active hero slides seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Verify first slide is showing ("Build Your Online Presence")
  3. Click the right arrow button (next)
  4. Verify second slide is showing ("Engage Your Audience")
  5. Click the left arrow button (previous)
  6. Verify first slide is showing again
  7. Click dot indicator for slide 3
  8. Verify third slide is showing ("Grow Your Business")
- **Expected**:
  - Right arrow advances to next slide
  - Left arrow goes back to previous slide
  - Dot indicators navigate to specific slides
  - Transitions include fade animation
- **Selectors** (from mockup):
  - Right arrow: `button[aria-label="Next slide"]`
  - Left arrow: `button[aria-label="Previous slide"]`
  - Dot indicators: `button[aria-label^="Go to slide"]`

### 4c. Constraint Tests

#### CONS-HRS-001: No header/footer in hero section

- **Source**: CONSL0006 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Inspect the `#hero` section
- **Expected**:
  - `#hero` section contains only: image, headline, subheadline, CTA button, navigation arrows, dot indicators
  - No `<header>` or `<footer>` elements within `#hero`

---

## 5. Data Cleanup

### 5a. Cleanup Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM hrs_hero_section WHERE created_by = 'test-seed';
"
```

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-HRS-001 | USL000003 | v1.0.0 | NFRL00006 | — | View |
| CAR-HRS-001 | USL000003 | v1.0.0 | NFRL00003 | — | Carousel |
| CAR-HRS-002 | USL000003 | v1.0.0 | NFRL00003 | — | Carousel |
| CONS-HRS-001 | — | v1.0.0 | — | CONSL0006 | Constraint |
