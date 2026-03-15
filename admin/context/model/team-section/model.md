# Module Model: Team Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Team Section | TMS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| TeamMember | Aggregate Root | Team Section | profilePicturePath, name, role, linkedinUrl, displayOrder, status | — | USA000072, USA000075, USA000078, USA000081, NFRA00081-093, CONSA0024 | v1.0.0 |

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

### 4.1 TeamMember

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| profilePicturePath | String | No | Max 500 chars. Original image must be exactly 400x400 pixels (square). | EXPLICIT | USA000072, NFRA00081 | v1.0.0 |
| name | String | No | Max 100 chars | EXPLICIT | USA000072, NFRA00084 | v1.0.0 |
| role | String | No | Max 100 chars. Job title/position of the team member. | EXPLICIT | USA000072, NFRA00087 | v1.0.0 |
| linkedinUrl | String | No | Max 500 chars, valid URL format | EXPLICIT | USA000072, NFRA00090 | v1.0.0 |
| displayOrder | Integer | No | Default: 0. Used for display sorting. | EXPLICIT | USA000075, NFRA00093 | v1.0.0 |
| status | TeamMemberStatus (Enum) | No | Default: DRAFT | EXPLICIT | USA000072, CONSA0024 | v1.0.0 |
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

### 6.1 TeamMemberStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| DRAFT | Initial status, content is being prepared (default) | CONSA0024 | v1.0.0 |
| INACTIVE | Content is temporarily hidden from the website | CONSA0024 | v1.0.0 |
| ACTIVE | Content is live and displayed on the website | CONSA0024 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| TeamMemberCreated | USA000072 | TeamMember | teamMemberId, name, status | v1.0.0 |
| TeamMemberUpdated | USA000072 | TeamMember | teamMemberId, changedFields | v1.0.0 |
| TeamMemberDeleted | USA000078 | TeamMember | teamMemberId | v1.0.0 |
| TeamMemberReordered | USA000075 | TeamMember | teamMemberId, newOrder | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Team Section | TeamMember | Manages team member profiles with photos, roles, LinkedIn links, and display ordering |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USA000072 | TeamMember.profilePicturePath | Image stored on file system; no thumbnail generation mentioned for team members | Should a thumbnail version be generated for the team section list? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | TeamMember | Team member profile entity with photo, name, role, LinkedIn, and ordering | USA000072-081, NFRA00081-093, CONSA0024 |
| Enum | TeamMemberStatus | DRAFT, INACTIVE, ACTIVE status types | CONSA0024 |
