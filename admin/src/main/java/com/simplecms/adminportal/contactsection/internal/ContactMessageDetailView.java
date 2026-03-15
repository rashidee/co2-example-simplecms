package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import com.simplecms.adminportal.contactsection.ContactResponseDTO;

public record ContactMessageDetailView(
    ContactMessageDTO message,
    ContactResponseDTO response,
    boolean hasResponse,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ContactMessageDetailView of(ContactMessageDTO message, ContactResponseDTO response) {
        return new ContactMessageDetailView(message, response, response != null, null, false, null, false);
    }

    public static ContactMessageDetailView withSuccess(ContactMessageDTO message, ContactResponseDTO response, String msg) {
        return new ContactMessageDetailView(message, response, response != null, msg, true, null, false);
    }

    public static ContactMessageDetailView withError(ContactMessageDTO message, ContactResponseDTO response, String msg) {
        return new ContactMessageDetailView(message, response, response != null, null, false, msg, true);
    }
}
