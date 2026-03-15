package com.simplecms.adminportal.contactsection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Public API for the Contact Section module.
 *
 * Traces: USA000084-093, NFRA00096-108, CONSA0027
 */
public interface ContactService {

    /**
     * Get the current contact information.
     * Returns null if not yet configured.
     *
     * Traces: USA000084
     */
    ContactInfoDTO getContactInfo();

    /**
     * Create or update contact information (single record upsert).
     *
     * Traces: USA000084, CONSA0027
     */
    ContactInfoDTO updateContactInfo(String phoneNumber, String emailAddress,
                                     String physicalAddress, String linkedinUrl);

    /**
     * List submitted contact messages with pagination.
     *
     * Traces: USA000087
     */
    Page<ContactMessageDTO> listMessages(Pageable pageable);

    /**
     * Get a contact message by ID including its response if any.
     *
     * Traces: USA000090
     */
    ContactMessageDTO getMessageById(UUID id);

    /**
     * Get the response for a message if one exists.
     *
     * Traces: USA000090
     */
    ContactResponseDTO getResponseByMessageId(UUID messageId);

    /**
     * Submit a response to a contact message.
     * The response is saved with sentAt=null and picked up by the
     * async Quartz email job.
     *
     * Traces: USA000093, NFRA00108
     */
    ContactResponseDTO submitResponse(UUID messageId, String responderName,
                                      String responderEmail, String responseContent);
}
