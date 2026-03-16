package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import org.springframework.data.domain.Page;

public record TeamMemberListView(
    Page<TeamMemberDTO> teamMembers,
    String successMessage,
    boolean hasSuccess
) {
    public static TeamMemberListView of(Page<TeamMemberDTO> teamMembers) {
        return new TeamMemberListView(teamMembers, null, false);
    }
}
