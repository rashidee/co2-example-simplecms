# Module Model: Testimonials Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Testimonials Section | TST | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| Testimonial | Aggregate Root | Testimonials Section | customerName, customerReview, customerRating, displayOrder, status | — | USA000060, USA000063, USA000066, USA000069, NFRA00069-078, CONSA0021 | v1.0.0 |

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

### 4.1 Testimonial

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| customerName | String | No | Max 100 chars | EXPLICIT | USA000060, NFRA00069 | v1.0.0 |
| customerReview | String | No | Max 1000 chars | EXPLICIT | USA000060, NFRA00072 | v1.0.0 |
| customerRating | Integer | No | Min: 1, Max: 5 | EXPLICIT | USA000060, NFRA00075 | v1.0.0 |
| displayOrder | Integer | No | Default: 0. Used for display sorting. | EXPLICIT | USA000063, NFRA00078 | v1.0.0 |
| status | TestimonialStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000060, CONSA0021 | v1.0.0 |
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

### 6.1 TestimonialStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0021 | v1.0.0 |
| INACTIVE | Content is temporarily hidden from the website | CONSA0021 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0021 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| TestimonialCreated | USA000060 | Testimonial | testimonialId, customerName, status | v1.0.0 |
| TestimonialUpdated | USA000060 | Testimonial | testimonialId, changedFields | v1.0.0 |
| TestimonialDeleted | USA000066 | Testimonial | testimonialId | v1.0.0 |
| TestimonialReordered | USA000063 | Testimonial | testimonialId, newOrder | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Testimonials Section | Testimonial | Manages customer testimonials with ratings, reviews, and display ordering |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USA000060 | Testimonial | Testimonials are entered manually by editors, not submitted by customers directly | Should there be a customer-facing testimonial submission form in the future? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | Testimonial | Customer testimonial content entity with name, review, rating, and ordering | USA000060-069, NFRA00069-078, CONSA0021 |
| Enum | TestimonialStatus | DRAFT, INACTIVE, ACTIVE status types | CONSA0021 |
