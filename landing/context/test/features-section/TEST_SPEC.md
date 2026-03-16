# TEST_SPEC: Features Section

**Application**: Landing Page (SCMS)
**Module**: Features Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the features section of the marketing page. Displays key features and benefits in a grid layout with icons.

### Layer Classification Reasoning

L2 (Reference Data): Content managed by Admin Portal, displayed read-only. No auth, no MQ, no auto-generated data.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000009 | v1.0.0 |
| NFR | NFRL00021 | v1.0.0 |
| NFR | NFRL00024 | v1.0.0 |
| NFR | NFRL00027 | v1.0.0 |
| NFR | NFRL00030 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/features-section/model.md` | Found |
| Specification | `landing/context/specification/features-section/SPEC.md` | Found |
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
INSERT INTO fts_feature (id, icon, title, description, display_order, version, created_at, created_by, updated_at, updated_by) VALUES
  ('c3000001-0000-0000-0000-000000000001', 'fa-solid fa-globe', 'Responsive Design', 'Every page you create looks great on desktops, tablets and mobile devices out of the box.', 1, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('c3000001-0000-0000-0000-000000000002', 'fa-solid fa-lock', 'Secure & Reliable', 'Enterprise-grade security with SSL, CAPTCHA and regular backups to keep your data safe.', 2, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('c3000001-0000-0000-0000-000000000003', 'fa-solid fa-bolt', 'Lightning Fast', 'Optimised for speed with efficient caching and minimal page load times for the best user experience.', 3, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('c3000001-0000-0000-0000-000000000004', 'fa-solid fa-paint-brush', 'Easy Customisation', 'Customise colours, fonts and layouts without code. Make your brand stand out with full creative control.', 4, 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `fts_feature` | 4 | icon, title, description, display_order | "fa-solid fa-globe", "Responsive Design", display_order: 1 |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-FTS-001: Verify features section renders all items

- **Source**: USL000009 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 4 features seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to the `#features` section
  3. Count visible feature cards
- **Expected**:
  - Section heading is "Key Features and Benefits" (NFRL00021)
  - 4 feature cards visible
  - Each card shows: icon, title, description
  - Cards ordered by display_order
- **Selectors** (from mockup):
  - Section: `#features`
  - Heading: `#features h2`
  - Card grid: `#features .grid`

### 4b. Layout Tests

#### LAY-FTS-001: 3-column desktop, 1-column mobile

- **Source**: NFRL00024 [v1.0.0], NFRL00030 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to home page at desktop viewport (1280x720)
  2. Scroll to `#features` and verify 3-column grid
  3. Resize to mobile (375x667)
  4. Verify 1-column layout
- **Expected**:
  - Desktop: 3 columns
  - Mobile: 1 column
  - Light gray background (`bg-page-bg`)
  - Cards have shadow effect

### 4c. Content Tests

#### CONT-FTS-001: Feature card shows icon, title, description

- **Source**: USL000009 [v1.0.0], NFRL00027 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to home page, scroll to `#features`
  2. Inspect first feature card
- **Expected**:
  - Icon element is present (FontAwesome `fa-solid fa-globe` or SVG icon)
  - Icon is at least 48px in size (NFRL00027)
  - Title "Responsive Design" displayed in bold
  - Description text visible
  - Text centered within card

---

## 5. Data Cleanup

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM fts_feature WHERE created_by = 'test-seed';
"
```

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-FTS-001 | USL000009 | v1.0.0 | NFRL00021 | — | View |
| LAY-FTS-001 | — | v1.0.0 | NFRL00024, NFRL00030 | — | Layout |
| CONT-FTS-001 | USL000009 | v1.0.0 | NFRL00027 | — | Content |
