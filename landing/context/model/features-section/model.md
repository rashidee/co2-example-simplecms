# Module Model: Features Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Features Section | FTS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| Feature | Read Model | Features Section | icon, title, description, displayOrder, status | — | USL000009 | v1.0.0 |

## 3. Base Entity Specification

This is a read-only model. The Landing Page reads from the `features` table created by the Admin Portal. No audit write fields are needed for the Landing Page context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 Feature

> Reads from the `features` table. Filtered by `status = ACTIVE`, ordered by `displayOrder ASC`.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| icon | String | No | Max 100 chars. FontAwesome icon class name. | EXPLICIT | USL000009 | v1.0.0 |
| title | String | No | Max 100 chars | EXPLICIT | USL000009 | v1.0.0 |
| description | String | No | Max 500 chars | EXPLICIT | USL000009 | v1.0.0 |
| displayOrder | Integer | No | Used for display sorting | EXPLICIT | USL000009 | v1.0.0 |
| status | FeatureStatus (Enum) | No | Filtered to ACTIVE only | EXPLICIT | USL000009 | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships in this module_ | | | | | | | |

## 6. Enum Definitions

### 6.1 FeatureStatus

> Reuses the same enum as Admin Portal. Landing Page only queries for ACTIVE records.

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Content is being prepared (not visible on landing page) | USL000009 | v1.0.0 |
| INACTIVE | Content is temporarily hidden (not visible on landing page) | USL000009 | v1.0.0 |
| ACTIVE | Content is live and displayed on the landing page | USL000009 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| _No domain events — this is a read-only module_ | | | | |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Features Section | Feature (Read Model) | Displays active features with icons and descriptions on the public landing page, ordered by display order |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000009 | Feature | The Landing Page reads from the same `features` table managed by the Admin Portal. Only records with status=ACTIVE are displayed, ordered by displayOrder ascending. | Is there a maximum number of features to display on the landing page? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | Feature | Read-only feature entity for landing page display | USL000009 |
| Enum (Shared) | FeatureStatus | DRAFT, INACTIVE, ACTIVE — shared with Admin Portal | USL000009 |
