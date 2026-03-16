# E2E Test Plan: Landing Page

**Generated**: 2026-03-16
**Source**: `landing/context/PRD.md`
**Version**: all versions
**Versions Covered**: v1.0.0
**Module Filter**: all modules

---

## 1. Application Overview

- **Application**: Landing Page (SCMS)
- **Description**: A landing page to display the marketing content and blog content to the public.
- **Stack**: Laravel 12 + Eloquent + Blade + Tailwind CSS + Alpine.js + HTMX + PostgreSQL 14

---

## 2. Test Infrastructure

| Component | Type | CLI Tool | Connection |
|-----------|------|----------|------------|
| Database | PostgreSQL 14 | `psql` | `psql -h localhost -p 5432 -U cms_user -d cms_db` |

### CLI Commands Reference

```bash
# PostgreSQL — insert/query/delete test data
psql -h localhost -p 5432 -U cms_user -d cms_db -c "<SQL>"

# Alternative: Laravel artisan tinker
php artisan tinker --execute="<PHP code>"
```

**Note:** No auth provider or message queue infrastructure is needed — this is a public-facing landing page with no authentication (CONSL0003).

---

## 3. Test Users

No test users required. All pages are publicly accessible without authentication.

---

## 4. Layer Classification Summary

| Module | Category | Layer | Seeding Strategy | Versions | Dependencies |
|--------|----------|-------|-----------------|----------|--------------|
| Hero Section | Business | L2 | DB Insert (psql) | v1.0.0 | None |
| Product and Service Section | Business | L2 | DB Insert (psql) | v1.0.0 | None |
| Features Section | Business | L2 | DB Insert (psql) | v1.0.0 | None |
| Testimonials Section | Business | L2 | DB Insert (psql) | v1.0.0 | None |
| Team Section | Business | L2 | DB Insert (psql) | v1.0.0 | None |
| Contact Section | Business | L2 | DB Insert (psql) + Form Submit | v1.0.0 | None |
| Blog | Business | L2 | DB Insert (psql) | v1.0.0 | None (blog_category seeded first) |

**Classification Reasoning**: All modules are L2 (Reference Data) because:
- No authentication modules exist (L1 not applicable per CONSL0003)
- No message queue triggers (L3 not applicable)
- No auto-generated/audit data modules (L4 not applicable)
- All content is managed via the Admin Portal and read by the Landing Page
- Contact Section has a write operation (contact form) but is primarily reference-data-driven

---

## 5. Execution Order

Since all modules are L2 with no inter-module dependencies (each section is independent), the execution order follows the page layout top-to-bottom:

1. **L2 — Reference Data (Home Page Sections)**:
   1. Hero Section
   2. Product and Service Section
   3. Features Section
   4. Testimonials Section
   5. Team Section
   6. Contact Section
2. **L2 — Reference Data (Blog)**:
   7. Blog (requires `blg_blog_category` seeded before `blg_blog_post`)

---

## 6. Table of Contents

### L2 — Reference Data

