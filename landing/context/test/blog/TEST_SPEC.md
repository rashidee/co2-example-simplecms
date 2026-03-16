# TEST_SPEC: Blog

**Application**: Landing Page (SCMS)
**Module**: Blog
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql)
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the blog content and features. Public visitors can browse a paginated list of blog articles and read individual blog posts via SEO-friendly slug URLs.

### Layer Classification Reasoning

L2 (Reference Data): Blog content managed by Admin Portal, displayed read-only on the landing page. No auth, no MQ, no auto-generated data.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000021 | v1.0.0 |
| User Story | USL000024 | v1.0.0 |
| NFR | NFRL00087 | v1.0.0 |
| NFR | NFRL00090 | v1.0.0 |
| NFR | NFRL00093 | v1.0.0 |
| NFR | NFRL00096 | v1.0.0 |
| NFR | NFRL00099 | v1.0.0 |
| NFR | NFRL00102 | v1.0.0 |
| Constraint | CONSL0009 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/blog/model.md` | Found |
| Specification | `landing/context/specification/blog/SPEC.md` | Found |
| Mockup (list) | `landing/context/mockup/pages/blog.html` | Found |
| Mockup (detail) | `landing/context/mockup/pages/blog_detail.html` | Found |

### Removed / Replaced

_None._

---

## 2. Prerequisites

| Prerequisite | Module | Layer | How to Verify |
|-------------|--------|-------|--------------|
| PostgreSQL running | — | Infra | `pg_isready -h localhost -p 5432` |
| Landing page app running | — | Infra | `curl -s http://localhost:8000/` |
| Blog categories seeded | Blog (self) | L2 | `psql ... -c "SELECT count(*) FROM blg_blog_category WHERE created_by='test-seed'"` |

---

## 3. Data Seeding

