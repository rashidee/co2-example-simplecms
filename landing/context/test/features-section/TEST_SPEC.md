# Test Specification: Features Section

| Field | Value |
|-------|-------|
| Module | Features Section |
| Prefix | FTS |
| Layer | L2 (Reference Data) |
| Source Stories | USL000009 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Features Section displays a grid of key features and benefits. Each feature item includes an icon (FontAwesome), a title, and a short description. Only items with `status = ACTIVE` are displayed, ordered by `display_order` ascending. The layout uses a 3-column grid on desktop and a single column on mobile.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `features` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000009 | v1.0.0 |
| Data Model | `landing/context/model/features-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/features-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Features Section: 6 ACTIVE features
INSERT INTO features (id, icon, title, description, display_order, status, created_at, updated_at) VALUES
('c3000001-0000-0000-0000-000000000001', 'fa-solid fa-bolt', 'Lightning Fast Performance', 'Our platform is optimized for speed, ensuring your applications load in milliseconds.', 1, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000002', 'fa-solid fa-shield-halved', 'Enterprise Security', 'Bank-grade security with end-to-end encryption and compliance certifications.', 2, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000003', 'fa-solid fa-cloud', 'Cloud Native', 'Built for the cloud with auto-scaling, high availability, and disaster recovery.', 3, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000004', 'fa-solid fa-headset', '24/7 Support', 'Round-the-clock customer support via chat, email, and phone.', 4, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000005', 'fa-solid fa-chart-line', 'Advanced Analytics', 'Real-time dashboards and reporting to make data-driven decisions.', 5, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000006', 'fa-solid fa-puzzle-piece', 'Easy Integration', 'Seamless integration with popular tools and APIs you already use.', 6, 'ACTIVE', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Icon | Title | Status | Display Order | Expected Display |
|-------------|------|-------|--------|---------------|------------------|
| ...0001 | fa-solid fa-bolt | Lightning Fast Performance | ACTIVE | 1 | Yes |
| ...0002 | fa-solid fa-shield-halved | Enterprise Security | ACTIVE | 2 | Yes |
| ...0003 | fa-solid fa-cloud | Cloud Native | ACTIVE | 3 | Yes |
| ...0004 | fa-solid fa-headset | 24/7 Support | ACTIVE | 4 | Yes |
| ...0005 | fa-solid fa-chart-line | Advanced Analytics | ACTIVE | 5 | Yes |
| ...0006 | fa-solid fa-puzzle-piece | Easy Integration | ACTIVE | 6 | Yes |

---

## 6. Test Scenarios

### NAV-FTS-001: Features section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-FTS-001 |
| Title | Features section is visible on the landing page |
| Priority | High |
| Source Story | USL000009 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Features section. | The section is visible with the title "Key Features and Benefits". |

---

### VIEW-FTS-001: Each feature displays icon, title, and description

| Field | Value |
|-------|-------|
| ID | VIEW-FTS-001 |
| Title | Each feature displays icon, title, and description |
| Priority | High |
| Source Story | USL000009 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Features section. | 6 feature items are displayed. |
| 3 | Inspect the first feature item. | The item displays an icon (bolt icon), the title "Lightning Fast Performance", and the description "Our platform is optimized for speed, ensuring your applications load in milliseconds." |
| 4 | Inspect each of the remaining feature items. | Each item displays its respective icon, title, and description. Icons are rendered as FontAwesome icons with a size of at least 48px. |

---

### VIEW-FTS-002: Features are displayed in a 3-column grid on desktop

| Field | Value |
|-------|-------|
| ID | VIEW-FTS-002 |
| Title | Features are displayed in a 3-column grid on desktop |
| Priority | Medium |
| Source Story | NFRL00024 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/` on a desktop viewport (width >= 1024px). | The landing page loads successfully. |
| 2 | Scroll to the Features section. | The feature items are arranged in a grid layout with 3 columns. The first row contains 3 features, and the second row contains the remaining 3 features. |

---

## 7. Cleanup Script

```sql
DELETE FROM features WHERE id IN (
    'c3000001-0000-0000-0000-000000000001',
    'c3000001-0000-0000-0000-000000000002',
    'c3000001-0000-0000-0000-000000000003',
    'c3000001-0000-0000-0000-000000000004',
    'c3000001-0000-0000-0000-000000000005',
    'c3000001-0000-0000-0000-000000000006'
);
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-FTS-001 | USL000009 | NFRL00021 | — | Feature |
| VIEW-FTS-001 | USL000009 | NFRL00027 | — | Feature |
| VIEW-FTS-002 | USL000009 | NFRL00024 | — | Feature |
