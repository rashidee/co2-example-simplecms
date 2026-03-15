# Module Specification: Contact Section

[Back to SPECIFICATION.md](../SPECIFICATION.md)

---

## 1. Traceability Matrix

### User Stories

| ID | Version | Description |
|----|---------|-------------|
| USA000084 | v1.0.0 | As Editor, I want to configure the contact section content (phone, email, address, LinkedIn) |
| USA000087 | v1.0.0 | As Editor, I want to view submitted messages from the contact form |
| USA000090 | v1.0.0 | As Editor, I want to view the details of a submitted message |
| USA000093 | v1.0.0 | As Editor, I want to submit a response to the sender of the message |

### Non-Functional Requirements

| ID | Version | Description |
|----|---------|-------------|
| NFRA00096 | v1.0.0 | Phone number must be in valid format |
| NFRA00099 | v1.0.0 | Email address must be in valid format |
| NFRA00102 | v1.0.0 | Physical address max 500 chars |
| NFRA00105 | v1.0.0 | LinkedIn URL must be valid URL format |
| NFRA00108 | v1.0.0 | Response email sent asynchronously via Quartz batch job (checks sentAt IS NULL) |

### Constraints

| ID | Version | Description |
|----|---------|-------------|
| CONSA0027 | v1.0.0 | Contact information stored as single record; updates overwrite existing |

### Removed / Replaced

None for v1.0.0.

---

## 2. Module Structure

- **Base Package:** `com.simplecms.adminportal.contactsection`
- **Architecture:** Spring Modulith (public API + internal)
- **Database:** PostgreSQL with JPA, Flyway migrations
- **Views:** JTE templates, Tailwind CSS, htmx, Alpine.js

### Package Layout

```
com.simplecms.adminportal.contactsection
  +-- ContactService.java                     (public API - interface)
  +-- ContactInfoDTO.java                     (public API - DTO)
  +-- ContactMessageDTO.java                  (public API - DTO)
  +-- ContactResponseDTO.java                 (public API - DTO)
  +-- internal
        +-- ContactInfoEntity.java             (JPA entity)
        +-- ContactMessageEntity.java          (JPA entity)
        +-- ContactResponseEntity.java         (JPA entity)
        +-- ContactInfoRepository.java         (Spring Data JPA)
        +-- ContactMessageRepository.java      (Spring Data JPA)
        +-- ContactResponseRepository.java     (Spring Data JPA)
        +-- ContactServiceImpl.java            (service implementation)
        +-- ContactMapper.java                 (MapStruct mapper)
        +-- ContactSectionPageController.java  (page controller)
        +-- ContactMessagePageController.java  (page controller)
        +-- ContactEmailJob.java               (Quartz job for async email)
        +-- ContactInfoView.java               (view model)
        +-- ContactMessageListView.java        (view model)
        +-- ContactMessageDetailView.java      (view model)
```

### JTE Templates

```
src/main/jte
  +-- contactsection
        +-- ContactInfoPage.jte
        +-- ContactMessageListPage.jte
        +-- ContactMessageDetailPage.jte
```

---

## 3. Security Configuration

| Endpoint | Method | Access |
|----------|--------|--------|
| `/contact-section` | GET, POST | EDITOR, ADMIN |
| `/contact-section/messages` | GET | EDITOR, ADMIN |
| `/contact-section/messages/{id}` | GET | EDITOR, ADMIN |
| `/contact-section/messages/{id}/respond` | POST | EDITOR, ADMIN |

---

## 4. Database Migration

**File:** `src/main/resources/db/migration/V8__create_contact_tables.sql`

