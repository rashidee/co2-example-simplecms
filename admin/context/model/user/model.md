# Module Model: User

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| User | USR | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| User | Aggregate Root | User | email, password, firstName, lastName, role, status | Referenced by PasswordResetToken (Auth module), BlogPost (Blog module) | USA000012, USA000015, USA000018, USA000021, USA000024, USA000027, NFRA00006, NFRA00009, NFRA00012, NFRA00015, CONSA0003, CONSA0006, CONSA0009 | v1.0.0 |

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

### 4.1 User

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| email | String | No | Unique, max 255 chars, valid email format. Used as username for login. | EXPLICIT | NFRA00006, USA000003 | v1.0.0 |
| password | String | No | Max 255 chars, stored hashed. Default: "password" for new users. | EXPLICIT | NFRA00006, NFRA00012 | v1.0.0 |
| firstName | String | No | Max 100 chars | EXPLICIT | NFRA00006 | v1.0.0 |
| lastName | String | No | Max 100 chars | EXPLICIT | NFRA00006 | v1.0.0 |
| role | UserRole (Enum) | No | Default: USER | EXPLICIT | NFRA00006, NFRA00009 | v1.0.0 |
| status | UserStatus (Enum) | No | Default: ACTIVE | EXPLICIT | NFRA00006, CONSA0006 | v1.0.0 |
| forcePasswordChange | Boolean | No | Default: true for new users | OPERATION_INFERENCE | NFRA00012 | v1.0.0 |
| lastLoginAt | Instant | Yes | Updated on each successful login | OPERATION_INFERENCE | USA000003 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No intra-module relationships_ | | | | | | | |

## 6. Enum Definitions

### 6.1 UserRole

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| USER | Basic user with read-only access | NFRA00006 | v1.0.0 |
| EDITOR | User who can create and manage content | NFRA00009 | v1.0.0 |
| ADMIN | Administrator with full system access including user management | NFRA00006, NFRA00009, CONSA0009 | v1.0.0 |

### 6.2 UserStatus

| Value | Description | Source Story | Version |
|-------|-------------|--------------|---------|
| ACTIVE | User can log in and use the system (default) | CONSA0006 | v1.0.0 |
| INACTIVE | User cannot log in to the system | CONSA0006 | v1.0.0 |

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| UserCreated | USA000021 | User | userId, email, role, status | v1.0.0 |
| UserUpdated | USA000024 | User | userId, changedFields | v1.0.0 |
| UserDeleted | USA000027 | User | userId | v1.0.0 |
| PasswordChanged | USA000015 | User | userId | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| User | User | Manages user accounts, profiles, roles, and status for the admin portal |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| NFRA00006, NFRA00009 | User.role | NFRA00006 states "Role (USER or ADMIN)" but NFRA00009 mentions "User and Editor by default" and upgrading from "Editor to Admin". Assumed three roles exist: USER, EDITOR, ADMIN. | Please confirm the complete list of user roles. | v1.0.0 |
| NFRA00012 | User.forcePasswordChange | Default password "password" requires forced change on first login. Added boolean flag to track this. | Is a password expiry policy needed beyond first login? | v1.0.0 |
| NFRA00015 | User | System creates admin user on startup if no users exist. This is application initialization logic, not a model concern. | Should the seed admin user be configurable via environment variables? | v1.0.0 |
| USA000027 | User | Assumed soft delete is NOT used — admin deletes remove the user record. Users have ACTIVE/INACTIVE status for disabling access. | Should user deletion be soft delete (preserving records) or hard delete? | v1.0.0 |
| CONSA0003 | User | Only firstName and lastName are editable on the profile page. Other fields are editable only via admin user management. | Can admin change a user's email? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | User | Core user entity with authentication and profile information | USA000012-027, NFRA00006-015, CONSA0003-009 |
| Enum | UserRole | USER, EDITOR, ADMIN role types | NFRA00006, NFRA00009 |
| Enum | UserStatus | ACTIVE, INACTIVE status types | CONSA0006 |
