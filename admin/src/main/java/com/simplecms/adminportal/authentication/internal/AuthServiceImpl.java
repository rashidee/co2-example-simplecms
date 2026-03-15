package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import com.simplecms.adminportal.user.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Implementation of AuthService.
 * Handles password reset token generation, validation, and password update.
 * Login/logout are handled by Spring Security configuration.
 *
 * Traces: USA000003, USA000006, USA000009, NFRA00003
 */
@Service
@Transactional
class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final int TOKEN_EXPIRY_HOURS = 24;

    private final PasswordResetTokenRepository tokenRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    AuthServiceImpl(PasswordResetTokenRepository tokenRepository,
                    UserService userService,
                    PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Generate a secure password reset token and send an email with the reset link.
     * If the email does not correspond to an active user, silently succeed
     * to prevent user enumeration attacks.
     *
     * Traces: USA000006, NFRA00003
     */
    @Override
    public void requestPasswordReset(String email) {
        log.info("Password reset requested for email: {}", email);

        UserDTO user;
        try {
            user = userService.findByEmail(email).orElse(null);
        } catch (Exception e) {
            log.warn("Error looking up user for password reset: {}", e.getMessage());
            return;
        }

        if (user == null || user.status() != UserStatus.ACTIVE) {
            log.info("No active user found for email: {}. Silently ignoring.", email);
            return;
        }

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(TOKEN_EXPIRY_HOURS, ChronoUnit.HOURS);

        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity(
            user.id(), token, expiresAt, "SYSTEM"
        );

        tokenRepository.save(resetToken);

        // TODO: Send email with reset link containing the token
        log.info("Password reset token generated for user: {}", user.id());
    }

    /**
     * Validate the reset token and update the user's password.
     * Marks the token as used after successful password reset.
     *
     * Traces: USA000006, NFRA00003
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        log.info("Password reset attempt with token");

        PasswordResetTokenEntity resetToken = tokenRepository.findByTokenAndUsedFalse(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Password reset token has expired");
        }

        if (!resetToken.isValid()) {
            throw new IllegalArgumentException("Password reset token is no longer valid");
        }

        // Update user's password
        userService.changePassword(resetToken.getUserId(), passwordEncoder.encode(newPassword));

        resetToken.markAsUsed("SYSTEM");
        tokenRepository.save(resetToken);

        log.info("Password reset completed for user: {}", resetToken.getUserId());
    }
}
