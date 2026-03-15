# E2E Test Plan: Simple CMS Landing Page

| Field | Value |
|-------|-------|
| Application | Landing Page (Simple CMS) |
| Application Initials | L |
| Version | v1.0.0 |
| Date | 2026-03-15 |
| Technology Stack | Laravel 12, Eloquent, Blade, HTMX |
| Database | PostgreSQL 14 (`cms_db`) |
| Authentication | NONE (public landing page) |

---

## 1. Application Overview

The Simple CMS Landing Page is a public-facing web application that displays marketing content and blog articles managed via the Admin Portal. The Landing Page shares the `cms_db` PostgreSQL database with the Admin Portal. All content is read-only except for the contact form submission. No authentication or authorization is required -- all visitors are anonymous public users.

### Key URLs

| Page | URL |
|------|-----|
| Home (Landing Page) | `/` |
| Blog Directory | `/blog` |
| Blog Detail | `/blog/{slug}` |

---

## 2. Test Infrastructure

### 2.1 Database Connection

All test data is seeded directly via `psql` into the shared PostgreSQL database. No application-level seeding is used.

```
Host:     localhost
Port:     5432
Database: cms_db
User:     cms_user
Password: cms_password
```

**Connection command:**

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db
```

### 2.2 Test Users

**None.** This is a public landing page with no authentication. All E2E tests execute as anonymous visitors navigating directly to `/` or `/blog`.

### 2.3 Test Data Seeding Strategy

- All test data is inserted via `psql` INSERT statements before test execution.
- Only `ACTIVE` status records (and valid date ranges where applicable) are seeded for display verification.
- Additional non-displayable records (EXPIRED, INACTIVE, DRAFT) are seeded to verify filtering logic.
- Each module's TEST_SPEC.md contains its own seeding and cleanup scripts.

---

## 3. Layer Classification

All modules are classified as **L2 (Reference Data)**. Data is seeded via `psql` into shared tables (same tables as Admin Portal). There is no authentication layer (L1 is empty), no messaging layer (L3 is empty), and no side effects layer (L4 is empty), except for the contact form write which is categorized under L2 for simplicity.

| Layer | Description | Modules |
|-------|-------------|---------|
| L1 | Authentication & Authorization | NONE (public page) |
| L2 | Reference Data (psql seeded) | Hero Section, Product & Service, Features, Testimonials, Team, Contact, Blog |
| L3 | Messaging | NONE |
| L4 | Side Effects | NONE |

---

## 4. Execution Order

All modules are L2 and independent of each other. They can be executed in any order or in parallel. The recommended execution order follows the visual layout of the landing page:

| Order | Module | Layer | Dependencies |
|-------|--------|-------|--------------|
| 1 | Hero Section | L2 | None |
| 2 | Product & Service Section | L2 | None |
| 3 | Features Section | L2 | None |
| 4 | Testimonials Section | L2 | None |
| 5 | Team Section | L2 | None |
| 6 | Contact Section | L2 | None |
| 7 | Blog | L2 | None |

---

## 5. Table of Contents

| # | Module | Test Spec | Scenarios | Source Stories |
|---|--------|-----------|-----------|---------------|
| 1 | Hero Section | [hero-section/TEST_SPEC.md](./hero-section/TEST_SPEC.md) | 6 | USL000003 |
| 2 | Product & Service Section | [product-and-service-section/TEST_SPEC.md](./product-and-service-section/TEST_SPEC.md) | 5 | USL000006 |
| 3 | Features Section | [features-section/TEST_SPEC.md](./features-section/TEST_SPEC.md) | 3 | USL000009 |
| 4 | Testimonials Section | [testimonials-section/TEST_SPEC.md](./testimonials-section/TEST_SPEC.md) | 4 | USL000012 |
| 5 | Team Section | [team-section/TEST_SPEC.md](./team-section/TEST_SPEC.md) | 4 | USL000015 |
| 6 | Contact Section | [contact-section/TEST_SPEC.md](./contact-section/TEST_SPEC.md) | 7 | USL000018 |
| 7 | Blog | [blog/TEST_SPEC.md](./blog/TEST_SPEC.md) | 9 | USL000021, USL000024 |
| | **Total** | | **38** | |

---

## 6. Module Summaries

### 6.1 Hero Section (6 scenarios)

Verifies the hero carousel displays active hero slides with images, headlines, sub-headlines, and CTA buttons. Validates auto-slide timing, arrow navigation, and filtering of expired hero content.

### 6.2 Product & Service Section (5 scenarios)

Verifies the product/service cards are displayed in a 3-column grid with images, titles, descriptions, and CTA links. Validates that inactive items are filtered out.

### 6.3 Features Section (3 scenarios)

Verifies the features grid displays icons, titles, and descriptions in a 3-column layout for all active features.

### 6.4 Testimonials Section (4 scenarios)

Verifies the testimonials carousel displays customer names, reviews, and star ratings. Validates that star icons visually match the numeric rating value.

### 6.5 Team Section (4 scenarios)

Verifies team member cards display circular profile photos, names, roles, and LinkedIn links that open in new tabs.

### 6.6 Contact Section (7 scenarios)

Verifies contact information display (phone, email, address) and contact form submission. Validates required fields, email format, message length limit, and CAPTCHA presence.

### 6.7 Blog (9 scenarios)

Verifies the blog directory page with card layout, pagination (10 per page), and blog detail pages with SEO-friendly slug URLs. Validates that expired posts are filtered out.

---

## 7. Global Setup

Run the following `psql` commands to seed all test data before executing the test suite. Each module's TEST_SPEC.md contains the detailed INSERT statements. Below is the consolidated setup sequence.

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db <<'SQL'

-- ============================================================
-- GLOBAL SETUP: Seed all test data for Landing Page E2E tests
-- ============================================================

-- 1. Hero Section (3 ACTIVE + 1 EXPIRED)
INSERT INTO hero_sections (id, image_path, headline, subheadline, cta_url, cta_text, effective_date, expiration_date, status, created_at, updated_at) VALUES
('a1000001-0000-0000-0000-000000000001', '/images/hero/slide-1.jpg', 'Innovate Your Business Today', 'Discover cutting-edge solutions that drive growth and efficiency for your company.', 'https://example.com/products', 'Explore Products', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000002', '/images/hero/slide-2.jpg', 'Trusted by Industry Leaders', 'Join thousands of businesses that rely on our services every day.', 'https://example.com/services', 'View Services', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000003', '/images/hero/slide-3.jpg', 'Start Your Free Trial', 'Experience the full power of our platform with a 30-day free trial.', 'https://example.com/trial', 'Start Trial', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', NOW(), NOW()),
('a1000001-0000-0000-0000-000000000004', '/images/hero/slide-expired.jpg', 'Holiday Special Offer', 'This limited-time offer has ended.', 'https://example.com/holiday', 'Shop Now', '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'EXPIRED', NOW(), NOW());

-- 2. Product & Service Section (4 ACTIVE + 1 INACTIVE)
INSERT INTO product_services (id, image_path, title, description, cta_url, cta_text, display_order, status, created_at, updated_at) VALUES
('b2000001-0000-0000-0000-000000000001', '/images/products/web-dev.jpg', 'Web Development', 'Custom web applications built with modern technologies to meet your business needs.', 'https://example.com/web-dev', 'Learn More', 1, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000002', '/images/products/mobile-app.jpg', 'Mobile Applications', 'Native and cross-platform mobile apps for iOS and Android devices.', 'https://example.com/mobile', 'Learn More', 2, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000003', '/images/products/cloud.jpg', 'Cloud Solutions', 'Scalable cloud infrastructure and migration services for enterprises.', 'https://example.com/cloud', 'Learn More', 3, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000004', '/images/products/consulting.jpg', 'IT Consulting', 'Expert technology consulting to align your IT strategy with business goals.', 'https://example.com/consulting', 'Learn More', 4, 'ACTIVE', NOW(), NOW()),
('b2000001-0000-0000-0000-000000000005', '/images/products/legacy.jpg', 'Legacy System Support', 'This service is temporarily unavailable.', NULL, NULL, 5, 'INACTIVE', NOW(), NOW());

-- 3. Features Section (6 ACTIVE)
INSERT INTO features (id, icon, title, description, display_order, status, created_at, updated_at) VALUES
('c3000001-0000-0000-0000-000000000001', 'fa-solid fa-bolt', 'Lightning Fast Performance', 'Our platform is optimized for speed, ensuring your applications load in milliseconds.', 1, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000002', 'fa-solid fa-shield-halved', 'Enterprise Security', 'Bank-grade security with end-to-end encryption and compliance certifications.', 2, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000003', 'fa-solid fa-cloud', 'Cloud Native', 'Built for the cloud with auto-scaling, high availability, and disaster recovery.', 3, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000004', 'fa-solid fa-headset', '24/7 Support', 'Round-the-clock customer support via chat, email, and phone.', 4, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000005', 'fa-solid fa-chart-line', 'Advanced Analytics', 'Real-time dashboards and reporting to make data-driven decisions.', 5, 'ACTIVE', NOW(), NOW()),
('c3000001-0000-0000-0000-000000000006', 'fa-solid fa-puzzle-piece', 'Easy Integration', 'Seamless integration with popular tools and APIs you already use.', 6, 'ACTIVE', NOW(), NOW());

-- 4. Testimonials Section (5 ACTIVE)
INSERT INTO testimonials (id, customer_name, customer_review, customer_rating, display_order, status, created_at, updated_at) VALUES
('d4000001-0000-0000-0000-000000000001', 'Sarah Johnson', 'Absolutely transformed our business operations. The platform is intuitive and powerful.', 5, 1, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000002', 'Michael Chen', 'Great value for money. The support team is responsive and knowledgeable.', 4, 2, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000003', 'Emily Rodriguez', 'We migrated our entire infrastructure in just two weeks. Highly recommended.', 5, 3, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000004', 'David Kim', 'The analytics features alone made it worth switching from our previous provider.', 4, 4, 'ACTIVE', NOW(), NOW()),
('d4000001-0000-0000-0000-000000000005', 'Lisa Thompson', 'Good product overall. Some features could use more customization options.', 3, 5, 'ACTIVE', NOW(), NOW());

-- 5. Team Section (6 ACTIVE)
INSERT INTO team_members (id, profile_picture_path, name, role, linkedin_url, display_order, status, created_at, updated_at) VALUES
('e5000001-0000-0000-0000-000000000001', '/images/team/john-doe.jpg', 'John Doe', 'Chief Executive Officer', 'https://linkedin.com/in/johndoe', 1, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000002', '/images/team/jane-smith.jpg', 'Jane Smith', 'Chief Technology Officer', 'https://linkedin.com/in/janesmith', 2, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000003', '/images/team/robert-wilson.jpg', 'Robert Wilson', 'Head of Product', 'https://linkedin.com/in/robertwilson', 3, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000004', '/images/team/maria-garcia.jpg', 'Maria Garcia', 'Lead Designer', 'https://linkedin.com/in/mariagarcia', 4, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000005', '/images/team/alex-nguyen.jpg', 'Alex Nguyen', 'Senior Developer', 'https://linkedin.com/in/alexnguyen', 5, 'ACTIVE', NOW(), NOW()),
('e5000001-0000-0000-0000-000000000006', '/images/team/priya-patel.jpg', 'Priya Patel', 'Marketing Manager', 'https://linkedin.com/in/priyapatel', 6, 'ACTIVE', NOW(), NOW());

-- 6. Contact Section (1 contact_info record)
INSERT INTO contact_info (id, phone_number, email_address, physical_address, created_at, updated_at) VALUES
('f6000001-0000-0000-0000-000000000001', '+1 (555) 123-4567', 'info@simplecms.com', '123 Innovation Drive, Suite 400, San Francisco, CA 94105, USA', NOW(), NOW());

-- 7. Blog (3 categories + 15 posts: 13 ACTIVE + 2 EXPIRED)
INSERT INTO blog_categories (id, name, created_at, updated_at) VALUES
('g7000001-0000-0000-0000-000000000001', 'Technology', NOW(), NOW()),
('g7000001-0000-0000-0000-000000000002', 'Business', NOW(), NOW()),
('g7000001-0000-0000-0000-000000000003', 'Design', NOW(), NOW());

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

SQL
```

