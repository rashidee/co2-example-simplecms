# TEST_SPEC: Contact Section

**Application**: Landing Page (SCMS)
**Module**: Contact Section
**Category**: Business Module
**Layer**: L2 — Reference Data
**Seeding Strategy**: DB Insert (psql) + Form Submit
**Generated**: 2026-03-16
**Version**: all versions
**Versions Covered**: v1.0.0

---

## 1. Module Overview

Information about the contact section of the marketing page. Displays company contact information (phone, email, address) and a contact form for visitors to submit messages.

### Layer Classification Reasoning

L2 (Reference Data): Contact info is managed by Admin Portal (read-only). The contact form is the only write operation in the entire Landing Page — it inserts into `cts_contact_message`.

### Source Artifacts

| Artifact Type | Reference | Version |
|---------------|-----------|---------|
| User Story | USL000018 | v1.0.0 |
| NFR | NFRL00072 | v1.0.0 |
| NFR | NFRL00075 | v1.0.0 |
| NFR | NFRL00078 | v1.0.0 |
| NFR | NFRL00081 | v1.0.0 |
| NFR | NFRL00084 | v1.0.0 |

| Artifact | Path | Status |
|----------|------|--------|
| User Stories | `landing/context/PRD.md` | Found |
| Module Model | `admin/context/model/contact-section/model.md` | Found |
| Specification | `landing/context/specification/contact-section/SPEC.md` | Found |
| Mockup | `landing/context/mockup/pages/home.html` | Found |

### Removed / Replaced

_None._

---

## 2. Prerequisites

| Prerequisite | Module | Layer | How to Verify |
|-------------|--------|-------|--------------|
| PostgreSQL running | — | Infra | `pg_isready -h localhost -p 5432` |
| Landing page app running | — | Infra | `curl -s http://localhost:8000/` |

---

## 3. Data Seeding

### 3a. Seeding Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
INSERT INTO cts_contact_info (id, phone_number, email_address, physical_address, linkedin_url, version, created_at, created_by, updated_at, updated_by) VALUES
  ('f6000001-0000-0000-0000-000000000001', '+1 (555) 123-4567', 'hello@simplecms.example.com', '123 Innovation Drive, Suite 400\nSan Francisco, CA 94107', 'https://linkedin.com/company/simplecms-test', 0, NOW(), 'test-seed', NOW(), 'test-seed');
"
```

### 3b. Seeded Data Summary

| Table | Record Count | Key Fields | Sample Values |
|-------|-------------|------------|---------------|
| `cts_contact_info` | 1 | phone_number, email_address, physical_address | "+1 (555) 123-4567", "hello@simplecms.example.com" |

---

## 4. Test Scenarios

### 4a. View Tests

#### VIEW-CTS-001: Verify contact info displays phone, email, address

- **Source**: USL000018 [v1.0.0], NFRL00072 [v1.0.0], NFRL00075 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: 1 contact info record seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to `#contact` section
- **Expected**:
  - Section heading is "Contact Us" (NFRL00072)
  - Phone number "+1 (555) 123-4567" displayed with phone icon
  - Email "hello@simplecms.example.com" displayed with email icon
  - Address "123 Innovation Drive, Suite 400, San Francisco, CA 94107" with location icon
  - White background (NFRL00081)
- **Selectors** (from mockup):
  - Section: `#contact`
  - Heading: `#contact h2`
  - Phone: text containing "+1 (555) 123-4567"
  - Email: text containing "hello@simplecms.example.com"

### 4b. CRUD Tests (Create — Contact Form)

#### CRUD-CTS-001: Submit contact form message successfully

- **Source**: USL000018 [v1.0.0], NFRL00078 [v1.0.0]
- **Role**: Public (anonymous)
- **Preconditions**: Landing page running, contact info seeded
- **Steps**:
  1. Navigate to `http://localhost:8000/`
  2. Scroll to `#contact` section
  3. Fill in the contact form:
     - Name: "Test User"
     - Email: "testuser@test.example.com"
     - Message: "This is a test message from E2E testing."
  4. Click "Send Message" button
  5. Wait for response
- **Expected**:
  - Toast notification appears: "Thank you! Your message has been sent successfully."
  - Form fields are cleared after successful submission
  - New record exists in `cts_contact_message` table with:
    - sender_name = "Test User"
    - sender_email = "testuser@test.example.com"
    - message_content = "This is a test message from E2E testing."
    - submitted_at is set
- **Form Fields** (from mockup):
  | Field | Selector | Type | Required | Sample Value |
  |-------|----------|------|----------|--------------|
  | Name | `#contact-name` or `input[name="sender_name"]` | text | yes | "Test User" |
  | Email | `#contact-email` or `input[name="sender_email"]` | email | yes | "testuser@test.example.com" |
  | Message | `#contact-message` or `textarea[name="message_content"]` | textarea | yes | "This is a test message from E2E testing." |

### 4c. Validation Tests

#### VAL-CTS-001: Required field validation on contact form

- **Source**: NFRL00078 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to home page, scroll to `#contact`
  2. Leave all fields empty
  3. Click "Send Message"
- **Expected**:
  - Form is NOT submitted
  - Browser-native required field validation triggers
  - No toast notification appears
  - No record inserted into database

#### VAL-CTS-002: Message maximum 500 characters

- **Source**: NFRL00078 [v1.0.0]
- **Role**: Public (anonymous)
- **Steps**:
  1. Navigate to contact form
  2. Fill name: "Test User"
  3. Fill email: "test@test.example.com"
  4. Fill message with 501 characters (e.g., "A" repeated 501 times)
  5. Attempt to submit
- **Expected**:
  - Message field has `maxlength="500"` attribute
  - Input is truncated at 500 characters OR
  - Server-side validation returns error "Your message must not exceed 500 characters."
- **Selectors** (from mockup):
  - Message textarea: `textarea[maxlength="500"]` or `textarea[name="message_content"]`

---

## 5. Data Cleanup

### 5a. Cleanup Script

```bash
PGPASSWORD=cms_password psql -h localhost -p 5432 -U cms_user -d cms_db -c "
DELETE FROM cts_contact_message WHERE sender_email LIKE '%@test.example.com';
DELETE FROM cts_contact_info WHERE created_by = 'test-seed';
"
```

### 5b. Cleanup Order

1. `cts_contact_message` (submitted test messages)
2. `cts_contact_info` (seeded contact info)

---

## 6. Traceability Matrix

| Test Scenario ID | User Story | Version | NFR(s) | Constraint(s) | Test Type |
|-----------------|------------|---------|--------|---------------|-----------|
| VIEW-CTS-001 | USL000018 | v1.0.0 | NFRL00072, NFRL00075, NFRL00081 | — | View |
| CRUD-CTS-001 | USL000018 | v1.0.0 | NFRL00078 | — | Create |
| VAL-CTS-001 | — | v1.0.0 | NFRL00078 | — | Validation |
| VAL-CTS-002 | — | v1.0.0 | NFRL00078 | — | Validation |
