# Test Specification: Contact Section

| Field | Value |
|-------|-------|
| Module | Contact Section |
| Prefix | CTS |
| Layer | L2 (Reference Data) |
| Source Stories | USL000018 |
| Version | v1.0.0 |
| Date | 2026-03-15 |

---

## 1. Module Overview

The Contact Section displays company contact information (phone, email, physical address) and provides a contact form for visitors to submit messages. The contact information is read from the `contact_info` table, while form submissions are written to the `contact_messages` table. The form includes validation for required fields, email format, message length (max 500 characters), and CAPTCHA protection.

---

## 2. Layer Classification

| Layer | Value |
|-------|-------|
| Classification | L2 -- Reference Data |
| Seeding Strategy | psql INSERT into `contact_info` table |
| Auth Required | No |
| Dependencies | None |

---

## 3. Source Artifacts

| Artifact | Path | Version |
|----------|------|---------|
| PRD (User Story) | `landing/context/PRD.md` -- USL000018 | v1.0.0 |
| Data Model | `landing/context/model/contact-section/model.md` | v1.0.0 |
| ERD | `landing/context/model/contact-section/erd.mermaid` | v1.0.0 |

---

## 4. Prerequisites

- Landing Page application is running and accessible at the base URL.
- PostgreSQL database `cms_db` is available at `localhost:5432`.
- Test data has been seeded (see Section 5).
- CAPTCHA service is configured (may need test/bypass keys for E2E testing).

---

## 5. Test Data Seeding

### 5.1 Seed Script

```sql
-- Contact Section: 1 contact_info record
INSERT INTO contact_info (id, phone_number, email_address, physical_address, created_at, updated_at) VALUES
('f6000001-0000-0000-0000-000000000001', '+1 (555) 123-4567', 'info@simplecms.com', '123 Innovation Drive, Suite 400, San Francisco, CA 94105, USA', NOW(), NOW());
```

### 5.2 Seed Data Summary

| ID (suffix) | Phone Number | Email Address | Physical Address |
|-------------|--------------|---------------|------------------|
| ...0001 | +1 (555) 123-4567 | info@simplecms.com | 123 Innovation Drive, Suite 400, San Francisco, CA 94105, USA |

---

## 6. Test Scenarios

### NAV-CTS-001: Contact section is visible on the landing page

| Field | Value |
|-------|-------|
| ID | NAV-CTS-001 |
| Title | Contact section is visible on the landing page |
| Priority | High |
| Source Story | USL000018 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The section is visible with the title "Contact Us". |

---

### VIEW-CTS-001: Contact information is displayed correctly

| Field | Value |
|-------|-------|
| ID | VIEW-CTS-001 |
| Title | Contact information is displayed correctly |
| Priority | High |
| Source Story | USL000018, NFRL00075 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact information area is displayed. |
| 3 | Verify phone number display. | The phone number "+1 (555) 123-4567" is displayed alongside a phone icon (at least 16px). |
| 4 | Verify email address display. | The email address "info@simplecms.com" is displayed alongside an email icon (at least 16px). |
| 5 | Verify physical address display. | The address "123 Innovation Drive, Suite 400, San Francisco, CA 94105, USA" is displayed alongside a location icon (at least 16px). |

---

### CRUD-CTS-001: Submit contact form successfully

| Field | Value |
|-------|-------|
| ID | CRUD-CTS-001 |
| Title | Submit contact form successfully |
| Priority | High |
| Source Story | USL000018 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact form is displayed with name, email, message fields, and a submit button. |
| 3 | Enter "Test User" in the name field. | The name field accepts the input. |
| 4 | Enter "testuser@example.com" in the email field. | The email field accepts the input. |
| 5 | Enter "This is a test message for the contact form." in the message field. | The message field accepts the input. |
| 6 | Complete the CAPTCHA challenge (if applicable in test environment). | CAPTCHA is validated. |
| 7 | Click the "Submit" button. | A success message is displayed confirming the message has been sent. The form fields are cleared. |
| 8 | Verify in database: `SELECT * FROM contact_messages WHERE sender_email = 'testuser@example.com';` | A new record exists with `sender_name = 'Test User'`, `sender_email = 'testuser@example.com'`, and the submitted message content. |

