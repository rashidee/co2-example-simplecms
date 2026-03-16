package com.simplecms.adminportal.testimonial.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for testimonials.
 * Table: tst_testimonial
 *
 * Traces: USA000060, CONSA0021
 */
@Entity
@Table(name = "tst_testimonial")
@Getter
@Setter
public class TestimonialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_review", nullable = false, length = 1000)
    private String customerReview;

    @Column(name = "customer_rating", nullable = false)
    private int customerRating;

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

    protected TestimonialEntity() {
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
