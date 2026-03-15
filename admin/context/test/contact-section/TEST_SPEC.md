# Test Specification: Contact Section

## 1. Module Overview

| Attribute          | Value                                                                 |
|--------------------|-----------------------------------------------------------------------|
| Module Name        | Contact Section                                                       |
| Module Prefix      | CTS                                                                   |
| Application        | Admin Portal (SCMS)                                                   |
| Package            | com.simplecms.adminportal                                             |
| Layer              | L2 (Business Module)                                                  |
| Version            | v1.0.0                                                                |
| Date               | 2026-03-15                                                            |
| Database           | PostgreSQL 14 (localhost:5432/cms_db, cms_user/cms_password)          |
| Auth Mechanism     | Spring Security form login                                            |
| Roles              | USER, EDITOR, ADMIN                                                   |

## 2. Layer Classification Reasoning

This module is classified as **L2 (Business Module)** because it manages contact information and message responses, which are business-facing communication functions. It includes three entities (ContactInfo, ContactMessage, ContactResponse) that handle the company contact details and customer inquiry workflow. It depends on L1 modules for authentication and has no cross-module dependencies with other business modules.

## 3. Source Artifacts

| ID         | Type       | Description                                                        | Version |
|------------|------------|--------------------------------------------------------------------|---------|
| USA000084  | User Story | Configure contact section content (phone, email, address, LinkedIn)| v1.0.0  |
| USA000087  | User Story | View list of submitted messages from contact form                  | v1.0.0  |
| USA000090  | User Story | View details of submitted message                                  | v1.0.0  |
| USA000093  | User Story | Submit response to message sender                                  | v1.0.0  |
| NFRA00096  | NFR        | Phone number must be valid format                                  | v1.0.0  |
| NFRA00099  | NFR        | Email address must be valid format                                 | v1.0.0  |
| NFRA00102  | NFR        | Physical address max 500 chars                                     | v1.0.0  |
| NFRA00105  | NFR        | LinkedIn URL must be valid URL format                              | v1.0.0  |
| NFRA00108  | NFR        | Response sent asynchronously via batch job                         | v1.0.0  |
| CONSA0027  | Constraint | Contact info stored as single record; update overwrites existing   | v1.0.0  |

## 4. Prerequisites

- PostgreSQL 14 database `cms_db` is running on `localhost:5432`.
- Database user `cms_user` with password `cms_password` has full access to `cms_db`.
- The `contact_info`, `contact_messages`, and `contact_responses` tables exist with the schema defined in the model.
- The Admin Portal Spring Boot application is running and accessible.
- An EDITOR or ADMIN user is authenticated.
- SMTP server or mock email service is configured for response sending.

## 5. Data Seeding

Execute the following SQL scripts against the `cms_db` database using `psql`:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Seed an editor user (if not already seeded)
INSERT INTO users (id, email, password, first_name, last_name, role, status, force_password_change, last_login_at, version, created_at, created_by)
VALUES
  ('u7000000-0000-0000-0000-000000000001', 'ctseditor@simplecms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CTS', 'Editor', 'EDITOR', 'ACTIVE', false, NULL, 0, '2026-03-01 08:00:00', 'system')
ON CONFLICT (id) DO NOTHING;

-- Seed 1 contact_info record (single record design per CONSA0027)
INSERT INTO contact_info (id, phone_number, email_address, physical_address, linkedin_url, version, created_at, created_by)
VALUES
  ('c1000000-0000-0000-0000-000000000001', '+1-555-123-4567', 'info@simplecms.com', '123 Innovation Drive, Suite 400, San Francisco, CA 94105, United States', 'https://linkedin.com/company/simplecms', 0, '2026-03-01 08:00:00', 'admin');

