# Module Model: Product and Service Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Product and Service Section | PAS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| ProductService | Aggregate Root | Product and Service Section | imagePath, title, description, ctaUrl, ctaText, displayOrder, status | — | USA000036, USA000039, USA000042, USA000045, NFRA00039-057, CONSA0015 | v1.0.0 |

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

### 4.1 ProductService

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| imagePath | String | No | Max 500 chars. Original image must be exactly 400x400 pixels. | EXPLICIT | USA000036, NFRA00039 | v1.0.0 |
| thumbnailPath | String | Yes | Max 500 chars. Auto-generated at 200x200 pixels. | OPERATION_INFERENCE | NFRA00057 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USA000036, NFRA00042 | v1.0.0 |
| description | String | No | Max 500 chars | EXPLICIT | USA000036, NFRA00045 | v1.0.0 |
| ctaUrl | String | Yes | Max 500 chars, valid URL format if provided | EXPLICIT | USA000036, NFRA00048 | v1.0.0 |
| ctaText | String | Yes | Max 50 chars if provided | EXPLICIT | USA000036, NFRA00051 | v1.0.0 |
| displayOrder | Integer | No | Default: 0. Used for display sorting. | EXPLICIT | USA000039, NFRA00054 | v1.0.0 |
| status | ProductServiceStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000036, CONSA0015 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships in this module_ | | | | | | | |

## 6. Enum Definitions

### 6.1 ProductServiceStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0015 | v1.0.0 |
| INACTIVE | Content is temporarily hidden from the website | CONSA0015 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0015 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| ProductServiceCreated | USA000036 | ProductService | productServiceId, title, status | v1.0.0 |
| ProductServiceUpdated | USA000036 | ProductService | productServiceId, changedFields | v1.0.0 |
| ProductServiceDeleted | USA000042 | ProductService | productServiceId | v1.0.0 |
| ProductServiceReordered | USA000039 | ProductService | productServiceId, newOrder | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Product and Service Section | ProductService | Manages product and service showcase content with images, descriptions, optional CTAs, and display ordering |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USA000042 | ProductService | Deletion is a hard delete, not soft delete, since the status field handles visibility | Should deleted content be recoverable? | v1.0.0 |
| NFRA00054 | ProductService.displayOrder | Items with the same displayOrder are sub-sorted by createdAt ascending | Is this the intended tie-breaking behavior? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | ProductService | Product and service content entity with image, text, optional CTA, and ordering | USA000036-045, NFRA00039-057, CONSA0015 |
| Enum | ProductServiceStatus | DRAFT, INACTIVE, ACTIVE status types | CONSA0015 |
