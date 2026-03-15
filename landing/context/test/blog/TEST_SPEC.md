# Test Specification: Blog

| Field | Value |
|-------|-------|
| Module | Blog |
| Prefix | BLG |
| Layer | L2 (Reference Data) |
| Source Stories | USL000021, USL000024 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Blog module provides a blog directory page (`/blog`) displaying blog post cards and individual blog detail pages (`/blog/{slug}`) with full article content. The directory page shows cards with thumbnail, title, and summary in a paginated layout (10 posts per page). Blog detail pages use SEO-friendly slug URLs and display the full image, title (overlaid on image), content, and a back-to-directory link. Only posts with `status = ACTIVE` are displayed, ordered by `effective_date` descending.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `blog_categories` and `blog_posts` tables |
| Auth Required | No |
| Dependencies | BlogPost references BlogCategory via `category_id` FK |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Stories) | `landing/context/PRD.md` -- USL000021, USL000024 | v1.0.0 |
| Data Model | `landing/context/model/blog/model.md` | v1.0.0 |
| ERD | `landing/context/model/blog/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Blog Categories: 3 categories
INSERT INTO blog_categories (id, name, created_at, updated_at) VALUES
('g7000001-0000-0000-0000-000000000001', 'Technology', NOW(), NOW()),
('g7000001-0000-0000-0000-000000000002', 'Business', NOW(), NOW()),
('g7000001-0000-0000-0000-000000000003', 'Design', NOW(), NOW());

-- Blog Posts: 13 ACTIVE + 2 EXPIRED = 15 total
INSERT INTO blog_posts (id, category_id, title, slug, summary, content, image_path, effective_date, status, created_at, updated_at) VALUES
('h8000001-0000-0000-0000-000000000001', 'g7000001-0000-0000-0000-000000000001', 'The Future of AI in Web Development', 'the-future-of-ai-in-web-development', 'Exploring how artificial intelligence is reshaping the way we build web applications.', '<p>Artificial intelligence is transforming web development in unprecedented ways. From code generation to automated testing, AI tools are becoming indispensable for modern developers.</p><p>In this article, we explore the key trends and what they mean for your business.</p>', '/images/blog/ai-web-dev.jpg', '2026-03-15 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000002', 'g7000001-0000-0000-0000-000000000001', 'Cloud Migration Best Practices', 'cloud-migration-best-practices', 'A comprehensive guide to migrating your infrastructure to the cloud safely and efficiently.', '<p>Cloud migration is a critical step for modern businesses. This guide covers planning, execution, and post-migration optimization strategies.</p>', '/images/blog/cloud-migration.jpg', '2026-03-14 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000003', 'g7000001-0000-0000-0000-000000000002', 'Building a Remote-First Culture', 'building-a-remote-first-culture', 'How to create a thriving company culture when your team works from anywhere.', '<p>Remote work is here to stay. Learn how successful companies are building strong cultures without physical offices.</p>', '/images/blog/remote-culture.jpg', '2026-03-13 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000004', 'g7000001-0000-0000-0000-000000000003', 'UI Design Trends for 2026', 'ui-design-trends-for-2026', 'The latest design trends that are shaping user interfaces this year.', '<p>From glassmorphism to AI-generated layouts, discover the design trends defining 2026.</p>', '/images/blog/design-trends.jpg', '2026-03-12 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000005', 'g7000001-0000-0000-0000-000000000001', 'Cybersecurity Essentials for Startups', 'cybersecurity-essentials-for-startups', 'Essential security practices every startup should implement from day one.', '<p>Startups often overlook security in favor of speed. Here are the non-negotiable security measures you need.</p>', '/images/blog/cybersecurity.jpg', '2026-03-11 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000006', 'g7000001-0000-0000-0000-000000000002', 'Scaling Your SaaS Business', 'scaling-your-saas-business', 'Proven strategies for scaling a SaaS company from startup to enterprise.', '<p>Scaling a SaaS business requires careful planning across product, engineering, and go-to-market functions.</p>', '/images/blog/saas-scaling.jpg', '2026-03-10 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000007', 'g7000001-0000-0000-0000-000000000003', 'Accessibility in Modern Web Design', 'accessibility-in-modern-web-design', 'Why accessibility matters and how to implement it in your web projects.', '<p>Web accessibility is not just a legal requirement but a moral imperative. Learn how to make your sites inclusive.</p>', '/images/blog/accessibility.jpg', '2026-03-09 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000008', 'g7000001-0000-0000-0000-000000000001', 'Introduction to Edge Computing', 'introduction-to-edge-computing', 'Understanding edge computing and its impact on application performance.', '<p>Edge computing brings computation closer to users, reducing latency and improving user experience.</p>', '/images/blog/edge-computing.jpg', '2026-03-08 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000009', 'g7000001-0000-0000-0000-000000000002', 'Customer Retention Strategies', 'customer-retention-strategies', 'Effective strategies to keep your customers engaged and reduce churn.', '<p>Retaining customers costs far less than acquiring new ones. Here are proven retention strategies.</p>', '/images/blog/retention.jpg', '2026-03-07 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000010', 'g7000001-0000-0000-0000-000000000003', 'The Art of Responsive Design', 'the-art-of-responsive-design', 'Mastering responsive design for seamless experiences across all devices.', '<p>Responsive design is no longer optional. Learn advanced techniques for truly fluid layouts.</p>', '/images/blog/responsive.jpg', '2026-03-06 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000011', 'g7000001-0000-0000-0000-000000000001', 'DevOps Pipeline Automation', 'devops-pipeline-automation', 'Automating your CI/CD pipeline for faster and more reliable deployments.', '<p>A well-automated DevOps pipeline accelerates delivery while maintaining quality.</p>', '/images/blog/devops.jpg', '2026-03-05 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000012', 'g7000001-0000-0000-0000-000000000002', 'Data-Driven Decision Making', 'data-driven-decision-making', 'How to leverage data analytics to make better business decisions.', '<p>Data-driven organizations outperform their peers. Learn how to build a data culture.</p>', '/images/blog/data-driven.jpg', '2026-03-04 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000013', 'g7000001-0000-0000-0000-000000000003', 'Microinteractions That Delight Users', 'microinteractions-that-delight-users', 'Small design details that make a big difference in user experience.', '<p>Microinteractions are the subtle animations and feedback that make interfaces feel alive.</p>', '/images/blog/microinteractions.jpg', '2026-03-03 00:00:00', 'ACTIVE', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000014', 'g7000001-0000-0000-0000-000000000001', 'Holiday Tech Gift Guide 2025', 'holiday-tech-gift-guide-2025', 'Our picks for the best tech gifts this holiday season.', '<p>Looking for the perfect tech gift? Here are our top recommendations for the 2025 holiday season.</p>', '/images/blog/holiday-gifts.jpg', '2025-12-01 00:00:00', 'EXPIRED', NOW(), NOW()),
('h8000001-0000-0000-0000-000000000015', 'g7000001-0000-0000-0000-000000000002', 'Year-End Business Review Template', 'year-end-business-review-template', 'A template for conducting your annual business performance review.', '<p>Use this comprehensive template to review your business performance and set goals for the new year.</p>', '/images/blog/year-review.jpg', '2025-12-15 00:00:00', 'EXPIRED', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Title | Category | Slug | Status | Effective Date | Expected Display |
|-------------|-------|----------|------|--------|----------------|------------------|
| ...0001 | The Future of AI in Web Development | Technology | the-future-of-ai-in-web-development | ACTIVE | 2026-03-15 | Yes |
| ...0002 | Cloud Migration Best Practices | Technology | cloud-migration-best-practices | ACTIVE | 2026-03-14 | Yes |
| ...0003 | Building a Remote-First Culture | Business | building-a-remote-first-culture | ACTIVE | 2026-03-13 | Yes |
| ...0004 | UI Design Trends for 2026 | Design | ui-design-trends-for-2026 | ACTIVE | 2026-03-12 | Yes |
| ...0005 | Cybersecurity Essentials for Startups | Technology | cybersecurity-essentials-for-startups | ACTIVE | 2026-03-11 | Yes |
| ...0006 | Scaling Your SaaS Business | Business | scaling-your-saas-business | ACTIVE | 2026-03-10 | Yes |
| ...0007 | Accessibility in Modern Web Design | Design | accessibility-in-modern-web-design | ACTIVE | 2026-03-09 | Yes |
| ...0008 | Introduction to Edge Computing | Technology | introduction-to-edge-computing | ACTIVE | 2026-03-08 | Yes |
| ...0009 | Customer Retention Strategies | Business | customer-retention-strategies | ACTIVE | 2026-03-07 | Yes |
| ...0010 | The Art of Responsive Design | Design | the-art-of-responsive-design | ACTIVE | 2026-03-06 | Yes |
| ...0011 | DevOps Pipeline Automation | Technology | devops-pipeline-automation | ACTIVE | 2026-03-05 | Yes |
| ...0012 | Data-Driven Decision Making | Business | data-driven-decision-making | ACTIVE | 2026-03-04 | Yes |
| ...0013 | Microinteractions That Delight Users | Design | microinteractions-that-delight-users | ACTIVE | 2026-03-03 | Yes |
| ...0014 | Holiday Tech Gift Guide 2025 | Technology | holiday-tech-gift-guide-2025 | EXPIRED | 2025-12-01 | No |
| ...0015 | Year-End Business Review Template | Business | year-end-business-review-template | EXPIRED | 2025-12-15 | No |

---

## 6. Test Scenarios

### NAV-BLG-001: Blog link in navbar navigates to /blog

| Field | Value |
|-------|-------|
| ID | NAV-BLG-001 |
| Title | Blog link in navbar navigates to /blog |
| Priority | High |
| Source Story | USL000021 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Locate the "Blog" menu item in the top navigation bar. | The "Blog" link is visible in the navbar. |
| 3 | Click the "Blog" menu item. | The browser navigates to `/blog` and the blog directory page loads. |

---

### VIEW-BLG-001: Blog directory shows blog post cards

| Field | Value |
|-------|-------|
| ID | VIEW-BLG-001 |
| Title | Blog directory shows blog post cards |
| Priority | High |
| Source Story | USL000021 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog`. | The blog directory page loads with the title "Our Blog". |
| 2 | Observe the blog post listing. | Blog post cards are displayed in a vertical card layout. On desktop, each card has the thumbnail image on the left and the title and short description on the right. |
| 3 | Count the visible blog post cards on the first page. | 10 blog post cards are displayed (pagination applies). |

