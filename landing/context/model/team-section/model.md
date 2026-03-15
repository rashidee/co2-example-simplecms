# Module Model: Team Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Team Section | TMS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| TeamMember | Read Model | Team Section | profilePicturePath, name, role, linkedinUrl, displayOrder, status | — | USL000015 | v1.0.0 |

## 3. Base Entity Specification

This is a read-only model. The Landing Page reads from the `team_members` table created by the Admin Portal. No audit write fields are needed for the Landing Page context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 TeamMember

> Reads from the `team_members` table. Filtered by `status = ACTIVE`, ordered by `displayOrder ASC`.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| profilePicturePath | String | No | Max 500 chars. Path to team member photo (400x400 pixels, square). | EXPLICIT | USL000015 | v1.0.0 |
| name | String | No | Max 100 chars | EXPLICIT | USL000015 | v1.0.0 |
| role | String | No | Max 100 chars. Job title/position. | EXPLICIT | USL000015 | v1.0.0 |
| linkedinUrl | String | No | Max 500 chars, valid URL format | EXPLICIT | USL000015 | v1.0.0 |
| displayOrder | Integer | No | Used for display sorting | EXPLICIT | USL000015 | v1.0.0 |
| status | TeamMemberStatus (Enum) | No | Filtered to ACTIVE only | EXPLICIT | USL000015 | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships in this module_ | | | | | | | |

## 6. Enum Definitions

### 6.1 TeamMemberStatus

> Reuses the same enum as Admin Portal. Landing Page only queries for ACTIVE records.

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Content is being prepared (not visible on landing page) | USL000015 | v1.0.0 |
| INACTIVE | Content is temporarily hidden (not visible on landing page) | USL000015 | v1.0.0 |
| ACTIVE | Content is live and displayed on the landing page | USL000015 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| _No domain events — this is a read-only module_ | | | | |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Team Section | TeamMember (Read Model) | Displays active team member profiles on the public landing page, ordered by display order |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000015 | TeamMember | The Landing Page reads from the same `team_members` table managed by the Admin Portal. Only records with status=ACTIVE are displayed, ordered by displayOrder ascending. | Is there a maximum number of team members to display on the landing page? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | TeamMember | Read-only team member entity for landing page display | USL000015 |
| Enum (Shared) | TeamMemberStatus | DRAFT, INACTIVE, ACTIVE — shared with Admin Portal | USL000015 |
