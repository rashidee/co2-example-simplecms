# Module Model Reference

> The Landing Page application shares the same `cms_db` PostgreSQL database with the Admin Portal. All data models (entities, relationships, ERDs) are defined and maintained by the Admin Portal. This document serves as a cross-reference to those models.

## Shared Database

- **Database**: `cms_db` (PostgreSQL 14)
- **Schema Owner**: Admin Portal — all tables are created and migrated via the Admin Portal's Flyway migrations
- **Landing Page Access**: Read-only for all modules except Contact Section (which writes contact form submissions)

## Model Reference

All module models are defined in the Admin Portal's context folder. Refer to the following for entity definitions, ERD diagrams, and field specifications:

| # | Module | Prefix | Admin Model Path | Access Mode |
|---|--------|--------|-----------------|-------------|
| 1 | Hero Section | HRS | [admin/context/model/hero-section/model.md](../../../admin/context/model/hero-section/model.md) | Read-only (status=ACTIVE, within date range) |
| 2 | Product and Service Section | PAS | [admin/context/model/product-and-service-section/model.md](../../../admin/context/model/product-and-service-section/model.md) | Read-only (ordered by displayOrder) |
| 3 | Features Section | FTS | [admin/context/model/features-section/model.md](../../../admin/context/model/features-section/model.md) | Read-only (ordered by displayOrder) |
| 4 | Testimonials Section | TST | [admin/context/model/testimonials-section/model.md](../../../admin/context/model/testimonials-section/model.md) | Read-only (ordered by displayOrder) |
| 5 | Team Section | TMS | [admin/context/model/team-section/model.md](../../../admin/context/model/team-section/model.md) | Read-only (ordered by displayOrder) |
| 6 | Contact Section | CTS | [admin/context/model/contact-section/model.md](../../../admin/context/model/contact-section/model.md) | Read-only (ContactInfo) + Write (ContactMessage) |
| 7 | Blog | BLG | [admin/context/model/blog/model.md](../../../admin/context/model/blog/model.md) | Read-only (status=ACTIVE, ordered by effectiveDate desc) |

## Landing Page–Specific Notes

- **v1.0.4 Changes**: Status columns have been removed from Product/Service, Features, Testimonials, and Team tables. Content is now always visible (no status filter needed). Hero Section status is auto-computed from dates. Images are stored as BLOB (BYTEA) in the database — the Landing Page should read `image_data`/`thumbnail_data` columns directly.
- **Contact Form**: The only write operation — inserts into `cts_contact_message` table.
- **Image Serving**: For modules with BLOB image storage (Hero, Product/Service, Team, Blog), images should be read from the `image_data` and `thumbnail_data` BYTEA columns rather than filesystem paths.
