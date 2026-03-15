package com.simplecms.adminportal.contactsection.internal;

import com.simplecms.adminportal.contactsection.ContactInfoDTO;

public record ContactInfoView(
    ContactInfoDTO contactInfo,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ContactInfoView of(ContactInfoDTO info) {
        return new ContactInfoView(info, null, false, null, false);
    }

    public static ContactInfoView withSuccess(ContactInfoDTO info, String message) {
        return new ContactInfoView(info, message, true, null, false);
    }

    public static ContactInfoView withError(ContactInfoDTO info, String message) {
        return new ContactInfoView(info, null, false, message, true);
    }
}
