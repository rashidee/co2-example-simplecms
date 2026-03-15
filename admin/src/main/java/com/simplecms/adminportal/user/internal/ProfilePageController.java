package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for profile pages.
 *
 * Traces: USA000012, CONSA0003
 */
@Controller
class ProfilePageController {

    private final UserService userService;

    ProfilePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        model.addAttribute("view", ProfileView.of(user));
        return "user/ProfilePage";
    }

    @PostMapping("/profile")
    String updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            Model model) {

        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        try {
            UserDTO updated = userService.updateProfile(user.id(), firstName, lastName);
            model.addAttribute("view", ProfileView.withSuccess(updated, "Profile updated successfully."));
        } catch (Exception e) {
            model.addAttribute("view", ProfileView.withError(user, e.getMessage()));
        }

        return "user/ProfilePage";
    }
}