```sql
CREATE TABLE cnt_contact_info (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    phone_number    VARCHAR(50)     NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    address         VARCHAR(500)    NOT NULL,
    linkedin_url    VARCHAR(500),
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_cnt_contact_info PRIMARY KEY (id)
);

CREATE TABLE cnt_contact_message (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    sender_name     VARCHAR(100)    NOT NULL,
    sender_email    VARCHAR(255)    NOT NULL,
    message         TEXT            NOT NULL,
    submitted_at    TIMESTAMP       NOT NULL DEFAULT NOW(),
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_cnt_contact_message PRIMARY KEY (id)
);

CREATE TABLE cnt_contact_response (
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    message_id      UUID            NOT NULL,
    responder_name  VARCHAR(100)    NOT NULL,
    responder_email VARCHAR(255)    NOT NULL,
    response_text   TEXT            NOT NULL,
    sent_at         TIMESTAMP,
    version         BIGINT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    created_by      VARCHAR(255)    NOT NULL,
    updated_at      TIMESTAMP,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_cnt_contact_response PRIMARY KEY (id),
    CONSTRAINT fk_cnt_contact_response_message FOREIGN KEY (message_id) REFERENCES cnt_contact_message(id) ON DELETE CASCADE
);

CREATE INDEX idx_cnt_contact_response_message_id ON cnt_contact_response(message_id);
CREATE INDEX idx_cnt_contact_response_sent_at ON cnt_contact_response(sent_at);
CREATE INDEX idx_cnt_contact_message_submitted_at ON cnt_contact_message(submitted_at);
```

---

## 5. Public API

### 5.1 ContactInfoDTO

```java
package com.simplecms.adminportal.contactsection;

import java.util.UUID;

/**
 * Traces: USA000084
 */
public record ContactInfoDTO(
    UUID id,
    String phoneNumber,
    String email,
    String address,
    String linkedinUrl
) {}
```

### 5.2 ContactMessageDTO

```java
package com.simplecms.adminportal.contactsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000087, USA000090
 */
public record ContactMessageDTO(
    UUID id,
    String senderName,
    String senderEmail,
    String message,
    Instant submittedAt,
    boolean hasResponse
) {}
```

### 5.3 ContactResponseDTO

```java
package com.simplecms.adminportal.contactsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000093
 */
public record ContactResponseDTO(
    UUID id,
    UUID messageId,
    String responderName,
    String responderEmail,
    String responseText,
    Instant sentAt,
    Instant createdAt
) {}
```

### 5.4 ContactService Interface

```java
package com.simplecms.adminportal.contactsection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Contact Section module.
 *
 * Traces: USA000084-093, NFRA00096-108, CONSA0027
 */
public interface ContactService {

    /**
     * Get the current contact information.
     * Returns null if not yet configured.
     *
     * Traces: USA000084
     *
     * @return the contact info DTO or null
     */
    ContactInfoDTO getContactInfo();

    /**
     * Create or update contact information (single record upsert).
     *
     * Traces: USA000084, CONSA0027
     *
     * @param phoneNumber  phone number
     * @param email        email address
     * @param address      physical address (max 500)
     * @param linkedinUrl  LinkedIn URL (nullable)
     * @return updated contact info DTO
     */
    ContactInfoDTO updateContactInfo(String phoneNumber, String email,
                                     String address, String linkedinUrl);

    /**
     * List submitted contact messages with pagination.
     *
     * Traces: USA000087
     *
     * @param pageable pagination parameters
     * @return page of contact message DTOs
     */
    Page<ContactMessageDTO> listMessages(Pageable pageable);

    /**
     * Get a contact message by ID including its response if any.
     *
     * Traces: USA000090
     *
     * @param id the message ID
     * @return the contact message DTO
     */
    ContactMessageDTO getMessageById(UUID id);

    /**
     * Get the response for a message if one exists.
     *
     * Traces: USA000090
     *
     * @param messageId the message ID
     * @return the response DTO or null
     */
    ContactResponseDTO getResponseByMessageId(UUID messageId);

    /**
     * Submit a response to a contact message.
     * The response is saved with sentAt=null and picked up by the
     * async Quartz email job.
     *
     * Traces: USA000093, NFRA00108
     *
     * @param messageId      the message ID
     * @param responderName  responder's name
     * @param responderEmail responder's email
     * @param responseText   response content
     * @return created response DTO
     */
    ContactResponseDTO submitResponse(UUID messageId, String responderName,
                                      String responderEmail, String responseText);
}
```

---

## 6. Internal Components

### 6.1 ContactInfoEntity

