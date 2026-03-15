package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactMessageDTO;
import org.springframework.data.domain.Page;

public record ContactMessageListView(
    Page<ContactMessageDTO> messages,
    String successMessage,
    boolean hasSuccess
) {
    public static ContactMessageListView of(Page<ContactMessageDTO> messages) {
        return new ContactMessageListView(messages, null, false);
    }
}
