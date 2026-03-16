package com.simplecms.adminportal.teamsection.internal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for team members.
 * Table: tms_team_member
 *
 * Traces: USA000072, CONSA0024
 */
@Entity
@Table(name = "tms_team_member")
@Getter
@Setter
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "profile_picture_path", length = 500)
    private String profilePicturePath;

    @Column(name = "profile_picture_data", columnDefinition = "BYTEA")
    private byte[] profilePictureData;

    @Column(name = "thumbnail_data", columnDefinition = "BYTEA")
    private byte[] thumbnailData;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

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

    protected TeamMemberEntity() {
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
