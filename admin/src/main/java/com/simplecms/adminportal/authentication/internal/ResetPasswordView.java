package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the reset password page.
 *
 * Traces: USA000006
 */
public record ResetPasswordView(
    String token,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError,
    boolean tokenValid
) {
    public static ResetPasswordView forToken(String token) {
        return new ResetPasswordView(token, null, false, null, false, true);
    }

    public static ResetPasswordView withSuccess(String successMessage) {
        return new ResetPasswordView("", successMessage, true, null, false, false);
    }

    public static ResetPasswordView withError(String token, String errorMessage) {
        return new ResetPasswordView(token, null, false, errorMessage, true, true);
    }

    public static ResetPasswordView invalidToken() {
        return new ResetPasswordView("", null, false, "Invalid or expired reset link.", true, false);
    }
}
