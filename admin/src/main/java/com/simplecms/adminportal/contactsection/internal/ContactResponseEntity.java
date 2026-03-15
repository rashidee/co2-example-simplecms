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
@Table(name = "cts_contact_response")
@Getter
@Setter
public class ContactResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "contact_message_id", nullable = false)
    private UUID contactMessageId;

    @Column(name = "responder_name", nullable = false, length = 100)
    private String responderName;

    @Column(name = "responder_email", nullable = false, length = 255)
    private String responderEmail;

    @Column(name = "response_content", nullable = false, columnDefinition = "TEXT")
    private String responseContent;

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
