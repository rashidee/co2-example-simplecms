# Module Model: Contact Section

## 1. Module Prefix Map

| Module | Prefix | Auto-Generated | Override |
|--------|--------|----------------|----------|
| Contact Section | CTS | Yes | — |

## 2. Entity Catalog

| Entity | DDD Type | Bounded Context | Key Attributes | Relationships | Source Stories | Version |
|--------|----------|-----------------|----------------|---------------|---------------|---------|
| ContactInfo | Aggregate Root | Contact Section | phoneNumber, emailAddress, physicalAddress, linkedinUrl | — | USA000084, NFRA00096-105, CONSA0027 | v1.0.0 |
| ContactMessage | Aggregate Root | Contact Section | senderName, senderEmail, messageContent, submittedAt | 1:N with ContactResponse | USA000087, USA000090 | v1.0.0 |
| ContactResponse | Entity | Contact Section | responderName, responderEmail, responseContent, sentAt | N:1 with ContactMessage | USA000093, NFRA00108 | v1.0.0 |

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

### 4.1 ContactInfo

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| phoneNumber | String | No | Max 50 chars, valid phone format | EXPLICIT | USA000084, NFRA00096 | v1.0.0 |
| emailAddress | String | No | Max 255 chars, valid email format | EXPLICIT | USA000084, NFRA00099 | v1.0.0 |
| physicalAddress | String | No | Max 500 chars | EXPLICIT | USA000084, NFRA00102 | v1.0.0 |
| linkedinUrl | String | No | Max 500 chars, valid URL format | EXPLICIT | USA000084, NFRA00105 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

### 4.2 ContactMessage

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| senderName | String | No | Max 255 chars | EXPLICIT | USA000087 | v1.0.0 |
| senderEmail | String | No | Max 255 chars, valid email format | EXPLICIT | USA000087 | v1.0.0 |
| messageContent | String (Text) | No | No character limit specified | EXPLICIT | USA000087, USA000090 | v1.0.0 |
| submittedAt | Instant | No | Auto-set when submitted from website | EXPLICIT | USA000087 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

### 4.3 ContactResponse

| Attribute | Type | Nullable | Constraints | Source | Source Story | Version |
|-----------|------|----------|-------------|--------|--------------|---------|
| id | UUID | No | PK | CONVENTION | — | v1.0.0 |
| contactMessageId | UUID | No | FK to ContactMessage | EXPLICIT | USA000093 | v1.0.0 |
| responderName | String | No | Max 255 chars | EXPLICIT | USA000093 | v1.0.0 |
| responderEmail | String | No | Max 255 chars, valid email format | EXPLICIT | USA000093 | v1.0.0 |
| responseContent | String (Text) | No | No character limit specified | EXPLICIT | USA000093 | v1.0.0 |
| sentAt | Instant | Yes | Set when email is actually sent via batch job | EXPLICIT | USA000093, NFRA00108 | v1.0.0 |
| version | Long | No | Optimistic locking | CONVENTION | — | v1.0.0 |
| createdAt | Instant | No | Auto-set on create | CONVENTION | — | v1.0.0 |
| createdBy | String | No | Max 255 chars | CONVENTION | — | v1.0.0 |
| updatedAt | Instant | Yes | Auto-set on update | CONVENTION | — | v1.0.0 |
| updatedBy | String | Yes | Max 255 chars | CONVENTION | — | v1.0.0 |

## 5. Relationship Catalog

| Source Entity | Target Entity | Cardinality | Join Entity | Cascade | Business Rule | Source Story | Version |
|---------------|---------------|-------------|-------------|---------|---------------|-------------|---------|
| ContactMessage | ContactResponse | 1:N | — | CASCADE DELETE | A message can have multiple responses; responses belong to one message | USA000093 | v1.0.0 |

## 6. Enum Definitions

_No enums defined in this module._

## 7. Domain Events

| Event Name | Trigger Story | Aggregate | Payload Fields | Version |
|------------|---------------|-----------|----------------|---------|
| ContactInfoUpdated | USA000084 | ContactInfo | contactInfoId | v1.0.0 |
| ContactMessageReceived | USA000087 | ContactMessage | messageId, senderName, senderEmail | v1.0.0 |
| ContactResponseSubmitted | USA000093 | ContactResponse | responseId, messageId, responderEmail | v1.0.0 |
| ContactResponseSent | NFRA00108 | ContactResponse | responseId, messageId, sentAt | v1.0.0 |

## 8. Bounded Context Summary

| Context | Aggregates | Description |
|---------|------------|-------------|
| Contact Section | ContactInfo, ContactMessage | Manages company contact information and handles incoming messages with response tracking |

## 9. Assumptions and Ambiguities

| Story ID | Entity / Attribute | Assumption Made | Clarification Needed | Version |
|----------|--------------------|-----------------|----------------------|---------|
| CONSA0027 | ContactInfo | Contact info is stored as a single record; updates overwrite the existing record. Assumed only one ContactInfo record exists in the system. | Is there a need to track contact info history? | v1.0.0 |
| USA000093 | ContactResponse | Response is created in the admin portal and then sent asynchronously via batch job. The sentAt field tracks actual email delivery. | What happens if the email fails to send? Should there be a retry mechanism or failure status? | v1.0.0 |
| NFRA00108 | ContactResponse | "Batch job asynchronously" means a scheduled job picks up unsent responses and sends emails. | What is the expected batch frequency? Should it be near-real-time or on a schedule? | v1.0.0 |
| USA000087 | ContactMessage | Messages are submitted via the landing page contact form and stored in the shared database. This module only reads and manages them. | Is there a maximum message length? | v1.0.0 |

## 10. Changelog

### Version v1.0.0 (initial)

#### Added
| Element Type | Name | Description | Source Stories |
|---|---|---|---|
| Entity | ContactInfo | Single-record entity storing company contact details | USA000084, NFRA00096-105, CONSA0027 |
| Entity | ContactMessage | Stores messages submitted from the website contact form | USA000087, USA000090 |
| Entity | ContactResponse | Stores editor responses to contact messages, sent via batch job | USA000093, NFRA00108 |