---

### VIEW-BLG-002: Blog card shows thumbnail, title, and summary

| Field | Value |
|-------|-------|
| ID | VIEW-BLG-002 |
| Title | Blog card shows thumbnail, title, and summary |
| Priority | High |
| Source Story | USL000021, NFRL00090 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog`. | The blog directory page loads. |
| 2 | Inspect the first blog post card. | The card displays: a thumbnail image, the title "The Future of AI in Web Development" in bold (at least 18px), and the summary "Exploring how artificial intelligence is reshaping the way we build web applications." (at least 16px). |
| 3 | Inspect the second blog post card. | The card displays the thumbnail, title "Cloud Migration Best Practices", and corresponding summary. |

---

### VIEW-BLG-003: Blog detail page accessible via slug URL

| Field | Value |
|-------|-------|
| ID | VIEW-BLG-003 |
| Title | Blog detail page accessible via slug URL |
| Priority | High |
| Source Story | USL000024 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog/the-future-of-ai-in-web-development`. | The blog detail page loads successfully. |
| 2 | Verify the URL in the browser address bar. | The URL is `/blog/the-future-of-ai-in-web-development` (SEO-friendly slug format). |

---

### VIEW-BLG-004: Blog detail page shows image, title, and content

| Field | Value |
|-------|-------|
| ID | VIEW-BLG-004 |
| Title | Blog detail page shows image, title, and content |
| Priority | High |
| Source Story | USL000024, NFRL00102 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog/the-future-of-ai-in-web-development`. | The blog detail page loads successfully. |
| 2 | Observe the hero image area. | A large image (1600x500 pixels) is displayed at the top of the article. |
| 3 | Observe the title. | The title "The Future of AI in Web Development" is displayed overlapping on top of the image. |
| 4 | Observe the article content. | The full article content is rendered as HTML, including paragraphs about AI transforming web development. The content is displayed in a single column layout with a maximum width of 800px. |

---

### VIEW-BLG-005: Back to blog link returns to the directory

| Field | Value |
|-------|-------|
| ID | VIEW-BLG-005 |
| Title | Back to blog link returns to the directory |
| Priority | Medium |
| Source Story | USL000024 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog/the-future-of-ai-in-web-development`. | The blog detail page loads successfully. |
| 2 | Locate the "back to blog" link on the page. | A link to return to the blog directory is visible. |
| 3 | Click the back-to-blog link. | The browser navigates to `/blog` and the blog directory page is displayed. |

