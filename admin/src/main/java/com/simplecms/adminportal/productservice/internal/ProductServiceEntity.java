package com.simplecms.adminportal.productservice.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for product and service content.
 * Table: pns_product_service
 *
 * Traces: USA000036, CONSA0015
 */
@Entity
@Table(name = "pas_product_service")
@Getter
@Setter
public class ProductServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "image_path", length = 500)
    private String imagePath;

    @Column(name = "thumbnail_path", length = 500)
    private String thumbnailPath;

    @Column(name = "image_data", columnDefinition = "BYTEA")
    private byte[] imageData;

    @Column(name = "thumbnail_data", columnDefinition = "BYTEA")
    private byte[] thumbnailData;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "cta_url", length = 500)
    private String ctaUrl;

    @Column(name = "cta_text", length = 50)
    private String ctaText;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

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

    protected ProductServiceEntity() {
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
