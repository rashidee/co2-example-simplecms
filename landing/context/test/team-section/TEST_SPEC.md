# Test Specification: Team Section

| Field | Value |
|-------|-------|
| Module | Team Section |
| Prefix | TMS |
| Layer | L2 (Reference Data) |
| Source Stories | USL000015 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Team Section displays a grid of team member profile cards. Each card includes a circular profile photo, the team member's name, their role/position, and a LinkedIn profile link. Only items with `status = ACTIVE` are displayed, ordered by `display_order` ascending. The layout uses a 3-column grid on desktop and a single column on mobile.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `team_members` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000015 | v1.0.0 |
| Data Model | `landing/context/model/team-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/team-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Team Section: 6 ACTIVE team members
INSERT INTO team_members (id, profile_picture_path, name, role, linkedin_url, display_order, status, created_at, updated_at) VALUES
('e5000001-0000-0000-0000-000000000001', '/images/team/john-doe.jpg', 'John Doe', 'Chief Executive Officer', 'https://linkedin.com/in/johndoe', 1, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000002', '/images/team/jane-smith.jpg', 'Jane Smith', 'Chief Technology Officer', 'https://linkedin.com/in/janesmith', 2, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000003', '/images/team/robert-wilson.jpg', 'Robert Wilson', 'Head of Product', 'https://linkedin.com/in/robertwilson', 3, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000004', '/images/team/maria-garcia.jpg', 'Maria Garcia', 'Lead Designer', 'https://linkedin.com/in/mariagarcia', 4, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000005', '/images/team/alex-nguyen.jpg', 'Alex Nguyen', 'Senior Developer', 'https://linkedin.com/in/alexnguyen', 5, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000006', '/images/team/priya-patel.jpg', 'Priya Patel', 'Marketing Manager', 'https://linkedin.com/in/priyapatel', 6, 'ACTIVE', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Name | Role | LinkedIn URL | Status | Expected Display |
|-------------|------|------|--------------|--------|------------------|
| ...0001 | John Doe | Chief Executive Officer | https://linkedin.com/in/johndoe | ACTIVE | Yes |
| ...0002 | Jane Smith | Chief Technology Officer | https://linkedin.com/in/janesmith | ACTIVE | Yes |
| ...0003 | Robert Wilson | Head of Product | https://linkedin.com/in/robertwilson | ACTIVE | Yes |
| ...0004 | Maria Garcia | Lead Designer | https://linkedin.com/in/mariagarcia | ACTIVE | Yes |
| ...0005 | Alex Nguyen | Senior Developer | https://linkedin.com/in/alexnguyen | ACTIVE | Yes |
| ...0006 | Priya Patel | Marketing Manager | https://linkedin.com/in/priyapatel | ACTIVE | Yes |

---

## 6. Test Scenarios

### NAV-TMS-001: Team section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-TMS-001 |
| Title | Team section is visible on the landing page |
| Priority | High |
| Source Story | USL000015 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Team section. | The section is visible with the title "Meet Our Team". |

---

### VIEW-TMS-001: Team cards display photo, name, and role

| Field | Value |
|-------|-------|
| ID | VIEW-TMS-001 |
| Title | Team cards display photo, name, and role |
| Priority | High |
| Source Story | USL000015 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Team section. | 6 team member cards are displayed. |
| 3 | Inspect the first card. | The card displays a profile photo, the name "John Doe" in bold (at least 18px), and the role "Chief Executive Officer" (at least 16px). |
| 4 | Inspect each of the remaining cards. | Each card displays its respective profile photo, name, and role. |

---

### VIEW-TMS-002: LinkedIn links open in a new tab

| Field | Value |
|-------|-------|
| ID | VIEW-TMS-002 |
| Title | LinkedIn links open in a new tab |
| Priority | High |
| Source Story | USL000015, NFRL00066 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Team section and inspect the first team member card (John Doe). | A LinkedIn icon link (at least 24px) is displayed on the card. |
| 3 | Inspect the LinkedIn link element. | The link has `target="_blank"` attribute and `href` pointing to `https://linkedin.com/in/johndoe`. |
| 4 | Click the LinkedIn link. | A new browser tab opens with the LinkedIn profile URL. |

---

### VIEW-TMS-003: Profile photos are displayed as circular images

| Field | Value |
|-------|-------|
| ID | VIEW-TMS-003 |
| Title | Profile photos are displayed as circular images |
| Priority | Medium |
| Source Story | USL000015, NFRL00057 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Team section. | Team member cards with profile photos are displayed. |
| 3 | Inspect the profile photo of the first team member (John Doe). | The image element has CSS `border-radius: 50%` (or equivalent) making it circular, and is at least 150x150 pixels in rendered size. |
| 4 | Verify all other team member photos. | All 6 profile photos are rendered as circular images. |

---

## 7. Cleanup Script

```sql
DELETE FROM team_members WHERE id IN (
    'e5000001-0000-0000-0000-000000000001',
    'e5000001-0000-0000-0000-000000000002',
    'e5000001-0000-0000-0000-000000000003',
    'e5000001-0000-0000-0000-000000000004',
    'e5000001-0000-0000-0000-000000000005',
    'e5000001-0000-0000-0000-000000000006'
);
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-TMS-001 | USL000015 | NFRL00051 | — | TeamMember |
| VIEW-TMS-001 | USL000015 | NFRL00060, NFRL00063 | — | TeamMember |
| VIEW-TMS-002 | USL000015 | NFRL00066 | — | TeamMember |
| VIEW-TMS-003 | USL000015 | NFRL00057 | — | TeamMember |
