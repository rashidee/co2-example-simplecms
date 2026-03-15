package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;

/**
 * View model for the profile page.
 *
 * Traces: USA000012, CONSA0003
 */
public record ProfileView(
    UserDTO user,
    String successMessage,
    boolean hasSuccess,
    String errorMessage,
    boolean hasError
) {
    public static ProfileView of(UserDTO user) {
        return new ProfileView(user, null, false, null, false);
    }

    public static ProfileView withSuccess(UserDTO user, String message) {
        return new ProfileView(user, message, true, null, false);
    }

    public static ProfileView withError(UserDTO user, String message) {
        return new ProfileView(user, null, false, message, true);
    }
}
