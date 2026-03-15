# Module Model Summary

> Landing Page application models for 7 modules. These models describe the same PostgreSQL database tables (cms_db) as the Admin Portal, but from a public-facing, read-only perspective. Only the Contact Section writes data (contact form submissions).

## Shared Database Notice

The Landing Page shares the `cms_db` PostgreSQL database with the Admin Portal. Tables are created and managed by the Admin Portal. The Landing Page reads from these tables (filtering by `status = ACTIVE` and applicable date ranges) and only writes to the `contact_messages` table via the contact form.

## Modules

| # | Module | Prefix | Entities | Relationships | Access Mode | Version | Source Stories |
|---|--------|--------|----------|---------------|-------------|---------|----------------|
| 1 | Hero Section | HRS | 1 | 0 | Read-only | v1.0.0 | USL000003 |
| 2 | Product and Service Section | PAS | 1 | 0 | Read-only | v1.0.0 | USL000006 |
| 3 | Features Section | FTS | 1 | 0 | Read-only | v1.0.0 | USL000009 |
| 4 | Testimonials Section | TST | 1 | 0 | Read-only | v1.0.0 | USL000012 |
| 5 | Team Section | TMS | 1 | 0 | Read-only | v1.0.0 | USL000015 |
| 6 | Contact Section | CTS | 2 | 0 | Read-only + Write (form) | v1.0.0 | USL000018 |
| 7 | Blog | BLG | 2 | 1 | Read-only | v1.0.0 | USL000021, USL000024 |

## Table of Contents

### Hero Section (`HRS`)

> Displays the active hero banner with image, headline, sub-headline, and call-to-action on the landing page.

- **Model Documentation:** [hero-section/model.md](./hero-section/model.md)
- **ERD Diagram:** [hero-section/erd.mermaid](./hero-section/erd.mermaid)
- **Entities:** HeroSection
- **Access Mode:** Read-only (status=ACTIVE, within date range)

### Product and Service Section (`PAS`)

> Displays active products and services with images, descriptions, and optional CTAs, ordered by display order.

- **Model Documentation:** [product-and-service-section/model.md](./product-and-service-section/model.md)
- **ERD Diagram:** [product-and-service-section/erd.mermaid](./product-and-service-section/erd.mermaid)
- **Entities:** ProductService
- **Access Mode:** Read-only (status=ACTIVE, ordered by displayOrder)

### Features Section (`FTS`)

> Displays active features with icons, titles, and descriptions, ordered by display order.

- **Model Documentation:** [features-section/model.md](./features-section/model.md)
- **ERD Diagram:** [features-section/erd.mermaid](./features-section/erd.mermaid)
- **Entities:** Feature
- **Access Mode:** Read-only (status=ACTIVE, ordered by displayOrder)

### Testimonials Section (`TST`)

> Displays active customer testimonials with ratings, ordered by display order.

- **Model Documentation:** [testimonials-section/model.md](./testimonials-section/model.md)
- **ERD Diagram:** [testimonials-section/erd.mermaid](./testimonials-section/erd.mermaid)
- **Entities:** Testimonial
- **Access Mode:** Read-only (status=ACTIVE, ordered by displayOrder)

### Team Section (`TMS`)

> Displays active team member profiles with photos, roles, and LinkedIn links, ordered by display order.

- **Model Documentation:** [team-section/model.md](./team-section/model.md)
- **ERD Diagram:** [team-section/erd.mermaid](./team-section/erd.mermaid)
- **Entities:** TeamMember
- **Access Mode:** Read-only (status=ACTIVE, ordered by displayOrder)

### Contact Section (`CTS`)

> Displays company contact information and provides a contact form for visitors to submit messages.

- **Model Documentation:** [contact-section/model.md](./contact-section/model.md)
- **ERD Diagram:** [contact-section/erd.mermaid](./contact-section/erd.mermaid)
- **Entities:** ContactInfo, ContactMessage
- **Access Mode:** Read-only (ContactInfo) + Write (ContactMessage form submission)

### Blog (`BLG`)

> Displays active blog posts with category filtering and SEO-friendly URLs, ordered by effective date descending.

- **Model Documentation:** [blog/model.md](./blog/model.md)
- **ERD Diagram:** [blog/erd.mermaid](./blog/erd.mermaid)
- **Entities:** BlogCategory, BlogPost
- **Key Relationships:** BlogCategory has many BlogPosts (cross-module dependency within Blog)
- **Access Mode:** Read-only (status=ACTIVE, ordered by effectiveDate desc)

## Cross-Module Dependencies

| Source Module | Target Module | Dependency | Noted In |
|---------------|---------------|------------|----------|
| Blog | Blog | BlogPost references BlogCategory via categoryId FK | blog/model.md S5 |

## Assumptions Summary

| Module | Count | Critical |
|--------|-------|----------|
| Hero Section | 1 | 0 |
| Product and Service Section | 1 | 0 |
| Features Section | 1 | 0 |
| Testimonials Section | 1 | 0 |
| Team Section | 1 | 0 |
| Contact Section | 2 | 1 (form validation and spam prevention) |
| Blog | 2 | 1 (SEO slug uniqueness) |

## Update History

| Version | Date | Modules Affected | Added | Modified | Removed | Flagged |
|---|---|---|---|---|---|---|
| v1.0.0 | 2026-03-15 | All (7 modules) | 9 entities, 6 enums, 1 domain event | — | — | 9 assumptions |
