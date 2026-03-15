package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;

public record TeamMemberFormView(
    TeamMemberDTO teamMember,
    boolean isEdit,
    TeamMemberStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static TeamMemberFormView forCreate() {
        return new TeamMemberFormView(null, false, TeamMemberStatus.values(), null, false);
    }

    public static TeamMemberFormView forEdit(TeamMemberDTO teamMember) {
        return new TeamMemberFormView(teamMember, true, TeamMemberStatus.values(), null, false);
    }

    public static TeamMemberFormView withError(TeamMemberDTO teamMember, boolean isEdit, String message) {
        return new TeamMemberFormView(teamMember, isEdit, TeamMemberStatus.values(), message, true);
    }
}
