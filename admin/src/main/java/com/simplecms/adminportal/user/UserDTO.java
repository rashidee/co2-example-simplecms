package com.simplecms.adminportal.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for user information.
 *
 * Traces: NFRA00006
 */
public record UserDTO(
    UUID id,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    UserStatus status,
    boolean forcePasswordChange,
    Instant lastLoginAt,
    Instant createdAt
) {}
