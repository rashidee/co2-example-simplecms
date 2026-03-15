package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;

/**
 * View model for user create/edit forms.
 *
 * Traces: USA000021, USA000024
 */
public record UserFormView(
    UserDTO user,
    boolean isEdit,
    UserRole[] roles,
    UserStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static UserFormView forCreate() {
        return new UserFormView(null, false, UserRole.values(), UserStatus.values(), null, false);
    }

    public static UserFormView forEdit(UserDTO user) {
        return new UserFormView(user, true, UserRole.values(), UserStatus.values(), null, false);
    }

    public static UserFormView withError(UserDTO user, boolean isEdit, String message) {
        return new UserFormView(user, isEdit, UserRole.values(), UserStatus.values(), message, true);
    }
}
