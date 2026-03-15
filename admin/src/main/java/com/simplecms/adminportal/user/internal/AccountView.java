package com.simplecms.adminportal.user.internal;

/**
 * View model for the account / change password page.
 *
 * Traces: USA000015
 */
public record AccountView(
    String email,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static AccountView of(String email) {
        return new AccountView(email, null, false, null, false);
    }

    public static AccountView withSuccess(String email, String message) {
        return new AccountView(email, message, true, null, false);
    }

    public static AccountView withError(String email, String message) {
        return new AccountView(email, null, false, message, true);
    }
}