-- Seed 15 contact_messages with various dates
INSERT INTO contact_messages (id, sender_name, sender_email, message_content, submitted_at, version, created_at, created_by)
VALUES
  ('cm100000-0000-0000-0000-000000000001', 'Alex Turner', 'alex.turner@email.com', 'Hi, I am interested in your web design services. Could you provide a quote for a small business website with approximately 10 pages?', '2026-03-15 09:00:00', 0, '2026-03-15 09:00:00', 'website'),
  ('cm100000-0000-0000-0000-000000000002', 'Maria Santos', 'maria.santos@email.com', 'We are looking for SEO optimization services for our e-commerce platform. Currently getting about 5000 monthly visitors and want to increase that significantly.', '2026-03-14 14:30:00', 0, '2026-03-14 14:30:00', 'website'),
  ('cm100000-0000-0000-0000-000000000003', 'Ryan Cooper', 'ryan.cooper@email.com', 'I need help with mobile app development for both iOS and Android. The app is for a food delivery service with GPS tracking features.', '2026-03-14 11:00:00', 0, '2026-03-14 11:00:00', 'website'),
  ('cm100000-0000-0000-0000-000000000004', 'Sophie Chen', 'sophie.chen@email.com', 'Can you tell me more about your cloud hosting plans? We have a medium-sized SaaS application with approximately 10,000 daily active users.', '2026-03-13 16:45:00', 0, '2026-03-13 16:45:00', 'website'),
  ('cm100000-0000-0000-0000-000000000005', 'James Kim', 'james.kim@email.com', 'We need a complete brand identity package including logo, color palette, typography, and brand guidelines document. Budget is flexible for quality work.', '2026-03-13 10:15:00', 0, '2026-03-13 10:15:00', 'website'),
  ('cm100000-0000-0000-0000-000000000006', 'Linda Parker', 'linda.parker@email.com', 'Interested in your data analytics services. We have a large dataset from our retail operations and need help setting up dashboards and automated reports.', '2026-03-12 13:20:00', 0, '2026-03-12 13:20:00', 'website'),
  ('cm100000-0000-0000-0000-000000000007', 'Daniel Wright', 'daniel.wright@email.com', 'Looking for cybersecurity consulting. We recently had a security incident and want a thorough audit of our infrastructure and web applications.', '2026-03-12 08:50:00', 0, '2026-03-12 08:50:00', 'website'),
  ('cm100000-0000-0000-0000-000000000008', 'Priya Patel', 'priya.patel@email.com', 'We are a startup and need help building our MVP. The product is a project management tool with collaboration features similar to Trello but for education.', '2026-03-11 15:30:00', 0, '2026-03-11 15:30:00', 'website'),
  ('cm100000-0000-0000-0000-000000000009', 'Tom Baker', 'tom.baker@email.com', 'Do you offer maintenance and support packages for existing websites? Our current site was built with WordPress and needs regular updates.', '2026-03-11 09:10:00', 0, '2026-03-11 09:10:00', 'website'),
  ('cm100000-0000-0000-0000-000000000010', 'Emma Wilson', 'emma.wilson@email.com', 'I would like to discuss a social media marketing campaign for our new product launch in Q2. We need coverage on Instagram, LinkedIn, and Twitter.', '2026-03-10 17:00:00', 0, '2026-03-10 17:00:00', 'website'),
  ('cm100000-0000-0000-0000-000000000011', 'Carlos Rodriguez', 'carlos.rodriguez@email.com', 'Interested in e-commerce development. We currently sell through marketplace platforms and want our own online store with payment processing.', '2026-03-10 11:40:00', 0, '2026-03-10 11:40:00', 'website'),
  ('cm100000-0000-0000-0000-000000000012', 'Hannah Lee', 'hannah.lee@email.com', 'Can you provide information about your content creation services? We need blog posts, product descriptions, and email newsletter content on a monthly basis.', '2026-03-09 14:55:00', 0, '2026-03-09 14:55:00', 'website'),
  ('cm100000-0000-0000-0000-000000000013', 'Steve Miller', 'steve.miller@email.com', 'We need IT consulting for our digital transformation project. Currently using legacy systems and want to migrate to modern cloud-based solutions.', '2026-03-09 10:25:00', 0, '2026-03-09 10:25:00', 'website'),
  ('cm100000-0000-0000-0000-000000000014', 'Olivia Taylor', 'olivia.taylor@email.com', 'Looking for a team to build a customer portal for our insurance company. Needs to be secure and handle sensitive personal data.', '2026-03-08 16:15:00', 0, '2026-03-08 16:15:00', 'website'),
  ('cm100000-0000-0000-0000-000000000015', 'Nathan Brooks', 'nathan.brooks@email.com', 'I am impressed by your portfolio and would like to schedule a meeting to discuss a potential long-term partnership for ongoing development work.', '2026-03-08 09:30:00', 0, '2026-03-08 09:30:00', 'website');

