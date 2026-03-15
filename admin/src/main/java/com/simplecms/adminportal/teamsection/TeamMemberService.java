package com.simplecms.adminportal.teamsection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Public API for the Team Section module.
 *
 * Traces: USA000072-081, NFRA00081-093, CONSA0024
 */
public interface TeamMemberService {

    /**
     * List team members with optional status filter,
     * ordered by displayOrder ASC, then createdAt ASC.
     *
     * Traces: USA000081, NFRA00093
     */
    Page<TeamMemberDTO> list(TeamMemberStatus status, Pageable pageable);

    /**
     * Get a team member by ID.
     */
    TeamMemberDTO getById(UUID id);

    /**
     * Create a new team member with profile picture upload.
     * Validates image dimensions (400x400 square).
     *
     * Traces: USA000072, USA000075, NFRA00081
     */
    TeamMemberDTO create(String name, String role, String linkedinUrl,
                         int displayOrder, TeamMemberStatus status, MultipartFile profilePicture);

    /**
     * Update an existing team member. Profile picture is optional on update.
     *
     * Traces: USA000072, USA000075
     */
    TeamMemberDTO update(UUID id, String name, String role, String linkedinUrl,
                         int displayOrder, TeamMemberStatus status, MultipartFile profilePicture);

    /**
     * Delete a team member by ID.
     *
     * Traces: USA000078
     */
    void delete(UUID id);
}
