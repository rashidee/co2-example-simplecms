package com.simplecms.adminportal.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Public API for the User module.
 * Provides user management, profile, and account operations.
 *
 * Traces: USA000012-027, NFRA00006-015, CONSA0003, CONSA0006, CONSA0009
 */
public interface UserService {

    /**
     * Get the profile of a user by ID.
     *
     * Traces: USA000012
     *
     * @param userId the user's ID
     * @return the user profile DTO
     */
    UserDTO getProfile(UUID userId);

    /**
     * Update the profile of the currently authenticated user.
     * Only firstName and lastName are editable per CONSA0003.
     *
     * Traces: USA000012, CONSA0003
     *
     * @param userId    the authenticated user's ID
     * @param firstName new first name
     * @param lastName  new last name
     * @return updated user DTO
     */
    UserDTO updateProfile(UUID userId, String firstName, String lastName);

    /**
     * Change the password for a user.
     *
     * Traces: USA000015
     *
     * @param userId      the user's ID
     * @param newPassword the new encoded password
     */
    void changePassword(UUID userId, String newPassword);

    /**
     * List all users with pagination.
     *
     * Traces: USA000018, CONSA0009
     *
     * @param pageable pagination parameters
     * @return page of user DTOs
     */
    Page<UserDTO> listUsers(Pageable pageable);

    /**
     * List all users (no pagination).
     *
     * Traces: USA000018
     *
     * @return list of all user DTOs
     */
    List<UserDTO> listUsers();

    /**
     * Create a new user with default password "password" and forcePasswordChange=true.
     *
     * Traces: USA000021, NFRA00006, NFRA00009, NFRA00012
     *
     * @param email     the user's email (unique)
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param role      the user's role
     * @param status    the user's status
     * @return created user DTO
     */
    UserDTO createUser(String email, String firstName, String lastName, UserRole role, UserStatus status);

    /**
     * Update an existing user.
     *
     * Traces: USA000024
     *
     * @param userId    the user's ID
     * @param email     the user's email
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param role      the user's role
     * @param status    the user's status
     * @return updated user DTO
     */
    UserDTO updateUser(UUID userId, String email, String firstName, String lastName, UserRole role, UserStatus status);

    /**
     * Delete a user by ID.
     *
     * Traces: USA000027
     *
     * @param userId the user's ID
     */
    void deleteUser(UUID userId);

    /**
     * Find a user by email address.
     *
     * @param email the email address
     * @return optional user DTO
     */
    Optional<UserDTO> findByEmail(String email);

    /**
     * List all users with EDITOR role (for blog author selection).
     *
     * Traces: CONSA0039 (cross-module: Blog)
     *
     * @return list of editor user DTOs
     */
    List<UserDTO> listEditors();
}
