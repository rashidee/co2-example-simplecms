# Module Model: Hero Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Hero Section | HRS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| HeroSection | Read Model | Hero Section | imagePath, headline, subheadline, ctaUrl, ctaText, effectiveDate, expirationDate, status | — | USL000003 | v1.0.0 |

## 3. Base Entity Specification

This is a read-only model. The Landing Page reads from the `hero_sections` table created by the Admin Portal. No audit write fields (createdBy, updatedBy) are needed for the Landing Page context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 HeroSection

> Reads from the `hero_sections` table. Filtered by `status = ACTIVE` and `effectiveDate <= NOW() AND expirationDate > NOW()`.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| imagePath | String | No | Max 500 chars. Path to hero banner image (1600x500 pixels). | EXPLICIT | USL000003 | v1.0.0 |
| headline | String | No | Max 100 chars | EXPLICIT | USL000003 | v1.0.0 |
| subheadline | String | No | Max 200 chars | EXPLICIT | USL000003 | v1.0.0 |
| ctaUrl | String | No | Max 500 chars, valid URL format | EXPLICIT | USL000003 | v1.0.0 |
| ctaText | String | No | Max 50 chars | EXPLICIT | USL000003 | v1.0.0 |
| effectiveDate | Instant | No | Used for date-range filtering | EXPLICIT | USL000003 | v1.0.0 |
| expirationDate | Instant | No | Must be after effectiveDate | EXPLICIT | USL000003 | v1.0.0 |
| status | HeroSectionStatus (Enum) | No | Filtered to ACTIVE only | EXPLICIT | USL000003 | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships in this module_ | | | | | | | |

## 6. Enum Definitions

### 6.1 HeroSectionStatus

> Reuses the same enum as Admin Portal. Landing Page only queries for ACTIVE records.

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Content is being prepared (not visible on landing page) | USL000003 | v1.0.0 |
| READY | Content is reviewed and ready to go live (not visible on landing page) | USL000003 | v1.0.0 |
| ACTIVE | Content is live and displayed on the landing page | USL000003 | v1.0.0 |
| EXPIRED | Content has passed its expiration date (not visible on landing page) | USL000003 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| _No domain events — this is a read-only module_ | | | | |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Hero Section | HeroSection (Read Model) | Displays the active hero banner content on the public landing page, filtered by status and date range |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000003 | HeroSection | The Landing Page reads from the same `hero_sections` table managed by the Admin Portal. Only records with status=ACTIVE and within the effectiveDate/expirationDate range are displayed. If multiple active records exist within the date range, the most recently created one is shown. | Should all active hero sections within the date range be rotated (carousel), or only the latest? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | HeroSection | Read-only hero banner entity for landing page display | USL000003 |
| Enum (Shared) | HeroSectionStatus | DRAFT, READY, ACTIVE, EXPIRED — shared with Admin Portal | USL000003 |
