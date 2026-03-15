package com.simplecms.adminportal.user;

import java.util.UUID;

/**
 * Domain event published when a new user is created.
 *
 * Traces: USA000021
 */
public record UserCreatedEvent(UUID userId, String email, UserRole role) {}
