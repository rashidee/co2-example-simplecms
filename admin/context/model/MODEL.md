# Module Model Summary

> Generated from 93 tagged items (36 user stories, 44 NFRs, 13 constraints) across 9 modules.

## Modules

| # | Module | Prefix | Entities | Relationships | Versions | Stories |
|---|--------|--------|----------|---------------|----------|---------|
| 1 | Authentication and Authorization | AAA | 1 | 1 (cross-module) | v1.0.0 | 4 |
| 2 | User | USR | 1 | 0 | v1.0.0 | 13 |
| 3 | Hero Section | HRS | 1 | 0 | v1.0.0 | 9 |
| 4 | Product and Service Section | PAS | 1 | 0 | v1.0.0 | 11 |
| 5 | Features Section | FTS | 1 | 0 | v1.0.0 | 7 |
| 6 | Testimonials Section | TST | 1 | 0 | v1.0.0 | 8 |
| 7 | Team Section | TMS | 1 | 0 | v1.0.0 | 9 |
| 8 | Contact Section | CTS | 3 | 1 | v1.0.0 | 9 |
| 9 | Blog | BLG | 2 | 2 (1 cross-module) | v1.0.0 | 14 |

## Table of Contents

### Authentication and Authorization (`AAA`)

> Manages user authentication flows including login, logout, and password reset.

- **Model Documentation:** [authentication-and-authorization/model.md](./authentication-and-authorization/model.md)
- **ERD Diagram:** [authentication-and-authorization/erd.mermaid](./authentication-and-authorization/erd.mermaid)
- **Entities:** PasswordResetToken
- **Key Relationships:** PasswordResetToken belongs to User (cross-module)

### User (`USR`)

> Manages user accounts, profiles, roles, and status for the admin portal.

- **Model Documentation:** [user/model.md](./user/model.md)
- **ERD Diagram:** [user/erd.mermaid](./user/erd.mermaid)
- **Entities:** User
- **Key Relationships:** Referenced by PasswordResetToken (Auth module), BlogPost (Blog module)

### Hero Section (`HRS`)

> Manages hero banner content with images, text, CTAs, and date-based lifecycle.

- **Model Documentation:** [hero-section/model.md](./hero-section/model.md)
- **ERD Diagram:** [hero-section/erd.mermaid](./hero-section/erd.mermaid)
- **Entities:** HeroSection
- **Key Relationships:** No intra-module relationships

### Product and Service Section (`PAS`)

> Manages product and service showcase content with images, descriptions, optional CTAs, and display ordering.

- **Model Documentation:** [product-and-service-section/model.md](./product-and-service-section/model.md)
- **ERD Diagram:** [product-and-service-section/erd.mermaid](./product-and-service-section/erd.mermaid)
- **Entities:** ProductService
- **Key Relationships:** No intra-module relationships

### Features Section (`FTS`)

> Manages feature highlight content with FontAwesome icons, descriptions, and display ordering.

- **Model Documentation:** [features-section/model.md](./features-section/model.md)
- **ERD Diagram:** [features-section/erd.mermaid](./features-section/erd.mermaid)
- **Entities:** Feature
- **Key Relationships:** No intra-module relationships

### Testimonials Section (`TST`)

> Manages customer testimonials with ratings, reviews, and display ordering.

- **Model Documentation:** [testimonials-section/model.md](./testimonials-section/model.md)
- **ERD Diagram:** [testimonials-section/erd.mermaid](./testimonials-section/erd.mermaid)
- **Entities:** Testimonial
- **Key Relationships:** No intra-module relationships

### Team Section (`TMS`)

> Manages team member profiles with photos, roles, LinkedIn links, and display ordering.

- **Model Documentation:** [team-section/model.md](./team-section/model.md)
- **ERD Diagram:** [team-section/erd.mermaid](./team-section/erd.mermaid)
- **Entities:** TeamMember
- **Key Relationships:** No intra-module relationships

### Contact Section (`CTS`)

> Manages company contact information and handles incoming messages with response tracking.

- **Model Documentation:** [contact-section/model.md](./contact-section/model.md)
- **ERD Diagram:** [contact-section/erd.mermaid](./contact-section/erd.mermaid)
- **Entities:** ContactInfo, ContactMessage, ContactResponse
- **Key Relationships:** ContactMessage has many ContactResponses; ContactResponse belongs to ContactMessage

### Blog (`BLG`)

> Manages blog content with categories, rich text editing, image management, and date-based lifecycle.

- **Model Documentation:** [blog/model.md](./blog/model.md)
- **ERD Diagram:** [blog/erd.mermaid](./blog/erd.mermaid)
- **Entities:** BlogCategory, BlogPost
- **Key Relationships:** BlogCategory has many BlogPosts; BlogPost references User as author (cross-module)

## Cross-Module Dependencies

| Source Module | Target Module | Dependency | Noted In |
|---------------|---------------|------------|----------|
| Authentication and Authorization | User | PasswordResetToken references User entity via userId FK | authentication-and-authorization/model.md §5 |
| Blog | User | BlogPost references User entity via authorId FK (must have Editor role) | blog/model.md §5, CONSA0039 |

## Assumptions Summary

| Module | Count | Critical |
|--------|-------|----------|
| Authentication and Authorization | 3 | 1 (token expiry duration) |
| User | 5 | 2 (role list confirmation, delete behavior) |
| Hero Section | 2 | 1 (storage mechanism) |
| Product and Service Section | 2 | 0 |
| Features Section | 1 | 1 (icon list storage) |
| Testimonials Section | 1 | 0 |
| Team Section | 1 | 0 |
| Contact Section | 4 | 2 (email failure handling, batch frequency) |
| Blog | 5 | 2 (rich text editor choice, author role downgrade) |

## Update History

| Version | Date | Modules Affected | Added | Modified | Removed | Flagged |
|---|---|---|---|---|---|---|
| v1.0.0 | 2026-03-15 | All (9 modules) | 12 entities, 8 enums, 24 domain events | — | — | 24 assumptions |
