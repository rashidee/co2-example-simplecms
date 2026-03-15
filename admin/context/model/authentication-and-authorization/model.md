# Module Model: Authentication and Authorization

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Authentication and Authorization | AAA | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| PasswordResetToken | Entity | Authentication and Authorization | token, expiresAt, used | N:1 with User (cross-module) | USA000006 | v1.0.0 |

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

### 4.1 PasswordResetToken

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| userId | UUID | No | FK to User | EXPLICIT | USA000006 | v1.0.0 |
| token | String | No | Unique, max 255 chars | OPERATION_INFERENCE | USA000006 | v1.0.0 |
| expiresAt | Instant | No | Must be future date | OPERATION_INFERENCE | USA000006 | v1.0.0 |
| used | Boolean | No | Default: false | OPERATION_INFERENCE | USA000006 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| PasswordResetToken | User (User module) | N:1 | — | CASCADE DELETE | Each reset token belongs to one user; a user can have multiple tokens | USA000006 | v1.0.0 |

## 6. Enum Definitions

_No enums defined in this module._

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| PasswordResetRequested | USA000006 | PasswordResetToken | userId, token, expiresAt | v1.0.0 |
| PasswordResetCompleted | USA000006 | PasswordResetToken | userId, tokenId | v1.0.0 |
| UserLoggedIn | USA000003 | — (cross-module) | userId, loginAt | v1.0.0 |
| UserLoggedOut | USA000009 | — (cross-module) | userId, logoutAt | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Authentication and Authorization | PasswordResetToken | Manages user authentication flows including login, logout, and password reset |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USA000003 | User | Login uses User entity from User module; no separate credential entity | Should credentials (email/password) be separated from User profile? | v1.0.0 |
| USA000006 | PasswordResetToken | Token is a secure random string sent via email link; token expires after a set period | What is the token expiry duration? (assumed 24 hours) | v1.0.0 |
| NFRA00003 | PasswordResetToken | "Typical and secure forgot password flow" means: request → email with link → token validation → password change | Are there additional security steps (e.g., CAPTCHA, rate limiting)? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | PasswordResetToken | Stores password reset tokens for forgot password flow | USA000006, NFRA00003 |
| Domain Event | PasswordResetRequested | Fired when user requests password reset | USA000006 |
| Domain Event | PasswordResetCompleted | Fired when password is successfully reset | USA000006 |
| Domain Event | UserLoggedIn | Fired on successful login | USA000003 |
| Domain Event | UserLoggedOut | Fired on logout | USA000009 |
