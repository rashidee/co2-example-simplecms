package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;

public record TeamMemberFormView(
    TeamMemberDTO teamMember,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static TeamMemberFormView forCreate() {
        return new TeamMemberFormView(null, false, null, false);
    }

    public static TeamMemberFormView forEdit(TeamMemberDTO teamMember) {
        return new TeamMemberFormView(teamMember, true, null, false);
    }

    public static TeamMemberFormView withError(TeamMemberDTO teamMember, boolean isEdit, String message) {
        return new TeamMemberFormView(teamMember, isEdit, message, true);
    }
}
