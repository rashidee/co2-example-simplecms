# Module Model: Blog

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Blog | BLG | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| BlogCategory | Read Model | Blog | name | 1:N with BlogPost | USL000021 | v1.0.0 |
| BlogPost | Read Model | Blog | categoryId, title, slug, summary, content, imagePath, effectiveDate, status | N:1 with BlogCategory | USL000021, USL000024 | v1.0.0 |

## 3. Base Entity Specification

This is a read-only model. The Landing Page reads from the `blog_categories` and `blog_posts` tables created by the Admin Portal. No audit write fields are needed for the Landing Page context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 BlogCategory

> Reads from the `blog_categories` table. Used for filtering and categorizing blog posts on the landing page.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| name | String | No | Max 100 chars, unique | EXPLICIT | USL000021 | v1.0.0 |

### 4.2 BlogPost

> Reads from the `blog_posts` table. Filtered by `status = ACTIVE`, ordered by `effectiveDate DESC`. Blog detail pages use `slug` for SEO-friendly URLs.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| categoryId | UUID | No | FK to BlogCategory | EXPLICIT | USL000021 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USL000021 | v1.0.0 |
| slug | String | No | Max 255 chars, unique. Used for SEO-friendly blog detail URLs. | EXPLICIT | USL000024 | v1.0.0 |
| summary | String | No | Max 300 chars. Displayed in blog listing cards. | EXPLICIT | USL000021 | v1.0.0 |
| content | String (Text) | No | Rich text HTML content. Rendered on blog detail page. | EXPLICIT | USL000024 | v1.0.0 |
| imagePath | String | No | Max 500 chars. Path to blog post image (1600x500 pixels). | EXPLICIT | USL000021 | v1.0.0 |
| effectiveDate | Instant | No | Used for ordering (most recent first) and display | EXPLICIT | USL000021 | v1.0.0 |
| status | BlogPostStatus (Enum) | No | Filtered to ACTIVE only | EXPLICIT | USL000021 | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| BlogCategory | BlogPost | 1:N | — | N/A (read-only) | A blog post belongs to one category; categories group posts for filtering | USL000021 | v1.0.0 |

## 6. Enum Definitions

### 6.1 BlogPostStatus

> Reuses the same enum as Admin Portal. Landing Page only queries for ACTIVE records.

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Content is being prepared (not visible on landing page) | USL000021 | v1.0.0 |
| READY | Content is reviewed and ready to go live (not visible on landing page) | USL000021 | v1.0.0 |
| ACTIVE | Content is live and displayed on the landing page | USL000021 | v1.0.0 |
| EXPIRED | Content has passed its expiration date (not visible on landing page) | USL000021 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| _No domain events — this is a read-only module_ | | | | |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Blog | BlogCategory (Read Model), BlogPost (Read Model) | Displays active blog posts with category filtering and SEO-friendly URLs on the public landing page |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000024 | BlogPost.slug | The slug field is used as the URL path segment for blog detail pages (e.g., `/blog/{slug}`). Slugs are unique and managed by the Admin Portal. The Landing Page looks up blog posts by slug for detail views. | Should the Landing Page return a 404 if a slug is not found or belongs to a non-ACTIVE post? | v1.0.0 |
| USL000021 | BlogCategory | All blog categories are displayed for filtering, even if they have no active blog posts. | Should empty categories (with no active posts) be hidden from the filter list? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | BlogCategory | Read-only blog category entity for landing page filtering | USL000021 |
| Entity (Read Model) | BlogPost | Read-only blog post entity for landing page listing and detail display | USL000021, USL000024 |
| Enum (Shared) | BlogPostStatus | DRAFT, READY, ACTIVE, EXPIRED — shared with Admin Portal | USL000021 |
