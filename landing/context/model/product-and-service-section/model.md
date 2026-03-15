# Module Model: Product and Service Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Product and Service Section | PAS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| ProductService | Read Model | Product and Service Section | imagePath, title, description, ctaUrl, ctaText, displayOrder, status | — | USL000006 | v1.0.0 |

## 3. Base Entity Specification

This is a read-only model. The Landing Page reads from the `product_services` table created by the Admin Portal. No audit write fields are needed for the Landing Page context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 ProductService

> Reads from the `product_services` table. Filtered by `status = ACTIVE`, ordered by `displayOrder ASC`.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| imagePath | String | No | Max 500 chars. Path to product/service image (400x400 pixels). | EXPLICIT | USL000006 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USL000006 | v1.0.0 |
| description | String | No | Max 500 chars | EXPLICIT | USL000006 | v1.0.0 |
| ctaUrl | String | Yes | Max 500 chars, valid URL format if provided | EXPLICIT | USL000006 | v1.0.0 |
| ctaText | String | Yes | Max 50 chars if provided | EXPLICIT | USL000006 | v1.0.0 |
| displayOrder | Integer | No | Used for display sorting | EXPLICIT | USL000006 | v1.0.0 |
| status | ProductServiceStatus (Enum) | No | Filtered to ACTIVE only | EXPLICIT | USL000006 | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships in this module_ | | | | | | | |

## 6. Enum Definitions

### 6.1 ProductServiceStatus

> Reuses the same enum as Admin Portal. Landing Page only queries for ACTIVE records.

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Content is being prepared (not visible on landing page) | USL000006 | v1.0.0 |
| INACTIVE | Content is temporarily hidden (not visible on landing page) | USL000006 | v1.0.0 |
| ACTIVE | Content is live and displayed on the landing page | USL000006 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| _No domain events — this is a read-only module_ | | | | |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Product and Service Section | ProductService (Read Model) | Displays active products and services on the public landing page, ordered by display order |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000006 | ProductService | The Landing Page reads from the same `product_services` table managed by the Admin Portal. Only records with status=ACTIVE are displayed, ordered by displayOrder ascending. Items with the same displayOrder are sub-sorted by createdAt ascending. | Is there a maximum number of products/services to display on the landing page? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | ProductService | Read-only product/service entity for landing page display | USL000006 |
| Enum (Shared) | ProductServiceStatus | DRAFT, INACTIVE, ACTIVE — shared with Admin Portal | USL000006 |