-- Seed 3 contact_responses for some messages
INSERT INTO contact_responses (id, contact_message_id, responder_name, responder_email, response_content, sent_at, version, created_at, created_by)
VALUES
  ('cr100000-0000-0000-0000-000000000001', 'cm100000-0000-0000-0000-000000000001', 'CTS Editor', 'ctseditor@simplecms.com', 'Thank you for your interest in our web design services, Alex! We would love to help with your small business website. I will send over a detailed quote within 24 hours. In the meantime, could you share some examples of websites you like?', '2026-03-15 10:30:00', 0, '2026-03-15 10:00:00', 'editor'),
  ('cr100000-0000-0000-0000-000000000002', 'cm100000-0000-0000-0000-000000000004', 'CTS Editor', 'ctseditor@simplecms.com', 'Hi Sophie! Thanks for reaching out about our cloud hosting plans. For a SaaS application with 10,000 DAUs, I would recommend our Business tier. Let me schedule a call to discuss your specific requirements and provide a tailored proposal.', '2026-03-14 09:00:00', 0, '2026-03-13 17:30:00', 'editor'),
  ('cr100000-0000-0000-0000-000000000003', 'cm100000-0000-0000-0000-000000000007', 'CTS Editor', 'ctseditor@simplecms.com', 'Hello Daniel, thank you for contacting us about cybersecurity consulting. Security incidents require immediate attention. We can schedule an emergency audit within 48 hours. Please call us directly so we can begin the assessment process.', NULL, 0, '2026-03-12 10:00:00', 'editor');
