package com.simplecms.adminportal.user;

import java.util.UUID;

/**
 * Domain event published when a user is deleted.
 *
 * Traces: USA000027
 */
public record UserDeletedEvent(UUID userId, String email) {}
