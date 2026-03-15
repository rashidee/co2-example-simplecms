package com.simplecms.adminportal.authentication.internal;

/**
 * View model for the login page.
 *
 * Traces: USA000003
 */
public record LoginView(
    String email,
    String errorMessage,
    boolean hasError,
    String logoutMessage,
    boolean hasLogoutMessage
) {
    public static LoginView empty() {
        return new LoginView("", null, false, null, false);
    }

    public static LoginView withError(String errorMessage) {
        return new LoginView("", errorMessage, true, null, false);
    }

    public static LoginView withLogoutMessage(String logoutMessage) {
        return new LoginView("", null, false, logoutMessage, true);
    }
}
