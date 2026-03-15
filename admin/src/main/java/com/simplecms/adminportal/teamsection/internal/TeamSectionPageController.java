package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import com.simplecms.adminportal.teamsection.TeamMemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/team-section")
@PreAuthorize("hasRole('EDITOR')")
class TeamSectionPageController {

    private final TeamMemberService teamMemberService;

    TeamSectionPageController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) TeamMemberStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<TeamMemberDTO> members = teamMemberService.list(status, pageable);
        model.addAttribute("view", TeamMemberListView.of(members, status));
        return "teamsection/TeamMemberListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", TeamMemberFormView.forCreate());
        return "teamsection/TeamMemberCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("name") String name,
            @RequestParam("role") String role,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TeamMemberStatus status,
            @RequestParam("profilePicture") MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.create(name, role, linkedinUrl, displayOrder, status, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Team member created successfully.");
            return "redirect:/team-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/team-section/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        TeamMemberDTO member = teamMemberService.getById(id);
        model.addAttribute("view", TeamMemberFormView.forEdit(member));
        return "teamsection/TeamMemberEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("name") String name,
            @RequestParam("role") String role,
            @RequestParam(value = "linkedinUrl", required = false) String linkedinUrl,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TeamMemberStatus status,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.update(id, name, role, linkedinUrl, displayOrder, status, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Team member updated successfully.");
            return "redirect:/team-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/team-section/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Team member deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/team-section";
    }
}
