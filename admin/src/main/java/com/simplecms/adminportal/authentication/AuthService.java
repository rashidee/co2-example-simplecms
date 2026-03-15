package com.simplecms.adminportal.authentication;

import java.util.UUID;

/**
 * Public API for the Authentication and Authorization module.
 * Provides authentication flows: login, logout, and password reset.
 *
 * Traces: USA000003, USA000006, USA000009, NFRA00003
 */
public interface AuthService {

    /**
     * Initiate a password reset flow by generating a token and sending
     * a reset link to the user's email address.
     * If the email does not exist, no error is raised (security best practice).
     *
     * Traces: USA000006, NFRA00003
     *
     * @param email the user's email address
     */
    void requestPasswordReset(String email);

    /**
     * Complete the password reset flow by validating the token and
     * updating the user's password.
     *
     * Traces: USA000006, NFRA00003
     *
     * @param token       the password reset token from the email link
     * @param newPassword the new plain-text password
     * @throws IllegalArgumentException if token is invalid, expired, or already used
     */
    void resetPassword(String token, String newPassword);
}