### 3a. Seeding Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
-- Categories first (FK dependency)
INSERT INTO blg_blog_category (id, name, description, version, created_at, created_by, updated_at, updated_by) VALUES
  ('g7000001-0000-0000-0000-000000000001', 'Getting Started', 'Tutorials and guides for new users', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('g7000001-0000-0000-0000-000000000002', 'Marketing Tips', 'Marketing advice for small businesses', 0, NOW(), 'test-seed', NOW(), 'test-seed');

-- 12 blog posts: 10 active (for pagination), 1 draft, 1 expired
INSERT INTO blg_blog_post (id, category_id, author_id, title, slug, summary, content, image_path, effective_date, expiration_date, status, version, created_at, created_by, updated_at, updated_by) VALUES
  ('h8000001-0000-0000-0000-000000000001', 'g7000001-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 'Getting Started with Simple CMS', 'getting-started-with-simple-cms', 'Learn how to set up your first marketing page in under 30 minutes.', '<p>Welcome to Simple CMS, the easiest way for small business owners...</p>', '/test/blog1.jpg', '2026-03-01 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000002', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'Top 10 Marketing Tips for Small Businesses', 'top-10-marketing-tips-for-small-businesses', 'Discover proven strategies that help small businesses attract more customers.', '<p>Marketing your small business...</p>', '/test/blog2.jpg', '2026-03-02 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000003', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'SEO Basics Every Business Owner Should Know', 'seo-basics-every-business-owner-should-know', 'Understanding search engine optimisation does not require a technical background.', '<p>SEO is fundamental...</p>', '/test/blog3.jpg', '2026-03-03 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000004', 'g7000001-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 'How to Write Compelling Blog Posts', 'how-to-write-compelling-blog-posts', 'Great content drives traffic and builds trust.', '<p>Writing compelling content...</p>', '/test/blog4.jpg', '2026-03-04 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000005', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'Building Customer Trust Through Testimonials', 'building-customer-trust-through-testimonials', 'Social proof is one of the most powerful marketing tools.', '<p>Testimonials matter...</p>', '/test/blog5.jpg', '2026-03-05 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000006', 'g7000001-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 'Designing Effective Contact Forms', 'designing-effective-contact-forms', 'Your contact form is often the first interaction a potential customer has.', '<p>Contact forms are critical...</p>', '/test/blog6.jpg', '2026-03-06 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000007', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'The Importance of Mobile Responsive Design', 'the-importance-of-mobile-responsive-design', 'With over 60% of web traffic from mobile devices, responsive design is essential.', '<p>Mobile-first is key...</p>', '/test/blog7.jpg', '2026-03-07 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000008', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'Email Marketing Strategies That Work', 'email-marketing-strategies-that-work', 'Email remains one of the highest ROI channels for small businesses.', '<p>Email marketing tips...</p>', '/test/blog8.jpg', '2026-03-08 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000009', 'g7000001-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 'Creating a Brand Identity Online', 'creating-a-brand-identity-online', 'Your brand is more than a logo.', '<p>Brand identity matters...</p>', '/test/blog9.jpg', '2026-03-09 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000010', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'Measuring Your Marketing Success', 'measuring-your-marketing-success', 'If you cannot measure it, you cannot improve it.', '<p>Analytics and KPIs...</p>', '/test/blog10.jpg', '2026-03-10 00:00:00', '2027-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000011', 'g7000001-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000000', 'Draft Blog Post', 'draft-blog-post', 'This draft should not be visible on the landing page.', '<p>Draft content...</p>', '/test/blog-draft.jpg', '2026-03-11 00:00:00', '2027-12-31 23:59:59', 'DRAFT', 0, NOW(), 'test-seed', NOW(), 'test-seed'),
  ('h8000001-0000-0000-0000-000000000012', 'g7000001-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000000', 'Expired Blog Post', 'expired-blog-post', 'This expired post should not be visible.', '<p>Expired content...</p>', '/test/blog-expired.jpg', '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'ACTIVE', 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `blg_blog_category` | 2 | name, description | "Getting Started", "Marketing Tips" |
| `blg_blog_post` | 12 (10 active + 1 draft + 1 expired) | title, slug, summary, status, effective_date | "Getting Started with Simple CMS", slug: "getting-started-with-simple-cms", ACTIVE |

---

## 4. Test Scenarios

### 4a. Navigation Tests

#### NAV-BLG-001: Navigate to blog from navbar

- **Source**: Sidebar/Navbar navigation [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Click "Blog" in the top navigation bar
  3. Wait for blog page to load
- **Expected**:
  - URL is `http://localhost:8000/blog`
  - Page heading "Our Blog" is visible (NFRL00087)
- **Selectors** (from mockup):
  - Blog nav link: `nav a[href="/blog"]`
  - Blog heading: `h1` containing "Our Blog"

### 4b. View Tests

#### VIEW-BLG-001: Blog list shows active posts only

- **Source**: USL000021 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 12 posts seeded (10 active + 1 draft + 1 expired)
- **Steps**:
  1. Navigate to `http://localhost:8000/blog`
  2. Count visible blog cards on page 1
- **Expected**:
  - Exactly 10 blog cards visible (first page, all active and within date range)
  - "Draft Blog Post" is NOT visible (status=DRAFT)
  - "Expired Blog Post" is NOT visible (expired date range)
  - Posts ordered by effective_date descending (most recent first)
  - Each card shows: thumbnail image, title, short description

#### VIEW-BLG-002: Blog detail page renders full content

- **Source**: USL000024 [v1.0.0], NFRL00102 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: Blog post "getting-started-with-simple-cms" seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/blog/getting-started-with-simple-cms`
  2. Wait for page to load
- **Expected**:
  - URL uses SEO-friendly slug format
  - Hero image visible at top (1600x500 per mockup)
  - Title "Getting Started with Simple CMS" overlaid on image
  - Published date visible
  - Full HTML content rendered
  - "Back to Blog" link visible at top and bottom
  - Single column layout, max-width ~800px (NFRL00102)
  - White background for content area
- **Selectors** (from mockup):
  - Hero image: `section.relative img`
  - Title overlay: `section.relative h1`
  - Back link: `a` containing "Back to Blog"
  - Article content: `article .prose`

### 4c. Pagination Tests

#### PAGE-BLG-001: Blog list paginated at 10 per page

- **Source**: CONSL0009 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 10 active blog posts seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/blog`
  2. Count blog cards on page 1
  3. Verify pagination controls are visible
  4. Note: with exactly 10 active posts, there should be 1 page
- **Expected**:
  - 10 blog cards displayed on page 1
  - Pagination navigation visible at bottom
  - Current page (1) highlighted
- **Selectors** (from mockup):
  - Pagination nav: `nav[aria-label="Blog pagination"]` or `nav[aria-label="Pagination"]`
  - Active page: `.bg-primary` within pagination

### 4d. SEO Tests

#### SEO-BLG-001: Blog URLs use SEO-friendly slug format

- **Source**: PRD.md Coding Standard [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/blog`
  2. Click on "Getting Started with Simple CMS"
  3. Check the URL
- **Expected**:
  - URL is `/blog/getting-started-with-simple-cms` (lowercase, hyphen-separated)
  - NOT `/blog/{uuid}` or `/blog/{id}`
  - Slug is unique and derived from title

### 4e. Content Tests

#### CONT-BLG-001: Blog list card layout matches mockup

- **Source**: USL000021 [v1.0.0], NFRL00090 [v1.0.0], NFRL00096 [v1.0.0], NFRL00099 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/blog` at desktop viewport (1280x720)
  2. Inspect first blog card
  3. Resize to mobile (375x667)
  4. Re-inspect layout
- **Expected**:
  - Desktop: horizontal card — thumbnail left, title+description right (NFRL00090)
  - Mobile: vertical card — thumbnail top, title+description below (NFRL00090)
  - Title in bold, at least 18px (NFRL00096)
  - Description at least 16px (NFRL00099)
  - Cards are clickable links to detail page

### 4f. Negative Tests

#### NEG-BLG-001: Non-existent slug returns 404

- **Source**: General robustness
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to `http://localhost:8000/blog/this-slug-does-not-exist`
- **Expected**:
  - 404 error page displayed
  - Error page includes "Back to Home" link

---

## 5. Data Cleanup

### 5a. Cleanup Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM blg_blog_post WHERE created_by = 'test-seed';
DELETE FROM blg_blog_category WHERE created_by = 'test-seed';
"
```

### 5b. Cleanup Order

1. `blg_blog_post` (FK to category)
2. `blg_blog_category`

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| NAV-BLG-001 | — | v1.0.0 | NFRL00087 | — | Navigation |
| VIEW-BLG-001 | USL000021 | v1.0.0 | — | — | View |
| VIEW-BLG-002 | USL000024 | v1.0.0 | NFRL00102 | — | View |
| PAGE-BLG-001 | — | v1.0.0 | — | CONSL0009 | Pagination |
| SEO-BLG-001 | — | v1.0.0 | — | — | SEO |
| CONT-BLG-001 | USL000021 | v1.0.0 | NFRL00090, NFRL00096, NFRL00099 | — | Content |
| NEG-BLG-001 | — | — | — | — | Negative |
