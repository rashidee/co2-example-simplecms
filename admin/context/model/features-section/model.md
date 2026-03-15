# Module Model: Features Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Features Section | FTS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| Feature | Aggregate Root | Features Section | icon, title, description, displayOrder, status | — | USA000048, USA000051, USA000054, USA000057, NFRA00060-066, CONSA0018 | v1.0.0 |

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

### 4.1 Feature

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| icon | String | No | Max 100 chars. FontAwesome icon class name from predefined list. | EXPLICIT | USA000048 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USA000048, NFRA00060 | v1.0.0 |
| description | String | No | Max 500 chars | EXPLICIT | USA000048, NFRA00063 | v1.0.0 |
| displayOrder | Integer | No | Default: 0. Used for display sorting. | EXPLICIT | USA000051, NFRA00066 | v1.0.0 |
| status | FeatureStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000048, CONSA0018 | v1.0.0 |
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

### 6.1 FeatureStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0018 | v1.0.0 |
| INACTIVE | Content is temporarily hidden from the website | CONSA0018 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0018 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| FeatureCreated | USA000048 | Feature | featureId, title, status | v1.0.0 |
| FeatureUpdated | USA000048 | Feature | featureId, changedFields | v1.0.0 |
| FeatureDeleted | USA000054 | Feature | featureId | v1.0.0 |
| FeatureReordered | USA000051 | Feature | featureId, newOrder | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Features Section | Feature | Manages feature highlight content with FontAwesome icons, descriptions, and display ordering |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USA000048 | Feature.icon | The icon field stores a FontAwesome icon class string (e.g., "fa-solid fa-star") selected from a predefined list | Should the predefined icon list be stored in the database or hardcoded in the application? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | Feature | Feature content entity with icon, text, ordering | USA000048-057, NFRA00060-066, CONSA0018 |
| Enum | FeatureStatus | DRAFT, INACTIVE, ACTIVE status types | CONSA0018 |
