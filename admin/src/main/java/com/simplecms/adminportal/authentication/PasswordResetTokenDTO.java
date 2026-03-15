package com.simplecms.adminportal.authentication;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for password reset token information.
 *
 * Traces: USA000006
 */
public record PasswordResetTokenDTO(
    UUID id,
    UUID userId,
    String token,
    Instant expiresAt,
    boolean used,
    Instant createdAt
) {}
