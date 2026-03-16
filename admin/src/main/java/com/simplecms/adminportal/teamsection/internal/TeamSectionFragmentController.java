package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team-section/fragments")
@PreAuthorize("hasRole('EDITOR')")
class TeamSectionFragmentController {

    private final TeamMemberService teamMemberService;

    TeamSectionFragmentController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("teamMembers", teamMemberService.list(pageable));
        return "teamsection/fragments/TeamMemberCardGrid";
    }
}
