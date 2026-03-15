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
@Table(name = "cts_contact_info")
@Getter
@Setter
public class ContactInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "email_address", nullable = false, length = 255)
    private String emailAddress;

    @Column(name = "physical_address", nullable = false, length = 500)
    private String physicalAddress;

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
