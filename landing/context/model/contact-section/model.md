# Module Model: Contact Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Contact Section | CTS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Access Mode | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|-------------|---------------|---------|
| ContactInfo | Read Model | Contact Section | phoneNumber, emailAddress, physicalAddress | — | Read-only | USL000018 | v1.0.0 |
| ContactMessage | Aggregate Root | Contact Section | senderName, senderEmail, messageContent, submittedAt, createdAt | — | Write (form submission) | USL000018 | v1.0.0 |

## 3. Base Entity Specification

ContactInfo is read-only (no audit write fields needed). ContactMessage is a write model and includes audit fields for the creation context.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |

## 4. Attribute Detail

### 4.1 ContactInfo

> Reads from the `contact_info` table. Single record containing company contact details. Read-only from the Landing Page.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| phoneNumber | String | No | Max 50 chars, valid phone format | EXPLICIT | USL000018 | v1.0.0 |
| emailAddress | String | No | Max 255 chars, valid email format | EXPLICIT | USL000018 | v1.0.0 |
| physicalAddress | String | No | Max 500 chars | EXPLICIT | USL000018 | v1.0.0 |

### 4.2 ContactMessage

> Writes to the `contact_messages` table. Submitted by visitors via the contact form on the landing page.

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK, auto-generated | CONVENTION | — | v1.0.0 |
| senderName | String | No | Max 255 chars | EXPLICIT | USL000018 | v1.0.0 |
| senderEmail | String | No | Max 255 chars, valid email format | EXPLICIT | USL000018 | v1.0.0 |
| messageContent | String (Text) | No | No character limit specified | EXPLICIT | USL000018 | v1.0.0 |
| submittedAt | Instant | No | Auto-set when form is submitted | EXPLICIT | USL000018 | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| _No relationships between Landing Page entities_ | | | | | | | |

## 6. Enum Definitions

_No enums defined in this module._

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| ContactMessageSubmitted | USL000018 | ContactMessage | messageId, senderName, senderEmail, submittedAt | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Contact Section | ContactInfo (Read Model), ContactMessage (Write Model) | Displays company contact information and accepts visitor messages via the contact form |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| USL000018 | ContactInfo | The Landing Page reads from the same `contact_info` table managed by the Admin Portal. Only a single record exists; the first (and only) row is fetched. | No clarification needed. | v1.0.0 |
| USL000018 | ContactMessage | The contact form writes to the same `contact_messages` table that the Admin Portal reads from. Basic form validation (required fields, email format) is performed client-side and server-side. Spam prevention (e.g., CAPTCHA, honeypot, rate limiting) should be implemented. | What spam prevention mechanism should be used? Should there be rate limiting on form submissions? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity (Read Model) | ContactInfo | Read-only company contact information for landing page display | USL000018 |
| Entity (Write Model) | ContactMessage | Contact form submission entity written by the landing page | USL000018 |
| Domain Event | ContactMessageSubmitted | Fired when a visitor submits a contact form message | USL000018 |