---

### PAGE-BLG-001: Pagination displays 10 posts per page

| Field | Value |
|-------|-------|
| ID | PAGE-BLG-001 |
| Title | Pagination displays 10 posts per page |
| Priority | High |
| Source Story | USL000021, CONSL0009 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog`. | The blog directory page loads. |
| 2 | Count the blog post cards on the first page. | Exactly 10 blog post cards are displayed. |
| 3 | Observe the pagination navigation at the bottom of the page. | Pagination controls are visible (e.g., page numbers, next/previous buttons). |
| 4 | Click the "Next" button or page 2 link. | The second page loads with the remaining 3 active blog posts. |
| 5 | Verify the posts on page 2. | 3 blog post cards are displayed (posts 11-13 by effective date descending). |

---

### VAL-BLG-001: Expired blog posts are not displayed

| Field | Value |
|-------|-------|
| ID | VAL-BLG-001 |
| Title | Expired blog posts are not displayed |
| Priority | High |
| Source Story | USL000021 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog`. | The blog directory page loads. |
| 2 | Browse through all pages of the blog directory. | A total of 13 blog post cards are displayed across all pages (10 on page 1, 3 on page 2). |
| 3 | Verify titles across all pages. | The titles "Holiday Tech Gift Guide 2025" and "Year-End Business Review Template" (EXPIRED status) do NOT appear on any page. |
| 4 | Navigate directly to `/blog/holiday-tech-gift-guide-2025`. | A 404 page or appropriate error is displayed (the expired post is not accessible). |

