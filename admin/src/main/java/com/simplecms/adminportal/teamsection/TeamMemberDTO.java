package com.simplecms.adminportal.teamsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000072
 */
public record TeamMemberDTO(
    UUID id,
    String profilePicturePath,
    String name,
    String role,
    String linkedinUrl,
    int displayOrder,
    TeamMemberStatus status,
    Instant createdAt
) {}
