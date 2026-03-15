package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the forgot password page.
 *
 * Traces: USA000006
 */
public record ForgotPasswordView(
    String email,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ForgotPasswordView empty() {
        return new ForgotPasswordView("", null, false, null, false);
    }

    public static ForgotPasswordView withSuccess(String successMessage) {
        return new ForgotPasswordView("", successMessage, true, null, false);
    }

    public static ForgotPasswordView withError(String errorMessage) {
        return new ForgotPasswordView("", null, false, errorMessage, true);
    }
}