---

### VAL-CTS-001: Required fields validation

| Field | Value |
|-------|-------|
| ID | VAL-CTS-001 |
| Title | Required fields validation |
| Priority | High |
| Source Story | USL000018, NFRL00078 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact form is displayed. |
| 3 | Leave all fields empty and click "Submit". | Validation errors are shown for the name, email, and message fields indicating they are required. The form is not submitted. |
| 4 | Enter only the name "Test User" and click "Submit". | Validation errors are shown for the email and message fields. The form is not submitted. |
| 5 | Enter name and email, leave message empty, and click "Submit". | A validation error is shown for the message field. The form is not submitted. |

---

### VAL-CTS-002: Email format validation

| Field | Value |
|-------|-------|
| ID | VAL-CTS-002 |
| Title | Email format validation |
| Priority | High |
| Source Story | USL000018 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact form is displayed. |
| 3 | Enter "Test User" in the name field. | The name field accepts the input. |
| 4 | Enter "invalid-email" in the email field. | The email field accepts the input. |
| 5 | Enter "Test message" in the message field. | The message field accepts the input. |
| 6 | Click the "Submit" button. | A validation error is shown for the email field indicating the format is invalid (e.g., "Please enter a valid email address"). The form is not submitted. |
| 7 | Change the email to "testuser@example.com". | The email field accepts the corrected input. |
| 8 | Click the "Submit" button (with CAPTCHA if applicable). | The form submits successfully (or proceeds to CAPTCHA step). |

---

### VAL-CTS-003: Message field maximum 500 characters

| Field | Value |
|-------|-------|
| ID | VAL-CTS-003 |
| Title | Message field maximum 500 characters |
| Priority | Medium |
| Source Story | NFRL00078 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact form is displayed. |
| 3 | Enter "Test User" in the name field and "testuser@example.com" in the email field. | Fields accept the input. |
| 4 | Enter a message with exactly 501 characters in the message field. | Either the field restricts input to 500 characters (via `maxlength` attribute) or the extra character is accepted for server-side validation. |
| 5 | Click the "Submit" button (with CAPTCHA if applicable). | If `maxlength` is set, the field only contains 500 characters and the form submits. If server-side validation, a validation error is shown indicating the message exceeds 500 characters. |

---

### VAL-CTS-004: CAPTCHA is present on the contact form

| Field | Value |
|-------|-------|
| ID | VAL-CTS-004 |
| Title | CAPTCHA is present on the contact form |
| Priority | High |
| Source Story | NFRL00084 |

**Steps:**

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Navigate to `/`. | The landing page loads successfully. |
| 2 | Scroll to the Contact section. | The contact form is displayed. |
| 3 | Inspect the form area for CAPTCHA. | A CAPTCHA widget (e.g., reCAPTCHA, hCaptcha, or similar) is visible within or near the contact form, positioned before the submit button. |

---

## 7. Cleanup Script

```sql
-- Remove seeded contact info
DELETE FROM contact_info WHERE id = 'f6000001-0000-0000-0000-000000000001';

-- Remove any contact messages created during testing
DELETE FROM contact_messages WHERE sender_email = 'testuser@example.com';
```

---

## 8. Traceability Matrix

| Scenario ID | Source Story | NFR | Constraint | Entities |
|-------------|-------------|-----|------------|----------|
| NAV-CTS-001 | USL000018 | NFRL00072 | — | ContactInfo |
| VIEW-CTS-001 | USL000018 | NFRL00075 | — | ContactInfo |
| CRUD-CTS-001 | USL000018 | NFRL00078 | — | ContactInfo, ContactMessage |
| VAL-CTS-001 | USL000018 | NFRL00078 | — | ContactMessage |
| VAL-CTS-002 | USL000018 | — | — | ContactMessage |
| VAL-CTS-003 | USL000018 | NFRL00078 | — | ContactMessage |
| VAL-CTS-004 | USL000018 | NFRL00084 | — | ContactMessage |
