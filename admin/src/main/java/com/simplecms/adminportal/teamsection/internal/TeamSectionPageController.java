package com.simplecms.adminportal.teamsection.internal;

import com.simplecms.adminportal.teamsection.TeamMemberDTO;
import com.simplecms.adminportal.teamsection.TeamMemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<TeamMemberDTO> members = teamMemberService.list(pageable);
        model.addAttribute("view", TeamMemberListView.of(members));
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
            @RequestParam("profilePicture") MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.create(name, role, linkedinUrl, displayOrder, profilePicture);
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
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            RedirectAttributes redirectAttributes) {
        try {
            teamMemberService.update(id, name, role, linkedinUrl, displayOrder, profilePicture);
            redirectAttributes.addFlashAttribute("successMessage", "Team member updated successfully.");
            return "redirect:/team-section";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/team-section/" + id + "/edit";
        }
    }

    /**
     * v1.0.4: Serve profile picture image from BLOB.
     */
    @GetMapping("/{id}/image")
    ResponseEntity<byte[]> serveImage(@PathVariable("id") UUID id) {
        byte[] data = teamMemberService.getImageData(id);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .body(data);
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
