package com.simplecms.adminportal.user;

import java.util.UUID;

/**
 * Domain event published when a user is updated.
 *
 * Traces: USA000024
 */
public record UserUpdatedEvent(UUID userId, String email) {}
