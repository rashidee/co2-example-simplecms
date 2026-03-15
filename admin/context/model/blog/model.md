# Module Model: Blog

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Blog | BLG | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| BlogCategory | Aggregate Root | Blog | name, description | 1:N with BlogPost | USA000105, USA000108, CONSA0033, CONSA0036 | v1.0.0 |
| BlogPost | Aggregate Root | Blog | title, slug, summary, content, imagePath, effectiveDate, expirationDate, status | N:1 with BlogCategory, N:1 with User (cross-module) | USA000096, USA000099, USA000102, NFRA00111-132, CONSA0030, CONSA0039 | v1.0.0 |

## 3. Base Entity Specification

All entities in this module inherit the following base fields:

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 BlogCategory

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| name | String | No | Max 100 chars, unique | EXPLICIT | USA000105 | v1.0.0 |
| description | String | Yes | Max 500 chars | EXPLICIT | USA000105 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

### 4.2 BlogPost

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| categoryId | UUID | No | FK to BlogCategory. Category must exist before creating post. | EXPLICIT | USA000096, CONSA0033 | v1.0.0 |
| authorId | UUID | No | FK to User (cross-module). Author must have Editor role. | EXPLICIT | USA000096, CONSA0039 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USA000096, NFRA00117 | v1.0.0 |
| slug | String | No | Max 255 chars, unique. Auto-generated from title. | OPERATION_INFERENCE | NFRA00126 | v1.0.0 |
| summary | String | No | Max 300 chars | EXPLICIT | USA000096, NFRA00120 | v1.0.0 |
| content | String (Text) | No | Rich text HTML content. Sanitized to prevent XSS. | EXPLICIT | USA000096, NFRA00129, NFRA00132 | v1.0.0 |
| imagePath | String | No | Max 500 chars. Original image must be exactly 1600x500 pixels. | EXPLICIT | USA000096, NFRA00111 | v1.0.0 |
| thumbnailPath | String | Yes | Max 500 chars. Auto-generated at 400x125 pixels. | OPERATION_INFERENCE | NFRA00114 | v1.0.0 |
| effectiveDate | Instant | No | Must be a valid date | EXPLICIT | USA000096 | v1.0.0 |
| expirationDate | Instant | No | Must be after effectiveDate | EXPLICIT | USA000096 | v1.0.0 |
| status | BlogPostStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000096, CONSA0030 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| BlogCategory | BlogPost | 1:N | — | RESTRICT DELETE | Category cannot be deleted if it has associated blog posts | CONSA0033, CONSA0036 | v1.0.0 |
| BlogPost | User (User module) | N:1 | — | NO ACTION | Author must be a user with Editor role; cross-module reference | USA000096, CONSA0039 | v1.0.0 |

## 6. Enum Definitions

### 6.1 BlogPostStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0030 | v1.0.0 |
| READY | Content is reviewed and ready to go live | CONSA0030 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0030 | v1.0.0 |
| EXPIRED | Content has passed its expiration date | CONSA0030 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| BlogCategoryCreated | USA000105 | BlogCategory | categoryId, name | v1.0.0 |
| BlogCategoryUpdated | USA000105 | BlogCategory | categoryId, changedFields | v1.0.0 |
| BlogCategoryDeleted | USA000108 | BlogCategory | categoryId | v1.0.0 |
| BlogPostCreated | USA000096 | BlogPost | postId, title, categoryId, authorId, status | v1.0.0 |
| BlogPostUpdated | USA000096 | BlogPost | postId, changedFields | v1.0.0 |
| BlogPostDeleted | USA000099 | BlogPost | postId | v1.0.0 |
| BlogPostExpired | USA000096 | BlogPost | postId, expirationDate | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Blog | BlogCategory, BlogPost | Manages blog content with categories, rich text editing, image management, and date-based lifecycle |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| NFRA00126 | BlogPost.slug | Slug is auto-generated from the title using standard slugification (lowercase, hyphens, stripped special chars). Uniqueness enforced at database level. | Can editors manually override the auto-generated slug? | v1.0.0 |
| NFRA00129 | BlogPost.content | Rich text editor (e.g., TinyMCE, Quill, CKEditor) will be used on the frontend. Content stored as sanitized HTML. | Which rich text editor library should be used? | v1.0.0 |
| NFRA00132 | BlogPost.content | HTML sanitization is applied on save to prevent XSS. A server-side sanitizer library will be used. | Should a whitelist-based sanitizer be used? Which HTML tags/attributes are allowed? | v1.0.0 |
| USA000096 | BlogPost.authorId | Author selection is limited to users with EDITOR role. The FK references User entity in the User module. | If an author's role is downgraded from EDITOR, what happens to their existing blog posts? | v1.0.0 |
| CONSA0036 | BlogCategory | Category deletion is blocked if any blog posts reference it. Error message shown to user. | Should there be an option to reassign posts to another category before deletion? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | BlogCategory | Blog category for organizing blog content | USA000105, USA000108, CONSA0033, CONSA0036 |
| Entity | BlogPost | Blog content entity with rich text, images, categories, and date lifecycle | USA000096-102, NFRA00111-132, CONSA0030, CONSA0039 |
| Enum | BlogPostStatus | DRAFT, READY, ACTIVE, EXPIRED status lifecycle | CONSA0030 |