```

## 6. Test Scenarios

### 6.1 Navigation Tests

#### NAV-CTS-001: Navigate to Contact Section

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | NAV-CTS-001                                                           |
| Title            | Navigate to the contact section management area                       |
| Type             | Navigation                                                            |
| Priority         | High                                                                  |
| Source           | USA000084, USA000087                                                  |
| Preconditions    | User is logged in as EDITOR.                                         |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on "Contact" link in the sidebar navigation.  | The contact section management area is displayed.                    |
| 2    | Verify there are tabs or sections for "Contact Info" and "Messages". | Both contact info management and messages list are accessible.       |

---

### 6.2 View/Detail Tests

#### VIEW-CTS-001: View Contact Information

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VIEW-CTS-001                                                          |
| Title            | View the current contact information                                  |
| Type             | View/Detail                                                           |
| Priority         | High                                                                  |
| Source           | USA000084                                                             |
| Preconditions    | Seeded contact_info record exists.                                    |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Contact Info section.               | The contact information form is displayed with current values.       |
| 2    | Verify the phone number is "+1-555-123-4567".       | Phone number field shows the seeded value.                           |
| 3    | Verify the email is "info@simplecms.com".           | Email field shows the seeded value.                                  |
| 4    | Verify the address is displayed correctly.          | Address field shows "123 Innovation Drive, Suite 400, San Francisco, CA 94105, United States". |
| 5    | Verify the LinkedIn URL is displayed.               | LinkedIn URL field shows "https://linkedin.com/company/simplecms".   |

---

#### VIEW-CTS-002: View Messages List

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VIEW-CTS-002                                                          |
| Title            | View list of submitted contact messages                               |
| Type             | View/Detail                                                           |
| Priority         | High                                                                  |
| Source           | USA000087                                                             |
| Preconditions    | 15 seeded contact messages exist.                                     |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Messages section.                   | The messages list is displayed.                                      |
| 2    | Verify each message row shows: sender name, sender email, message content (truncated), and submitted date. | All columns are visible with correct data.                          |
| 3    | Verify long message content is truncated with ellipsis. | Messages longer than the display limit show "..." at the end.       |
| 4    | Verify the most recent message ("Alex Turner") appears first. | Messages are ordered by submitted date descending.                  |

---

#### VIEW-CTS-003: View Message Detail

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VIEW-CTS-003                                                          |
| Title            | View full details of a submitted message                              |
| Type             | View/Detail                                                           |
| Priority         | High                                                                  |
| Source           | USA000090                                                             |
| Preconditions    | Seeded message from "Alex Turner" exists.                             |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Click on the "Alex Turner" message row in the list. | The message detail page is displayed.                                |
| 2    | Verify sender name is "Alex Turner".                | Sender name is displayed.                                            |
| 3    | Verify sender email is "alex.turner@email.com".     | Sender email is displayed.                                           |
| 4    | Verify the full message content is displayed (not truncated). | Full message text is visible.                                       |
| 5    | Verify the submitted date/time is "2026-03-15 09:00:00". | Submitted date is displayed.                                        |
| 6    | Verify a "Respond" form or button is available.     | A response form or respond button is visible.                        |
| 7    | Verify the previous response is shown (if any).     | The response from "CTS Editor" is displayed below the message.       |

---

### 6.3 CRUD Tests

#### CRUD-CTS-001: Update Contact Information (Overwrites)

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-CTS-001                                                          |
| Title            | Update contact information - overwrites existing single record        |
| Type             | CRUD (Update)                                                         |
| Priority         | Critical                                                              |
| Source           | USA000084, CONSA0027                                                  |
| Preconditions    | Seeded contact_info record exists.                                    |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Contact Info section.               | Current contact information is displayed.                            |
| 2    | Change the phone number to "+1-555-987-6543".       | Phone number field is updated.                                       |
| 3    | Change the email to "contact@simplecms.com".        | Email field is updated.                                              |
| 4    | Change the address to "456 Tech Boulevard, Floor 12, New York, NY 10001, United States". | Address field is updated.                                           |
| 5    | Click "Save" or "Update".                           | Success message "Contact information updated successfully" is displayed. |
| 6    | Refresh the page.                                   | Updated values are displayed. Only one record exists in `contact_info` table (CONSA0027). |
| 7    | Verify in database: `SELECT COUNT(*) FROM contact_info;` | Count is 1 (single record, overwritten, not a new record).          |

---

#### CRUD-CTS-002: Submit Response to Message

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | CRUD-CTS-002                                                          |
| Title            | Submit a response to a contact message sender                         |
| Type             | CRUD (Create)                                                         |
| Priority         | High                                                                  |
| Source           | USA000093, NFRA00108                                                  |
| Preconditions    | Seeded message from "Maria Santos" exists (no response yet).          |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the message detail for "Maria Santos".  | Message detail is displayed with full content.                       |
| 2    | Click "Respond" or locate the response form.        | Response form is displayed with responder name and email pre-filled. |
| 3    | Enter a response: "Hi Maria! Thank you for your interest in our SEO services. We have helped many e-commerce platforms achieve significant traffic growth. Let me prepare a custom proposal for your platform." | Response content field is populated.                                |
| 4    | Click "Send Response" or "Submit".                  | Success message displayed. Response is saved.                        |
| 5    | Verify the response record exists in the database.  | A new record in `contact_responses` with `contact_message_id` matching Maria's message. |
| 6    | Verify `sent_at` is NULL (pending batch job).       | The `sent_at` field is NULL, indicating the email has not been sent yet by the batch job (NFRA00108). |

---

### 6.4 Validation Tests

#### VAL-CTS-001: Phone Number Format Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-CTS-001                                                           |
| Title            | Phone number must be in a valid format                                |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00096                                                             |
| Preconditions    | Contact info form is displayed.                                       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter `abc-not-a-phone` in the phone number field.  | Validation error "Please enter a valid phone number" is displayed.   |
| 2    | Enter `+1-555-123-4567` in the phone number field.  | Input is accepted without errors.                                    |

---

#### VAL-CTS-002: Email Address Format Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-CTS-002                                                           |
| Title            | Email address must be in a valid format                               |
| Type             | Validation                                                            |
| Priority         | High                                                                  |
| Source           | NFRA00099                                                             |
| Preconditions    | Contact info form is displayed.                                       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter `not-an-email` in the email field.            | Validation error "Please enter a valid email address" is displayed.  |
| 2    | Enter `info@simplecms.com` in the email field.      | Input is accepted without errors.                                    |

---

#### VAL-CTS-003: Physical Address Max Length Validation

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | VAL-CTS-003                                                           |
| Title            | Physical address must not exceed 500 characters                       |
| Type             | Validation                                                            |
| Priority         | Medium                                                                |
| Source           | NFRA00102                                                             |
| Preconditions    | Contact info form is displayed.                                       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Enter an address with 501 characters.               | Validation error "Address must not exceed 500 characters" is displayed. |
| 2    | Enter an address with exactly 500 characters.       | Input is accepted without errors.                                    |

---

### 6.5 Pagination Tests

#### PAGE-CTS-001: Messages List Pagination

| Attribute        | Value                                                                 |
|------------------|-----------------------------------------------------------------------|
| Scenario ID      | PAGE-CTS-001                                                          |
| Title            | Contact messages list supports pagination                             |
| Type             | Pagination                                                            |
| Priority         | Medium                                                                |
| Source           | USA000087                                                             |
| Preconditions    | 15 contact messages are seeded.                                       |

**Steps:**

| Step | Action                                              | Expected Result                                                      |
|------|-----------------------------------------------------|----------------------------------------------------------------------|
| 1    | Navigate to the Messages list.                      | First page of messages is displayed (e.g., 10 per page).            |
| 2    | Verify pagination controls are visible.             | Page numbers or Next/Previous buttons are displayed.                 |
| 3    | Click "Next" or page 2.                             | Remaining messages are displayed.                                    |
| 4    | Click "Previous" or page 1.                         | First page is displayed again.                                       |

---

## 7. Data Cleanup

Execute the following SQL scripts to clean up test data:

```sql
-- Connect to database
-- psql -h localhost -p 5432 -U cms_user -d cms_db

