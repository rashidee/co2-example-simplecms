package com.simplecms.adminportal.contactsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Traces: USA000087, USA000090
 */
public record ContactMessageDTO(
    UUID id,
    String senderName,
    String senderEmail,
    String messageContent,
    Instant submittedAt,
    boolean hasResponse
) {}
