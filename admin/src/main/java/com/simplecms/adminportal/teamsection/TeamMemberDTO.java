package com.simplecms.adminportal.teamsection;

import java.time.Instant;
import java.util.UUID;

/**
 * Data transfer object for team member content.
 * v1.0.4: Added hasImageData flag; images now served via BLOB endpoint.
 *
 * Traces: USA000072
 */
public record TeamMemberDTO(
    UUID id,
    String profilePicturePath,
    boolean hasImageData,
    String name,
    String role,
    String linkedinUrl,
    int displayOrder,
    Instant createdAt
) {
    /**
     * Returns the URL for the profile picture image.
     * v1.0.4: Prefers BLOB endpoint if image data exists.
     */
    public String imageUrl() {
        if (hasImageData) {
            return "/team-section/" + id + "/image";
        }
        return profilePicturePath;
    }
}
