# Test Specification: Blog

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Blog                                                                  |
| Module Prefix      | BLG                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages blog content (categories and posts) for the landing page. Blog content is marketing and communication material managed by editors. It has a cross-module dependency on the User module (L1) since blog posts reference users with EDITOR role as authors. It depends on L1 modules for authentication and authorization.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000096  | User Story | Create/update blog content with category, title, content, image    | v1.0.0  |
| USA000099  | User Story | Delete blog content                                                | v1.0.0  |
| USA000102  | User Story | View list of blog content with filters                             | v1.0.0  |
| USA000105  | User Story | Create blog category with name and description                     | v1.0.0  |
| USA000108  | User Story | View list of blog categories with edit and delete                  | v1.0.0  |
| NFRA00111  | NFR        | Blog image must be exactly 1600x500 pixels                         | v1.0.0  |
| NFRA00114  | NFR        | Thumbnail auto-generated at 400x125 pixels                         | v1.0.0  |
| NFRA00117  | NFR        | Title max 100 chars                                                | v1.0.0  |
| NFRA00120  | NFR        | Summary max 300 chars                                              | v1.0.0  |
| NFRA00123  | NFR        | Blog list ordered by effective date descending                     | v1.0.0  |
| NFRA00126  | NFR        | Blog slug auto-generated from title, must be unique                | v1.0.0  |
| NFRA00129  | NFR        | Rich text editor support (bold, italic, headings, lists, etc.)     | v1.0.0  |
| NFRA00132  | NFR        | Content stored as sanitized HTML to prevent XSS                    | v1.0.0  |
| CONSA0030  | Constraint | Blog post status: DRAFT (default), READY, ACTIVE, EXPIRED         | v1.0.0  |
| CONSA0033  | Constraint | Category must exist before creating blog post                      | v1.0.0  |
| CONSA0036  | Constraint | Category cannot be deleted if blog posts are associated            | v1.0.0  |
| CONSA0039  | Constraint | Blog author must be a user with EDITOR role                        | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `blog_categories` and `blog_posts` tables exist with the schema defined in the model.
- The `users` table exists with at least one EDITOR user for author selection.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated.
- Test image files of 1600x500 pixels are available for upload testing.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed editor users for blog authoring (if not already seeded)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u8000000-0000-0000-0000-000000000001', 'blogeditor1@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Blog', 'Editor1', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system'),
  ('u8000000-0000-0000-0000-000000000002', 'blogeditor2@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Blog', 'Editor2', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed a USER role user (for testing author validation CONSA0039)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u8000000-0000-0000-0000-000000000003', 'bloguser@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Blog', 'UserOnly', 'USER', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 5 blog_categories
INSERT INTO blog_categories (id, name, description, version, created_at, created_by)
VALUES
  ('bc100000-0000-0000-0000-000000000001', 'Technology', 'Articles about the latest technology trends, software development, and digital innovation.', 0, '2026-03-01 09:00:00', 'editor'),
  ('bc100000-0000-0000-0000-000000000002', 'Business', 'Insights on business strategy, entrepreneurship, and market analysis for small business owners.', 0, '2026-03-01 09:15:00', 'editor'),
  ('bc100000-0000-0000-0000-000000000003', 'Design', 'Explorations of UI/UX design, graphic design trends, and creative inspiration for digital products.', 0, '2026-03-01 09:30:00', 'editor'),
  ('bc100000-0000-0000-0000-000000000004', 'Marketing', 'Tips and strategies for digital marketing, SEO, social media, and content marketing campaigns.', 0, '2026-03-01 09:45:00', 'editor'),
  ('bc100000-0000-0000-0000-000000000005', 'Tutorials', 'Step-by-step guides and how-to articles for developers, designers, and business professionals.', 0, '2026-03-01 10:00:00', 'editor');

-- Seed 15 blog_posts with various statuses, categories, and author_id FK to editor users
INSERT INTO blog_posts (id, category_id, author_id, title, slug, summary, content, image_path, thumbnail_path, effective_date, expiration_date, status, version, created_at, created_by)
VALUES
  ('bp100000-0000-0000-0000-000000000001', 'bc100000-0000-0000-0000-000000000001', 'u8000000-0000-0000-0000-000000000001', 'Getting Started with Spring Boot 4.0', 'getting-started-with-spring-boot-4-0', 'A comprehensive guide to the new features and improvements in Spring Boot 4.0 for modern Java development.', '<h2>Introduction</h2><p>Spring Boot 4.0 brings exciting new features that streamline Java development. In this article, we explore the key changes.</p><h3>Key Features</h3><ul><li>Virtual threads support</li><li>Enhanced observability</li><li>Improved startup time</li></ul>', '/images/blog/spring-boot-4.jpg', '/images/blog/thumb/spring-boot-4.jpg', '2026-03-15 00:00:00', '2026-06-15 23:59:59', 'ACTIVE', 0, '2026-03-10 09:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000002', 'bc100000-0000-0000-0000-000000000002', 'u8000000-0000-0000-0000-000000000001', 'Small Business Digital Strategy 2026', 'small-business-digital-strategy-2026', 'Essential digital strategies every small business should implement in 2026 to stay competitive and grow.', '<h2>Digital Strategy Essentials</h2><p>In the rapidly evolving digital landscape, small businesses need a solid strategy to thrive.</p>', '/images/blog/digital-strategy.jpg', '/images/blog/thumb/digital-strategy.jpg', '2026-03-14 00:00:00', '2026-09-14 23:59:59', 'ACTIVE', 0, '2026-03-09 14:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000003', 'bc100000-0000-0000-0000-000000000003', 'u8000000-0000-0000-0000-000000000002', 'UI Design Trends for 2026', 'ui-design-trends-for-2026', 'The hottest UI design trends shaping digital products this year, from glassmorphism to AI-generated layouts.', '<h2>Design Trends</h2><p>Stay ahead of the curve with these emerging UI design trends that are transforming the digital product landscape.</p>', '/images/blog/design-trends.jpg', '/images/blog/thumb/design-trends.jpg', '2026-03-12 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 0, '2026-03-08 11:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000004', 'bc100000-0000-0000-0000-000000000004', 'u8000000-0000-0000-0000-000000000001', 'SEO Best Practices: A 2026 Update', 'seo-best-practices-a-2026-update', 'Updated SEO strategies and techniques that reflect the latest search engine algorithm changes in 2026.', '<h2>SEO in 2026</h2><p>Search engine optimization continues to evolve. Here are the practices that matter most this year.</p>', '/images/blog/seo-2026.jpg', '/images/blog/thumb/seo-2026.jpg', '2026-03-10 00:00:00', '2026-09-30 23:59:59', 'ACTIVE', 0, '2026-03-07 16:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000005', 'bc100000-0000-0000-0000-000000000005', 'u8000000-0000-0000-0000-000000000002', 'Building REST APIs with Spring Boot', 'building-rest-apis-with-spring-boot', 'A step-by-step tutorial on building production-ready REST APIs using Spring Boot and best practices.', '<h2>REST API Tutorial</h2><p>Learn how to build robust REST APIs with Spring Boot, including validation, error handling, and testing.</p>', '/images/blog/rest-api.jpg', '/images/blog/thumb/rest-api.jpg', '2026-03-08 00:00:00', '2026-12-31 23:59:59', 'READY', 0, '2026-03-06 10:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000006', 'bc100000-0000-0000-0000-000000000001', 'u8000000-0000-0000-0000-000000000001', 'Introduction to AI-Powered Development', 'introduction-to-ai-powered-development', 'How artificial intelligence is revolutionizing the software development workflow and what it means for developers.', '<h2>AI in Development</h2><p>AI tools are changing how we write, test, and deploy code. Explore the landscape of AI-powered development.</p>', '/images/blog/ai-dev.jpg', '/images/blog/thumb/ai-dev.jpg', '2026-04-01 00:00:00', '2026-10-01 23:59:59', 'DRAFT', 0, '2026-03-12 09:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000007', 'bc100000-0000-0000-0000-000000000002', 'u8000000-0000-0000-0000-000000000002', 'Remote Work Best Practices', 'remote-work-best-practices', 'Proven strategies for managing remote teams effectively and maintaining productivity in a distributed environment.', '<h2>Remote Work Guide</h2><p>Master the art of remote work with these battle-tested strategies for teams of all sizes.</p>', '/images/blog/remote-work.jpg', '/images/blog/thumb/remote-work.jpg', '2026-03-05 00:00:00', '2026-06-05 23:59:59', 'ACTIVE', 0, '2026-03-03 13:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000008', 'bc100000-0000-0000-0000-000000000003', 'u8000000-0000-0000-0000-000000000001', 'Accessible Design Principles', 'accessible-design-principles', 'Why accessibility matters and how to implement WCAG guidelines in your web applications effectively.', '<h2>Accessibility</h2><p>Creating accessible designs is not just a legal requirement, it is good business. Learn the principles.</p>', '/images/blog/accessibility.jpg', '/images/blog/thumb/accessibility.jpg', '2026-03-03 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 0, '2026-03-01 15:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000009', 'bc100000-0000-0000-0000-000000000004', 'u8000000-0000-0000-0000-000000000002', 'Email Marketing in the AI Age', 'email-marketing-in-the-ai-age', 'How to leverage AI tools to create more personalized and effective email marketing campaigns.', '<h2>AI Email Marketing</h2><p>AI is transforming email marketing. Discover how to use these tools to boost your engagement rates.</p>', '/images/blog/email-ai.jpg', '/images/blog/thumb/email-ai.jpg', '2026-05-01 00:00:00', '2026-11-01 23:59:59', 'DRAFT', 0, '2026-03-13 08:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000010', 'bc100000-0000-0000-0000-000000000005', 'u8000000-0000-0000-0000-000000000001', 'Docker for Beginners', 'docker-for-beginners', 'A beginner-friendly guide to containerization with Docker, covering images, containers, and Docker Compose.', '<h2>Docker Tutorial</h2><p>Get started with Docker and learn how containers can simplify your development and deployment workflow.</p>', '/images/blog/docker.jpg', '/images/blog/thumb/docker.jpg', '2026-03-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 0, '2026-02-28 10:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000011', 'bc100000-0000-0000-0000-000000000001', 'u8000000-0000-0000-0000-000000000002', 'Microservices Architecture Patterns', 'microservices-architecture-patterns', 'Common architectural patterns for building scalable microservices and when to use each approach.', '<h2>Microservices Patterns</h2><p>Choose the right architectural pattern for your microservices to ensure scalability and maintainability.</p>', '/images/blog/microservices.jpg', '/images/blog/thumb/microservices.jpg', '2026-02-20 00:00:00', '2026-08-20 23:59:59', 'ACTIVE', 0, '2026-02-18 14:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000012', 'bc100000-0000-0000-0000-000000000002', 'u8000000-0000-0000-0000-000000000001', 'Startup Funding Strategies', 'startup-funding-strategies', 'Exploring different funding options for startups from bootstrapping to venture capital and everything in between.', '<h2>Funding Guide</h2><p>Navigate the complex world of startup funding with this comprehensive guide to your options.</p>', '/images/blog/startup-funding.jpg', '/images/blog/thumb/startup-funding.jpg', '2026-02-15 00:00:00', '2026-08-15 23:59:59', 'READY', 0, '2026-02-13 09:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000013', 'bc100000-0000-0000-0000-000000000004', 'u8000000-0000-0000-0000-000000000002', 'Social Media Analytics Deep Dive', 'social-media-analytics-deep-dive', 'Understanding social media metrics that matter and how to use analytics to improve your marketing ROI.', '<h2>Social Analytics</h2><p>Stop guessing and start measuring. Learn which social media metrics actually drive business results.</p>', '/images/blog/social-analytics.jpg', '/images/blog/thumb/social-analytics.jpg', '2026-01-15 00:00:00', '2026-02-28 23:59:59', 'EXPIRED', 0, '2026-01-12 11:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000014', 'bc100000-0000-0000-0000-000000000003', 'u8000000-0000-0000-0000-000000000001', 'Color Theory for Web Designers', 'color-theory-for-web-designers', 'Mastering color theory to create visually appealing and effective web designs that resonate with users.', '<h2>Color Theory</h2><p>Color is one of the most powerful tools in a designer toolkit. Learn to use it effectively.</p>', '/images/blog/color-theory.jpg', '/images/blog/thumb/color-theory.jpg', '2025-12-01 00:00:00', '2026-01-31 23:59:59', 'EXPIRED', 0, '2025-11-28 16:00:00', 'editor'),
  ('bp100000-0000-0000-0000-000000000015', 'bc100000-0000-0000-0000-000000000005', 'u8000000-0000-0000-0000-000000000002', 'PostgreSQL Performance Tuning', 'postgresql-performance-tuning', 'Advanced techniques for optimizing PostgreSQL database performance including indexing, query planning, and configuration.', '<h2>PostgreSQL Tuning</h2><p>Squeeze every bit of performance from your PostgreSQL database with these advanced techniques.</p>', '/images/blog/postgres-tuning.jpg', '/images/blog/thumb/postgres-tuning.jpg', '2026-03-18 00:00:00', '2026-09-18 23:59:59', 'READY', 0, '2026-03-14 08:00:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-BLG-001: Navigate to Blog Categories Page

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-BLG-001                                                           |
| Title            | Navigate to the blog categories management page                       |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000108                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Blog" in the sidebar, then "Categories".  | The blog categories list page is displayed.                          |
| 2    | Verify the list shows category name, description, edit link, and delete link. | All columns and action links are visible.                           |
| 3    | Verify a "Create Category" button is present.       | A button to create a new category is visible.                        |

---

#### NAV-BLG-002: Navigate to Blog Posts Page

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-BLG-002                                                           |
| Title            | Navigate to the blog posts management page                            |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000102                                                             |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Blog" in the sidebar, then "Posts".       | The blog posts list page is displayed.                               |
| 2    | Verify the page shows posts in a card grid layout (3 columns). | Cards are displayed in a 3-column grid.                             |
| 3    | Verify each card shows: thumbnail, category name, title, summary, author, dates, status, edit link, and delete link. | All card elements are visible.                                      |
| 4    | Verify the list is ordered by effective date descending. | Most recent effective date appears first.                           |

---

### 6.2 CRUD Tests

#### CRUD-BLG-001: Create Blog Category

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-001                                                          |
| Title            | Create a new blog category                                            |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000105                                                             |
| Preconditions    | User is logged in as EDITOR. Categories list is displayed.            |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Create Category" button.                     | The create category form is displayed.                               |
| 2    | Enter "DevOps" in the name field.                   | Name is populated.                                                   |
| 3    | Enter "Articles about CI/CD pipelines, infrastructure automation, and cloud deployment practices." in the description field. | Description is populated.                                           |
| 4    | Click "Save".                                       | Success message displayed. "DevOps" appears in the categories list.  |

---

#### CRUD-BLG-002: Edit Blog Category

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-002                                                          |
| Title            | Edit an existing blog category                                        |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000105                                                             |
| Preconditions    | Seeded category "Tutorials" exists.                                   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "Tutorials" category.           | Edit form with pre-populated data is displayed.                      |
| 2    | Change the description to "Hands-on guides and tutorials for developers at all skill levels." | Description is updated.                                             |
| 3    | Click "Save".                                       | Success message displayed. Updated description is shown in the list. |

---

#### CRUD-BLG-003: Delete Category Blocked When Posts Exist (CONSA0036)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-003                                                          |
| Title            | Delete category is blocked when blog posts are associated             |
| Type             | CRUD (Delete)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000108, CONSA0036                                                  |
| Preconditions    | Category "Technology" has associated blog posts.                      |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Technology" category.        | A confirmation dialog is displayed.                                  |
| 2    | Confirm the deletion.                               | An error message is displayed: "Cannot delete category. There are blog posts associated with this category." |
| 3    | Verify the category still exists in the list.       | "Technology" category remains in the categories list.                |
| 4    | Verify the category still exists in the database.   | Record exists in `blog_categories` with name "Technology".           |

---

#### CRUD-BLG-004: Create Blog Post with Auto-Slug

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-004                                                          |
| Title            | Create a new blog post with auto-generated slug                       |
| Type             | CRUD (Create)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000096, NFRA00111, NFRA00114, NFRA00117, NFRA00120, NFRA00126, NFRA00129, CONSA0030, CONSA0033, CONSA0039 |
| Preconditions    | User is logged in as EDITOR. At least one category exists. A valid 1600x500 image is available. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to blog posts page and click "Create Post". | The create blog post form is displayed.                             |
| 2    | Select "Technology" from the category dropdown.     | Category is selected (CONSA0033 satisfied).                          |
| 3    | Enter "Kubernetes Deployment Strategies" in the title field. | Title is populated (35 chars, within 100 limit).                    |
| 4    | Enter "An overview of blue-green, canary, and rolling deployment strategies for Kubernetes clusters." in the summary field. | Summary is populated (within 300 limit).                            |
| 5    | Enter rich text content in the content editor using bold, headings, and bullet points. | Content editor accepts rich text formatting (NFRA00129).            |
| 6    | Select "Blog Editor1" from the author dropdown.     | Author with EDITOR role is selected (CONSA0039 satisfied).           |
| 7    | Upload a 1600x500 pixel image.                      | Image is accepted.                                                   |
| 8    | Set effective date to `2026-04-01`.                 | Effective date is set.                                               |
| 9    | Set expiration date to `2026-10-01`.                | Expiration date is set.                                              |
| 10   | Leave status as "DRAFT" (default).                  | Status is DRAFT.                                                     |
| 11   | Click "Save".                                       | Success message displayed. New post appears in the list.             |
| 12   | Verify slug was auto-generated.                     | Database record has slug `kubernetes-deployment-strategies` (NFRA00126). |
| 13   | Verify thumbnail (400x125) was auto-generated.      | Database record has `thumbnail_path` populated (NFRA00114).          |

---

#### CRUD-BLG-005: Edit Blog Post

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-005                                                          |
| Title            | Edit an existing blog post                                            |
| Type             | CRUD (Update)                                                         |
| Priority         | High                                                                  |
| Source           | USA000096                                                             |
| Preconditions    | Seeded post "Building REST APIs with Spring Boot" (READY) exists.     |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Edit" on the "Building REST APIs" post card. | Edit form with pre-populated data is displayed.                      |
| 2    | Change the summary to "A complete tutorial on building production-ready REST APIs with Spring Boot, including authentication and testing." | Summary is updated.                                                 |
| 3    | Change status from "READY" to "ACTIVE".             | Status is updated.                                                   |
| 4    | Click "Save".                                       | Success message displayed. List shows updated summary and status.    |

---

#### CRUD-BLG-006: Delete Blog Post

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-BLG-006                                                          |
| Title            | Delete a blog post with confirmation                                  |
| Type             | CRUD (Delete)                                                         |
| Priority         | High                                                                  |
| Source           | USA000099                                                             |
| Preconditions    | Seeded post "Color Theory for Web Designers" (EXPIRED) exists.        |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click "Delete" on the "Color Theory for Web Designers" card. | A confirmation dialog is displayed.                                 |
| 2    | Click "Cancel".                                     | Dialog closes. Post remains in the list.                             |
| 3    | Click "Delete" again and confirm.                   | Success message displayed. Post is removed from the list.            |
| 4    | Verify deletion in the database.                    | No record with slug `color-theory-for-web-designers` in `blog_posts`.|

---

### 6.3 Search/Filter Tests

#### SRCH-BLG-001: Filter Posts by Status

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-BLG-001                                                          |
| Title            | Filter blog posts by status                                           |
| Type             | Search/Filter                                                         |
| Priority         | High                                                                  |
| Source           | USA000102                                                             |
| Preconditions    | Seeded posts with DRAFT, READY, ACTIVE, and EXPIRED statuses exist.   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the blog posts list page.               | All posts are displayed.                                             |
| 2    | Select "ACTIVE" from the status filter.             | Only posts with ACTIVE status are displayed (8 posts).               |
| 3    | Select "DRAFT" from the status filter.              | Only posts with DRAFT status are displayed (2 posts).                |
| 4    | Select "READY" from the status filter.              | Only posts with READY status are displayed (3 posts).                |
| 5    | Select "EXPIRED" from the status filter.            | Only posts with EXPIRED status are displayed (2 posts).              |
| 6    | Clear the filter.                                   | All posts are displayed again.                                       |

---

#### SRCH-BLG-002: Filter Posts by Date

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | SRCH-BLG-002                                                          |
| Title            | Filter blog posts by effective and expiration dates                   |
| Type             | Search/Filter                                                         |
| Priority         | Medium                                                                |
| Source           | USA000102                                                             |
| Preconditions    | Seeded posts with various effective and expiration dates.              |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the blog posts list page.               | All posts are displayed.                                             |
| 2    | Set the effective date filter to `2026-03-01`.      | Only posts with effective date on or after 2026-03-01 are shown.     |
| 3    | Set the expiration date filter to `2026-06-30`.     | Only posts with expiration date on or before 2026-06-30 are shown.   |
| 4    | Clear all date filters.                             | All posts are displayed again.                                       |

---

### 6.4 Validation Tests

#### VAL-BLG-001: Category Required Before Post (CONSA0033)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-BLG-001                                                           |
| Title            | Blog post cannot be created without selecting a category              |
| Type             | Validation                                                            |
| Priority         | Critical                                                              |
| Source           | CONSA0033                                                             |
| Preconditions    | Create blog post form is displayed.                                   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Fill in all fields except category (leave unselected). | All fields except category are populated.                           |
| 2    | Click "Save".                                       | Validation error "Please select a category" is displayed.            |
| 3    | Select a category and submit.                       | Category validation passes.                                          |

---

#### VAL-BLG-002: Author Must Be Editor (CONSA0039)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-BLG-002                                                           |
| Title            | Blog post author must have EDITOR role                                |
| Type             | Validation                                                            |
| Priority         | Critical                                                              |
| Source           | CONSA0039                                                             |
| Preconditions    | Create blog post form is displayed. Users with USER and EDITOR roles exist. |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Open the author dropdown.                           | Only users with EDITOR role are listed in the dropdown.              |
| 2    | Verify "Blog UserOnly" (USER role) is NOT in the list. | Users with USER role are excluded from author selection.             |
| 3    | Verify "Blog Editor1" and "Blog Editor2" ARE in the list. | Users with EDITOR role are available for selection.                  |

---

#### VAL-BLG-003: Title Max 100 Characters

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-BLG-003                                                           |
| Title            | Blog post title must not exceed 100 characters                        |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00117                                                             |
| Preconditions    | Create blog post form is displayed.                                   |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter a title with 101 characters.                  | The 101st character is blocked or a validation error is displayed.   |
| 2    | Enter a title with exactly 100 characters.          | Input is accepted without errors.                                    |

---

#### VAL-BLG-004: Unique Slug Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-BLG-004                                                           |
| Title            | Auto-generated blog slug must be unique                               |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00126                                                             |
| Preconditions    | Seeded post with slug `docker-for-beginners` exists.                  |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Create a new blog post with title "Docker for Beginners". | Form is submitted.                                                  |
| 2    | Verify the system handles the duplicate slug.       | Either an error is shown, or the slug is auto-modified to be unique (e.g., `docker-for-beginners-1`). |
| 3    | Verify no duplicate slugs exist in the database.    | `SELECT slug, COUNT(*) FROM blog_posts GROUP BY slug HAVING COUNT(*) > 1;` returns no rows. |

---

### 6.5 Pagination Tests

#### PAGE-BLG-001: Blog Categories Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-BLG-001                                                          |
| Title            | Blog categories list supports pagination                              |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000108                                                             |
| Preconditions    | Enough categories exist to trigger pagination (seed additional if needed). |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the blog categories list.               | First page of categories is displayed.                               |
| 2    | Verify pagination controls are visible (if enough records exist). | Page numbers or Next/Previous buttons are displayed.                |
| 3    | Click "Next" or page 2.                             | Next page of categories is displayed.                                |

---

#### PAGE-BLG-002: Blog Posts Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-BLG-002                                                          |
| Title            | Blog posts list supports pagination                                   |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000102                                                             |
| Preconditions    | 15 blog posts are seeded (enough for at least 2 pages in 3-col grid). |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the blog posts list page.               | First page of posts is displayed (e.g., 9 cards for 3-col grid).    |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | Remaining posts are displayed.                                       |
| 4    | Click "Previous" or page 1.                         | First page is displayed again.                                       |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete seeded blog posts first (FK dependency on blog_categories and users)
DELETE FROM blog_posts WHERE id IN (
  'bp100000-0000-0000-0000-000000000001',
  'bp100000-0000-0000-0000-000000000002',
  'bp100000-0000-0000-0000-000000000003',
  'bp100000-0000-0000-0000-000000000004',
  'bp100000-0000-0000-0000-000000000005',
  'bp100000-0000-0000-0000-000000000006',
  'bp100000-0000-0000-0000-000000000007',
  'bp100000-0000-0000-0000-000000000008',
  'bp100000-0000-0000-0000-000000000009',
  'bp100000-0000-0000-0000-000000000010',
  'bp100000-0000-0000-0000-000000000011',
  'bp100000-0000-0000-0000-000000000012',
  'bp100000-0000-0000-0000-000000000013',
  'bp100000-0000-0000-0000-000000000014',
  'bp100000-0000-0000-0000-000000000015'
);

-- Delete any blog posts created during tests
DELETE FROM blog_posts WHERE slug = 'kubernetes-deployment-strategies';

-- Delete seeded blog categories (after posts are deleted)
DELETE FROM blog_categories WHERE id IN (
  'bc100000-0000-0000-0000-000000000001',
  'bc100000-0000-0000-0000-000000000002',
  'bc100000-0000-0000-0000-000000000003',
  'bc100000-0000-0000-0000-000000000004',
  'bc100000-0000-0000-0000-000000000005'
);

-- Delete any categories created during tests
DELETE FROM blog_categories WHERE name = 'DevOps';

-- Delete seeded users
DELETE FROM users WHERE id IN (
  'u8000000-0000-0000-0000-000000000001',
  'u8000000-0000-0000-0000-000000000002',
  'u8000000-0000-0000-0000-000000000003'
);
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000096 | USA000099 | USA000102 | USA000105 | USA000108 | NFRA00111 | NFRA00114 | NFRA00117 | NFRA00120 | NFRA00123 | NFRA00126 | NFRA00129 | NFRA00132 | CONSA0030 | CONSA0033 | CONSA0036 | CONSA0039 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-BLG-001    | Navigation    |           |           |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |
| NAV-BLG-002    | Navigation    |           |           | X         |           |           |           |           |           |           | X         |           |           |           |           |           |           |           |
| CRUD-BLG-001   | CRUD (Create) |           |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |           |
| CRUD-BLG-002   | CRUD (Update) |           |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |           |
| CRUD-BLG-003   | CRUD (Delete) |           |           |           |           | X         |           |           |           |           |           |           |           |           |           |           | X         |           |
| CRUD-BLG-004   | CRUD (Create) | X         |           |           |           |           | X         | X         | X         | X         |           | X         | X         |           | X         | X         |           | X         |
| CRUD-BLG-005   | CRUD (Update) | X         |           |           |           |           |           |           |           |           |           |           |           |           | X         |           |           |           |
| CRUD-BLG-006   | CRUD (Delete) |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |           |           |           |
| SRCH-BLG-001   | Search/Filter |           |           | X         |           |           |           |           |           |           |           |           |           |           | X         |           |           |           |
| SRCH-BLG-002   | Search/Filter |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |           |           |
| VAL-BLG-001    | Validation    |           |           |           |           |           |           |           |           |           |           |           |           |           |           | X         |           |           |
| VAL-BLG-002    | Validation    |           |           |           |           |           |           |           |           |           |           |           |           |           |           |           |           | X         |
| VAL-BLG-003    | Validation    |           |           |           |           |           |           |           | X         |           |           |           |           |           |           |           |           |           |
| VAL-BLG-004    | Validation    |           |           |           |           |           |           |           |           |           |           | X         |           |           |           |           |           |           |
| PAGE-BLG-001   | Pagination    |           |           |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |
| PAGE-BLG-002   | Pagination    |           |           | X         |           |           |           |           |           |           |           |           |           |           |           |           |           |           |