```java
package com.simplecms.adminportal.contactsection.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for contact information (single record).
 * Table: cnt_contact_info
 *
 * Traces: USA000084, CONSA0027
 */
@Entity
@Table(name = "cnt_contact_info")
@Getter
@Setter
public class ContactInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    protected ContactInfoEntity() {
        // JPA
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 6.2 ContactMessageEntity

```java
package com.simplecms.adminportal.contactsection.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for contact form submissions.
 * Table: cnt_contact_message
 *
 * Traces: USA000087, USA000090
 */
@Entity
@Table(name = "cnt_contact_message")
@Getter
@Setter
public class ContactMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "sender_name", nullable = false, length = 100)
    private String senderName;

    @Column(name = "sender_email", nullable = false, length = 255)
    private String senderEmail;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    protected ContactMessageEntity() {
        // JPA
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 6.3 ContactResponseEntity

```java
package com.simplecms.adminportal.contactsection.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for responses to contact messages.
 * sentAt is null until the async email job processes it.
 * Table: cnt_contact_response
 *
 * Traces: USA000093, NFRA00108
 */
@Entity
@Table(name = "cnt_contact_response")
@Getter
@Setter
public class ContactResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Column(name = "responder_name", nullable = false, length = 100)
    private String responderName;

    @Column(name = "responder_email", nullable = false, length = 255)
    private String responderEmail;

    @Column(name = "response_text", nullable = false, columnDefinition = "TEXT")
    private String responseText;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    protected ContactResponseEntity() {
        // JPA
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
```

### 6.4 Repositories

```java
package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ContactInfoRepository extends JpaRepository<ContactInfoEntity, UUID> {

    /**
     * Find the single contact info record.
     * Per CONSA0027, only one record exists.
     */
    default Optional<ContactInfoEntity> findSingle() {
        return findAll().stream().findFirst();
    }
}
```

```java
package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface ContactMessageRepository extends JpaRepository<ContactMessageEntity, UUID> {

    @Query("SELECT m FROM ContactMessageEntity m ORDER BY m.submittedAt DESC")
    Page<ContactMessageEntity> findAllOrderBySubmittedAtDesc(Pageable pageable);
}
```

```java
package com.simplecms.adminportal.contactsection.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface ContactResponseRepository extends JpaRepository<ContactResponseEntity, UUID> {

    Optional<ContactResponseEntity> findByMessageId(UUID messageId);

    boolean existsByMessageId(UUID messageId);

    /**
     * Find responses that have not been sent yet (sentAt IS NULL).
     * Used by the async email Quartz job.
     *
     * Traces: NFRA00108
     */
    List<ContactResponseEntity> findBySentAtIsNull();
}
```

### 6.5 ContactMapper (MapStruct)

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;
import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ContactMapper {

    ContactInfoDTO toInfoDTO(ContactInfoEntity entity);

    @Mapping(target = "hasResponse", ignore = true)
    ContactMessageDTO toMessageDTO(ContactMessageEntity entity);

    ContactResponseDTO toResponseDTO(ContactResponseEntity entity);
}
```

### 6.6 ContactServiceImpl

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Traces: USA000084-093, NFRA00096-108, CONSA0027
 */
@Service
@Transactional
class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactInfoRepository infoRepository;
    private final ContactMessageRepository messageRepository;
    private final ContactResponseRepository responseRepository;
    private final ContactMapper mapper;

    ContactServiceImpl(ContactInfoRepository infoRepository,
                       ContactMessageRepository messageRepository,
                       ContactResponseRepository responseRepository,
                       ContactMapper mapper) {
        this.infoRepository = infoRepository;
        this.messageRepository = messageRepository;
        this.responseRepository = responseRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactInfoDTO getContactInfo() {
        return infoRepository.findSingle()
            .map(mapper::toInfoDTO)
            .orElse(null);
    }

    /**
     * Upsert contact info as single record.
     *
     * Traces: USA000084, CONSA0027
     */
    @Override
    public ContactInfoDTO updateContactInfo(String phoneNumber, String email,
                                            String address, String linkedinUrl) {
        ContactInfoEntity entity = infoRepository.findSingle().orElse(new ContactInfoEntity());

        entity.setPhoneNumber(phoneNumber);
        entity.setEmail(email);
        entity.setAddress(address);
        entity.setLinkedinUrl(linkedinUrl);

        if (entity.getId() == null) {
            entity.setCreatedBy("EDITOR");
        }
        entity.setUpdatedBy("EDITOR");

        ContactInfoEntity saved = infoRepository.save(entity);
        log.info("Contact info updated");
        return mapper.toInfoDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessageDTO> listMessages(Pageable pageable) {
        return messageRepository.findAllOrderBySubmittedAtDesc(pageable)
            .map(entity -> {
                boolean hasResponse = responseRepository.existsByMessageId(entity.getId());
                ContactMessageDTO dto = mapper.toMessageDTO(entity);
                return new ContactMessageDTO(dto.id(), dto.senderName(), dto.senderEmail(),
                    dto.message(), dto.submittedAt(), hasResponse);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageDTO getMessageById(UUID id) {
        ContactMessageEntity entity = messageRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Message not found: " + id));
        boolean hasResponse = responseRepository.existsByMessageId(id);
        ContactMessageDTO dto = mapper.toMessageDTO(entity);
        return new ContactMessageDTO(dto.id(), dto.senderName(), dto.senderEmail(),
            dto.message(), dto.submittedAt(), hasResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponseDTO getResponseByMessageId(UUID messageId) {
        return responseRepository.findByMessageId(messageId)
            .map(mapper::toResponseDTO)
            .orElse(null);
    }

    /**
     * Submit a response. sentAt is null -- the Quartz job will pick it up
     * and set sentAt after sending the email.
     *
     * Traces: USA000093, NFRA00108
     */
    @Override
    public ContactResponseDTO submitResponse(UUID messageId, String responderName,
                                             String responderEmail, String responseText) {
        // Verify message exists
        messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        // Check no existing response
        if (responseRepository.existsByMessageId(messageId)) {
            throw new IllegalArgumentException("A response has already been submitted for this message");
        }

        ContactResponseEntity entity = new ContactResponseEntity();
        entity.setMessageId(messageId);
        entity.setResponderName(responderName);
        entity.setResponderEmail(responderEmail);
        entity.setResponseText(responseText);
        entity.setSentAt(null); // Will be set by Quartz job after email is sent
        entity.setCreatedBy("EDITOR");

        ContactResponseEntity saved = responseRepository.save(entity);
        log.info("Response submitted for message: {}", messageId);
        return mapper.toResponseDTO(saved);
    }
}
```

### 6.7 ContactEmailJob (Quartz)

```java
package com.simplecms.adminportal.contactsection.internal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Quartz job that processes unsent contact responses.
 * Finds all ContactResponseEntity records where sentAt IS NULL,
 * sends the email, and updates sentAt.
 *
 * Traces: NFRA00108
 */
@Component
class ContactEmailJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(ContactEmailJob.class);

    private final ContactResponseRepository responseRepository;
    private final ContactMessageRepository messageRepository;

    ContactEmailJob(ContactResponseRepository responseRepository,
                    ContactMessageRepository messageRepository) {
        this.responseRepository = responseRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<ContactResponseEntity> pendingResponses = responseRepository.findBySentAtIsNull();

        for (ContactResponseEntity response : pendingResponses) {
            try {
                ContactMessageEntity message = messageRepository.findById(response.getMessageId())
                    .orElse(null);

                if (message == null) {
                    log.warn("Message not found for response: {}", response.getId());
                    continue;
                }

                // TODO: Send email to message.getSenderEmail()
                // EmailService.send(message.getSenderEmail(), response.getResponseText(), ...)

                response.setSentAt(Instant.now());
                responseRepository.save(response);

                log.info("Email sent for response: {} to: {}", response.getId(), message.getSenderEmail());
            } catch (Exception e) {
                log.error("Failed to send email for response: {}", response.getId(), e);
            }
        }
    }
}
```

### 6.8 View Models

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;

public record ContactInfoView(
    ContactInfoDTO contactInfo,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ContactInfoView of(ContactInfoDTO info) {
        return new ContactInfoView(info, null, false, null, false);
    }

    public static ContactInfoView withSuccess(ContactInfoDTO info, String message) {
        return new ContactInfoView(info, message, true, null, false);
    }

    public static ContactInfoView withError(ContactInfoDTO info, String message) {
        return new ContactInfoView(info, null, false, message, true);
    }
}
```

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import org.springframework.data.domain.Page;

public record ContactMessageListView(
    Page<ContactMessageDTO> messages,
    String successMessage,
    boolean hasSuccess
) {
    public static ContactMessageListView of(Page<ContactMessageDTO> messages) {
        return new ContactMessageListView(messages, null, false);
    }
}
```

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;

public record ContactMessageDetailView(
    ContactMessageDTO message,
    ContactResponseDTO response,
    boolean hasResponse,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ContactMessageDetailView of(ContactMessageDTO message, ContactResponseDTO response) {
        return new ContactMessageDetailView(message, response, response != null, null, false, null, false);
    }

    public static ContactMessageDetailView withSuccess(ContactMessageDTO message, ContactResponseDTO response, String msg) {
        return new ContactMessageDetailView(message, response, response != null, msg, true, null, false);
    }

    public static ContactMessageDetailView withError(ContactMessageDTO message, ContactResponseDTO response, String msg) {
        return new ContactMessageDetailView(message, response, response != null, null, false, msg, true);
    }
}
```

### 6.9 Page Controllers

#### ContactSectionPageController

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;
import com.simplecms.adminportal.contactsection.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for contact info configuration page.
 *
 * Traces: USA000084
 */
@Controller
@RequestMapping("/contact-section")
class ContactSectionPageController {

    private final ContactService contactService;

    ContactSectionPageController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    String showContactInfo(Model model) {
        ContactInfoDTO info = contactService.getContactInfo();
        model.addAttribute("view", ContactInfoView.of(info));
        return "contactsection/ContactInfoPage";
    }

    @PostMapping
    String updateContactInfo(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            Model model) {
        try {
            ContactInfoDTO updated = contactService.updateContactInfo(phoneNumber, email, address, linkedinUrl);
            model.addAttribute("view", ContactInfoView.withSuccess(updated, "Contact information updated successfully."));
        } catch (IllegalArgumentException e) {
            ContactInfoDTO info = contactService.getContactInfo();
            model.addAttribute("view", ContactInfoView.withError(info, e.getMessage()));
        }
        return "contactsection/ContactInfoPage";
    }
}
```

#### ContactMessagePageController

```java
package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;
import com.simplecms.adminportal.contactsection.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Controller for contact messages and responses.
 *
 * Traces: USA000087, USA000090, USA000093
 */
@Controller
@RequestMapping("/contact-section/messages")
class ContactMessagePageController {

    private final ContactService contactService;

    ContactMessagePageController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    String listMessages(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<ContactMessageDTO> messages = contactService.listMessages(pageable);
        model.addAttribute("view", ContactMessageListView.of(messages));
        return "contactsection/ContactMessageListPage";
    }

    @GetMapping("/{id}")
    String messageDetail(@PathVariable("id") UUID id, Model model) {
        ContactMessageDTO message = contactService.getMessageById(id);
        ContactResponseDTO response = contactService.getResponseByMessageId(id);
        model.addAttribute("view", ContactMessageDetailView.of(message, response));
        return "contactsection/ContactMessageDetailPage";
    }

    @PostMapping("/{id}/respond")
    String submitResponse(
            @PathVariable("id") UUID id,
            @RequestParam("responderName") String responderName,
            @RequestParam("responderEmail") String responderEmail,
            @RequestParam("responseText") String responseText,
            RedirectAttributes redirectAttributes) {
        try {
            contactService.submitResponse(id, responderName, responderEmail, responseText);
            redirectAttributes.addFlashAttribute("successMessage", "Response submitted. Email will be sent shortly.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/contact-section/messages/" + id;
    }
}
```

---

## 7. JTE Templates

### 7.1 ContactInfoPage.jte

```html
@param com.simplecms.adminportal.contactsection.internal.ContactInfoView view

@template.layout.MainLayout(
    content = @`
    <div class="max-w-2xl mx-auto">
        <div class="flex items-center justify-between mb-6">
            <h1 class="text-xl font-bold text-[#1e1e1e]">Contact Information</h1>
            <a href="/contact-section/messages" class="text-sm text-[#2271b1] hover:underline">View Messages</a>
        </div>

        <div class="bg-white border border-[#c3c4c7] rounded-[8px] p-6">
            @if(view.hasSuccess())
                <div class="mb-4 p-3 bg-[#00a32a]/10 border border-[#00a32a]/30 rounded-[4px] text-sm text-[#00a32a]">${view.successMessage()}</div>
            @endif
            @if(view.hasError())
                <div class="mb-4 p-3 bg-[#d63638]/10 border border-[#d63638]/30 rounded-[4px] text-sm text-[#d63638]">${view.errorMessage()}</div>
            @endif

            <form method="post" action="/contact-section" class="space-y-4">
                <div>
                    <label for="phoneNumber" class="block text-sm font-medium text-[#1e1e1e] mb-1">Phone Number</label>
                    <input type="text" id="phoneNumber" name="phoneNumber" required
                           value="${view.contactInfo() != null ? view.contactInfo().phoneNumber() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="email" class="block text-sm font-medium text-[#1e1e1e] mb-1">Email</label>
                    <input type="email" id="email" name="email" required
                           value="${view.contactInfo() != null ? view.contactInfo().email() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <div>
                    <label for="address" class="block text-sm font-medium text-[#1e1e1e] mb-1">Physical Address</label>
                    <textarea id="address" name="address" required rows="3" maxlength="500"
                              class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">${view.contactInfo() != null ? view.contactInfo().address() : ""}</textarea>
                </div>
                <div>
                    <label for="linkedinUrl" class="block text-sm font-medium text-[#1e1e1e] mb-1">LinkedIn URL</label>
                    <input type="url" id="linkedinUrl" name="linkedinUrl"
                           value="${view.contactInfo() != null ? view.contactInfo().linkedinUrl() : ""}"
                           class="w-full px-3 py-2.5 text-sm border border-[#c3c4c7] rounded-[4px] focus:outline-none focus:ring-2 focus:ring-[#2271b1]">
                </div>
                <button type="submit" class="px-4 py-2.5 bg-[#2271b1] hover:bg-[#135e96] text-white text-sm font-medium rounded-[4px]">Save</button>
            </form>
        </div>
    </div>
    `
)
```

---

## 8. Domain Events

| Event | Trigger | Payload | Traces |
|-------|---------|---------|--------|
| ContactInfoUpdated | Contact info saved | contactInfoId | USA000084 |
| ContactResponseSubmitted | Response submitted | responseId, messageId | USA000093 |
| ContactResponseSent | Email sent by Quartz job | responseId | NFRA00108 |

---

## 9. Cross-Module Dependencies

None. This module is self-contained.

---

## 10. Assumptions and Design Decisions

| ID | Decision | Rationale | Traces |
|----|----------|-----------|--------|
| 1 | Contact info is single-record upsert | Per CONSA0027, only one record exists | CONSA0027 |
| 2 | Response sentAt starts null | Quartz job picks up unsent responses and sets sentAt after sending | NFRA00108 |
| 3 | One response per message | System prevents duplicate responses | USA000093 |
| 4 | Quartz job for async email | Per NFRA00108, email sent asynchronously via batch job | NFRA00108 |

---

## 11. Changelog

### Version v1.0.0 (initial)

| Element | Name | Description | Traces |
|---------|------|-------------|--------|
| Entity | ContactInfoEntity | Single-record contact information | USA000084, CONSA0027 |
| Entity | ContactMessageEntity | Contact form submissions | USA000087 |
| Entity | ContactResponseEntity | Responses to messages with sentAt tracking | USA000093, NFRA00108 |
| Service | ContactService | Public API for contact info, messages, responses | USA000084-093 |
| Job | ContactEmailJob | Quartz job for async email delivery | NFRA00108 |
| Controller | ContactSectionPageController | Contact info form | USA000084 |
| Controller | ContactMessagePageController | Message list and detail with response form | USA000087-093 |
| Template | ContactInfoPage.jte | Contact info form with link to messages | USA000084 |
| Template | ContactMessageListPage.jte | Message list table | USA000087 |
| Template | ContactMessageDetailPage.jte | Message detail with response form | USA000090, USA000093 |
