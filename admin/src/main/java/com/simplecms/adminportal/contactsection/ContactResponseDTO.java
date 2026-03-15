package com.simplecms.adminportal.contactsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000093
 */
public record ContactResponseDTO(
    UUID id,
    UUID contactMessageId,
    String responderName,
    String responderEmail,
    String responseContent,
    Instant sentAt,
    Instant createdAt
) {}
