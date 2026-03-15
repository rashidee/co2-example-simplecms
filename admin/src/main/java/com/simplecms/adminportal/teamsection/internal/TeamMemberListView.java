package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;

public record TeamMemberListView(
    Page<TeamMemberDTO> teamMembers,
    TeamMemberStatus filterStatus,
    TeamMemberStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static TeamMemberListView of(Page<TeamMemberDTO> teamMembers, TeamMemberStatus filterStatus) {
        return new TeamMemberListView(teamMembers, filterStatus, TeamMemberStatus.values(), null, false);
    }
}
