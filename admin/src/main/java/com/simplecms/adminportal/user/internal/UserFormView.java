package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserStatus;

import java.util.Arrays;

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
    private static final UserRole[] ASSIGNABLE_ROLES = Arrays.stream(UserRole.values())
            .filter(r -> r != UserRole.USER)
            .toArray(UserRole[]::new);

    public static UserFormView forCreate() {
        return new UserFormView(null, false, ASSIGNABLE_ROLES, UserStatus.values(), null, false);
    }

    public static UserFormView forEdit(UserDTO user) {
        return new UserFormView(user, true, ASSIGNABLE_ROLES, UserStatus.values(), null, false);
    }

    public static UserFormView withError(UserDTO user, boolean isEdit, String message) {
        return new UserFormView(user, isEdit, ASSIGNABLE_ROLES, UserStatus.values(), message, true);
    }
}