1. [Hero Section](hero-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-hero-section)
2. [Product and Service Section](product-and-service-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-product-and-service-section)
3. [Features Section](features-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-features-section)
4. [Testimonials Section](testimonials-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-testimonials-section)
5. [Team Section](team-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-team-section)
6. [Contact Section](contact-section/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-contact-section)
7. [Blog](blog/TEST_SPEC.md) — Versions: v1.0.0 — [Summary](#module-blog)

---

## 7. Module Summaries

### Module: Hero Section {#module-hero-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `hrs_hero_section`)
**Spec File**: [hero-section/TEST_SPEC.md](hero-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `hrs_hero_section` | 3 | headline, subheadline, cta_url, cta_text, effective_date, expiration_date, image_data |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify hero carousel renders active slides |
| Carousel | 2 | Auto-slide every 5s, manual arrow navigation |
| Responsive | 1 | Hero section displays correctly on mobile |
| **Total** | **4** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- Only active slides (within effective_date and expiration_date range) are displayed
- Carousel auto-advances every 5 seconds (NFRL00003)
- CTA buttons open links in new tabs
- Images render at 1600x500 resolution (NFRL00006)

---

### Module: Product and Service Section {#module-product-and-service-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `pas_product_service`)
**Spec File**: [product-and-service-section/TEST_SPEC.md](product-and-service-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `pas_product_service` | 4 | title, description, cta_url, cta_text, display_order, image_data |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify products/services grid renders all items |
| Layout | 1 | 3-column desktop, 1-column mobile |
| Content | 1 | Card shows image, title, description, CTA link |
| **Total** | **3** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- All items displayed (no status filter — v1.0.4 removed status column)
- Items ordered by display_order ascending
- Grid: 3 columns on desktop, 1 column on mobile (NFRL00012)
- CTA links open in new tab

---

### Module: Features Section {#module-features-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `fts_feature`)
**Spec File**: [features-section/TEST_SPEC.md](features-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `fts_feature` | 4 | icon, title, description, display_order |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify features grid renders all items |
| Layout | 1 | 3-column desktop, 1-column mobile |
| Content | 1 | Card shows icon (48px), title, description |
| **Total** | **3** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- All items displayed (no status filter)
- Items ordered by display_order
- Icons rendered at 48px size (NFRL00027)
- Light gray background (NFRL00030)

---

### Module: Testimonials Section {#module-testimonials-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `tst_testimonial`)
**Spec File**: [testimonials-section/TEST_SPEC.md](testimonials-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `tst_testimonial` | 4 | customer_name, customer_review, customer_rating, display_order |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify testimonial carousel renders |
| Carousel | 1 | Manual arrow navigation between testimonials |
| Content | 1 | Shows star rating, review text, customer name |
| **Total** | **3** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- Carousel shows 1 testimonial per slide (NFRL00036)
- Star rating displays correct number of filled stars (1-5)
- Customer name bold 18px (NFRL00039), review 16px (NFRL00042)

---

### Module: Team Section {#module-team-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `tms_team_member`)
**Spec File**: [team-section/TEST_SPEC.md](team-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `tms_team_member` | 4 | name, role, linkedin_url, display_order, image_data |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify team grid renders all members |
| Layout | 1 | 3-column desktop, 1-column mobile |
| Content | 1 | Card shows circular photo, name, role, LinkedIn icon |
| **Total** | **3** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- All team members displayed (no status filter)
- Circular profile picture 150x150 (NFRL00057)
- LinkedIn icon opens in new tab (NFRL00066)

---

### Module: Contact Section {#module-contact-section}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `cts_contact_info`) + Form submit test
**Spec File**: [contact-section/TEST_SPEC.md](contact-section/TEST_SPEC.md)

**Dependencies**:

_None — this module has no prerequisites._

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `cts_contact_info` | 1 | phone_number, email_address, physical_address, linkedin_url |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| View | 1 | Verify contact info displays phone, email, address |
| CRUD — Create | 1 | Submit contact form message |
| Validation | 2 | Required fields, message max 500 chars |
| **Total** | **4** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- Contact info section shows phone, email, address with icons (NFRL00075)
- Contact form submits successfully via htmx POST
- Toast notification appears on successful submission
- Validation enforces required fields and 500-char message limit (NFRL00078)
- CAPTCHA placeholder is present (NFRL00084)

---

### Module: Blog {#module-blog}

**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql into `blg_blog_category` then `blg_blog_post`)
**Spec File**: [blog/TEST_SPEC.md](blog/TEST_SPEC.md)

**Dependencies**:

| Prerequisite | Module | Layer | Reason |
|-------------|--------|-------|--------|
| Blog categories | Blog (self) | L2 | `blg_blog_post.category_id` FK references `blg_blog_category` |

**Data Seeded**:

| Table | Record Count | Key Sample Fields |
|-------|-------------|-------------------|
| `blg_blog_category` | 2 | name, description |
| `blg_blog_post` | 12 | title, slug, summary, content, effective_date, expiration_date, status, category_id, image_data |

**Test Scenarios Summary**:

| Type | Count | Description |
|------|-------|-------------|
| Navigation | 1 | Navigate to /blog from navbar |
| View | 2 | Blog list page, blog detail page |
| Pagination | 1 | 10 per page pagination (CONSL0009) |
| SEO | 1 | SEO-friendly slug URL format |
| Content | 1 | Detail page shows image, title, full content, back link |
| **Total** | **6** | |

**Roles Tested**: Public (anonymous)

**Key Assertions**:
- Blog list shows only ACTIVE posts within date range
- Paginated at 10 per page (CONSL0009)
- Blog detail URL is SEO-friendly: `/blog/{slug}` format
- Blog detail shows hero image (1600x500), title overlay, full HTML content
- "Back to Blog" link navigates back to blog directory
- Horizontal card layout on desktop, vertical on mobile (NFRL00090)

---

## 8. Global Setup

### 8a. Auth Setup (L1)

Not applicable — no authentication required (CONSL0003).

### 8b. Reference Data Setup (L2)

Seed data in the following order:

1. `hrs_hero_section` — 3 active hero slides
2. `pas_product_service` — 4 products/services
3. `fts_feature` — 4 features
4. `tst_testimonial` — 4 testimonials
5. `tms_team_member` — 4 team members
6. `cts_contact_info` — 1 contact info record
7. `blg_blog_category` — 2 categories
8. `blg_blog_post` — 12 blog posts (10 active + 2 expired/draft for negative testing)

### 8c. Message Publishing (L3)

Not applicable — no message queue integration.

---

## 9. Global Teardown

### 9a. Database Cleanup

```bash
# Cleanup in reverse order
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM blg_blog_post WHERE created_by = 'test-seed';
DELETE FROM blg_blog_category WHERE created_by = 'test-seed';
DELETE FROM cts_contact_message WHERE created_by = 'public' AND sender_email LIKE '%@test.example.com';
DELETE FROM cts_contact_info WHERE created_by = 'test-seed';
DELETE FROM tms_team_member WHERE created_by = 'test-seed';
DELETE FROM tst_testimonial WHERE created_by = 'test-seed';
DELETE FROM fts_feature WHERE created_by = 'test-seed';
DELETE FROM pas_product_service WHERE created_by = 'test-seed';
DELETE FROM hrs_hero_section WHERE created_by = 'test-seed';
"
```

### 9b. Message Queue Cleanup

Not applicable.

### 9c. Auth Cleanup

Not applicable.