---

## 8. Global Teardown

Run the following `psql` commands to clean up all test data after the test suite completes.

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db <<'SQL'

-- ============================================================
-- GLOBAL TEARDOWN: Remove all test data for Landing Page E2E tests
-- ============================================================

-- Delete in reverse dependency order
DELETE FROM contact_messages WHERE sender_email = 'testuser@example.com';
DELETE FROM blog_posts WHERE id LIKE 'h8000001-%';
DELETE FROM blog_categories WHERE id LIKE 'g7000001-%';
DELETE FROM contact_info WHERE id = 'f6000001-0000-0000-0000-000000000001';
DELETE FROM team_members WHERE id LIKE 'e5000001-%';
DELETE FROM testimonials WHERE id LIKE 'd4000001-%';
DELETE FROM features WHERE id LIKE 'c3000001-%';
DELETE FROM product_services WHERE id LIKE 'b2000001-%';
DELETE FROM hero_sections WHERE id LIKE 'a1000001-%';

SQL
```

---

## 9. Notes

- All tests target a publicly accessible landing page. No login or authentication steps are required.
- Tests navigate directly to `/` (home) or `/blog` (blog directory) or `/blog/{slug}` (blog detail).
- The Landing Page reads from tables managed by the Admin Portal. Test data is seeded to simulate content created by the Admin Portal.
- The contact form is the only write operation from the Landing Page, inserting into the `contact_messages` table.
- All UUIDs used in test data follow a deterministic pattern for easy identification and cleanup.