-- Delete contact responses first (FK dependency on contact_messages)
DELETE FROM contact_responses WHERE id IN (
  'cr100000-0000-0000-0000-000000000001',
  'cr100000-0000-0000-0000-000000000002',
  'cr100000-0000-0000-0000-000000000003'
);

-- Delete any responses created during tests
DELETE FROM contact_responses WHERE contact_message_id = 'cm100000-0000-0000-0000-000000000002';

-- Delete seeded contact messages
DELETE FROM contact_messages WHERE id IN (
  'cm100000-0000-0000-0000-000000000001',
  'cm100000-0000-0000-0000-000000000002',
  'cm100000-0000-0000-0000-000000000003',
  'cm100000-0000-0000-0000-000000000004',
  'cm100000-0000-0000-0000-000000000005',
  'cm100000-0000-0000-0000-000000000006',
  'cm100000-0000-0000-0000-000000000007',
  'cm100000-0000-0000-0000-000000000008',
  'cm100000-0000-0000-0000-000000000009',
  'cm100000-0000-0000-0000-000000000010',
  'cm100000-0000-0000-0000-000000000011',
  'cm100000-0000-0000-0000-000000000012',
  'cm100000-0000-0000-0000-000000000013',
  'cm100000-0000-0000-0000-000000000014',
  'cm100000-0000-0000-0000-000000000015'
);

-- Delete seeded contact info
DELETE FROM contact_info WHERE id = 'c1000000-0000-0000-0000-000000000001';

-- Delete seeded editor user
DELETE FROM users WHERE id = 'u7000000-0000-0000-0000-000000000001';
```

## 8. Traceability Matrix

| Scenario ID    | Type          | USA000084 | USA000087 | USA000090 | USA000093 | NFRA00096 | NFRA00099 | NFRA00102 | NFRA00105 | NFRA00108 | CONSA0027 |
|----------------|---------------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|-----------|
| NAV-CTS-001    | Navigation    | X         | X         |           |           |           |           |           |           |           |           |
| VIEW-CTS-001   | View/Detail   | X         |           |           |           |           |           |           |           |           |           |
| VIEW-CTS-002   | View/Detail   |           | X         |           |           |           |           |           |           |           |           |
| VIEW-CTS-003   | View/Detail   |           |           | X         |           |           |           |           |           |           |           |
| CRUD-CTS-001   | CRUD (Update) | X         |           |           |           | X         | X         | X         | X         |           | X         |
| CRUD-CTS-002   | CRUD (Create) |           |           |           | X         |           |           |           |           | X         |           |
| VAL-CTS-001    | Validation    |           |           |           |           | X         |           |           |           |           |           |
| VAL-CTS-002    | Validation    |           |           |           |           |           | X         |           |           |           |           |
| VAL-CTS-003    | Validation    |           |           |           |           |           |           | X         |           |           |           |
| PAGE-CTS-001   | Pagination    |           | X         |           |           |           |           |           |           |           |           |