---

### VAL-BLG-002: Blog URLs use SEO-friendly slug format

| Field | Value |
|-------|-------|
| ID | VAL-BLG-002 |
| Title | Blog URLs use SEO-friendly slug format |
| Priority | Medium |
| Source Story | USL000024 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/blog`. | The blog directory page loads. |
| 2 | Click on the first blog post card ("The Future of AI in Web Development"). | The browser navigates to the blog detail page. |
| 3 | Verify the URL in the browser address bar. | The URL follows the pattern `/blog/{slug}` where the slug is lowercase, words separated by hyphens (e.g., `/blog/the-future-of-ai-in-web-development`). No numeric IDs or query parameters are present in the URL. |
| 4 | Click on another blog post and verify its URL. | The URL follows the same SEO-friendly slug pattern. |

---

## 7. Cleanup Script

```sql
-- Remove blog posts first (FK dependency on categories)
DELETE FROM blog_posts WHERE id LIKE 'h8000001-%';

-- Remove blog categories
DELETE FROM blog_categories WHERE id LIKE 'g7000001-%';
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-BLG-001 | USL000021 | — | — | BlogPost |
| VIEW-BLG-001 | USL000021 | NFRL00087, NFRL00090 | — | BlogPost |
| VIEW-BLG-002 | USL000021 | NFRL00090, NFRL00096, NFRL00099 | — | BlogPost |
| VIEW-BLG-003 | USL000024 | — | — | BlogPost |
| VIEW-BLG-004 | USL000024 | NFRL00093, NFRL00102 | — | BlogPost |
| VIEW-BLG-005 | USL000024 | — | — | BlogPost |
| PAGE-BLG-001 | USL000021 | — | CONSL0009 | BlogPost |
| VAL-BLG-001 | USL000021 | — | — | BlogPost |
| VAL-BLG-002 | USL000024 | — | — | BlogPost |
