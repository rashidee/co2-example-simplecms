# TEST_SPEC: Team Section

**Application**: Landing Page (SCMS)
**Module**: Team Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the team section of the marketing page. Displays team member profiles with photos, names, roles, and LinkedIn links.

### Layer Classification Reasoning

L2 (Reference Data): Content managed by Admin Portal, displayed read-only.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000015 | v1.0.0 |
| NFR | NFRL00051 | v1.0.0 |
| NFR | NFRL00054 | v1.0.0 |
| NFR | NFRL00057 | v1.0.0 |
| NFR | NFRL00060 | v1.0.0 |
| NFR | NFRL00063 | v1.0.0 |
| NFR | NFRL00066 | v1.0.0 |
| NFR | NFRL00069 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/team-section/model.md` | Found |
| Specification | `landing/context/specification/team-section/SPEC.md` | Found |
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
INSERT INTO tms_team_member (id, profile_picture_path, name, role, linkedin_url, display_order, version, created_at, created_by, updated_at, updated_by) VALUES
  ('e5000001-0000-0000-0000-000000000001', '/test/team1.jpg', 'Alex Johnson', 'Founder & CEO', 'https://linkedin.com/in/alex-johnson-test', 1, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('e5000001-0000-0000-0000-000000000002', '/test/team2.jpg', 'Samantha Kim', 'CTO', 'https://linkedin.com/in/samantha-kim-test', 2, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('e5000001-0000-0000-0000-000000000003', '/test/team3.jpg', 'Marcus Rivera', 'Lead Designer', 'https://linkedin.com/in/marcus-rivera-test', 3, 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('e5000001-0000-0000-0000-000000000004', '/test/team4.jpg', 'Olivia Wang', 'Marketing Manager', 'https://linkedin.com/in/olivia-wang-test', 4, 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `tms_team_member` | 4 | name, role, linkedin_url, display_order | "Alex Johnson", "Founder & CEO", display_order: 1 |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-TMS-001: Verify team section renders all members

- **Source**: USL000015 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 4 team members seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to `#team` section
  3. Count visible team member cards
- **Expected**:
  - Section heading is "Meet Our Team" (NFRL00051)
  - 4 team member cards visible
  - Cards ordered by display_order
  - Light gray background (NFRL00069)
- **Selectors** (from mockup):
  - Section: `#team`
  - Heading: `#team h2`
  - Card grid: `#team .grid`

### 4b. Layout Tests

#### LAY-TMS-001: 3-column desktop, 1-column mobile

- **Source**: NFRL00054 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. View at desktop viewport (1280x720): verify 3-column grid
  2. Resize to mobile (375x667): verify 1-column layout
- **Expected**:
  - Desktop: 3 columns
  - Mobile: 1 column
  - Cards have shadow effect (NFRL00069)

### 4c. Content Tests

#### CONT-TMS-001: Team member card shows photo, name, role, LinkedIn

- **Source**: USL000015 [v1.0.0], NFRL00057-NFRL00066 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Scroll to `#team`, inspect first card
- **Expected**:
  - Circular profile picture visible, 150x150px (NFRL00057) with `rounded-full` class
  - Name "Alex Johnson" in bold, at least 18px (NFRL00060)
  - Role "Founder & CEO" at least 16px (NFRL00063)
  - LinkedIn icon (SVG) visible, at least 24px (NFRL00066)
  - LinkedIn link opens in new tab (`target="_blank"`)
  - LinkedIn link href contains `linkedin.com/in/alex-johnson-test`
- **Selectors** (from mockup):
  - Profile image: `#team .grid > div:first-child img.rounded-full`
  - Name: `#team .grid > div:first-child h3`
  - Role: `#team .grid > div:first-child p`
  - LinkedIn link: `#team .grid > div:first-child a[aria-label]`

---

## 5. Data Cleanup

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM tms_team_member WHERE created_by = 'test-seed';
"
```

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-TMS-001 | USL000015 | v1.0.0 | NFRL00051, NFRL00069 | — | View |
| LAY-TMS-001 | — | v1.0.0 | NFRL00054, NFRL00069 | — | Layout |
| CONT-TMS-001 | USL000015 | v1.0.0 | NFRL00057, NFRL00060, NFRL00063, NFRL00066 | — | Content |
