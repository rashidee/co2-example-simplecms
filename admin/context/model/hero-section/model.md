# Module Model: Hero Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Hero Section | HRS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| HeroSection | Aggregate Root | Hero Section | imagePath, headline, subheadline, ctaUrl, ctaText, effectiveDate, expirationDate, status | — | USA000030, USA000033, NFRA00018-036, CONSA0012 | v1.0.0 |

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

### 4.1 HeroSection

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| imagePath | String | No | Max 500 chars. Original image must be exactly 1600x500 pixels. | EXPLICIT | USA000030, NFRA00021 | v1.0.0 |
| thumbnailPath | String | Yes | Max 500 chars. Auto-generated at 400x125 pixels. | OPERATION_INFERENCE | NFRA00036 | v1.0.0 |
| headline | String | No | Max 100 chars | EXPLICIT | USA000030, NFRA00024 | v1.0.0 |
| subheadline | String | No | Max 200 chars | EXPLICIT | USA000030, NFRA00024 | v1.0.0 |
| ctaUrl | String | No | Max 500 chars, valid URL format | EXPLICIT | USA000030, NFRA00027 | v1.0.0 |
| ctaText | String | No | Max 50 chars | EXPLICIT | USA000030, NFRA00030 | v1.0.0 |
| effectiveDate | Instant | No | Must be a valid date | EXPLICIT | USA000030 | v1.0.0 |
| expirationDate | Instant | No | Must be after effectiveDate | EXPLICIT | USA000030 | v1.0.0 |
| status | HeroSectionStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000030, CONSA0012 | v1.0.0 |
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

### 6.1 HeroSectionStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0012 | v1.0.0 |
| READY | Content is reviewed and ready to go live | CONSA0012 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0012 | v1.0.0 |
| EXPIRED | Content has passed its expiration date, automatically set by system | CONSA0012, NFRA00018 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| HeroSectionCreated | USA000030 | HeroSection | heroSectionId, headline, status | v1.0.0 |
| HeroSectionUpdated | USA000030 | HeroSection | heroSectionId, changedFields | v1.0.0 |
| HeroSectionExpired | NFRA00018 | HeroSection | heroSectionId, expirationDate | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Hero Section | HeroSection | Manages hero banner content with images, text, CTAs, and date-based lifecycle |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| NFRA00018 | HeroSection.status | Expiration is handled by a scheduled job that checks expirationDate and updates status to EXPIRED | How frequently should the expiration check run? | v1.0.0 |
| USA000030 | HeroSection.imagePath | Image is stored on the file system; path is stored in the database | Is cloud storage (S3, etc.) used, or local file system? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | HeroSection | Hero banner content entity with image, text, CTA, and date lifecycle | USA000030, USA000033, NFRA00018-036, CONSA0012 |
| Enum | HeroSectionStatus | DRAFT, READY, ACTIVE, EXPIRED status lifecycle | CONSA0012, NFRA00018 |
