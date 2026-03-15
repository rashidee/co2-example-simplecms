package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import org.springframework.data.domain.Page;

/**
 * View model for the user list page.
 *
 * Traces: USA000018
 */
public record UserListView(
    Page<UserDTO> users,
    String successMessage,
    boolean hasSuccess
) {
    public static UserListView of(Page<UserDTO> users) {
        return new UserListView(users, null, false);
    }

    public static UserListView withSuccess(Page<UserDTO> users, String message) {
        return new UserListView(users, message, true);
    }
}
