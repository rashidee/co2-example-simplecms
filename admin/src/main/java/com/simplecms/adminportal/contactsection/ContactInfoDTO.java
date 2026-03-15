package com.simplecms.adminportal.contactsection;

import java.util.UUID;

/**
 * Traces: USA000084
 */
public record ContactInfoDTO(
    UUID id,
    String phoneNumber,
    String emailAddress,
    String physicalAddress,
    String linkedinUrl
) {}
