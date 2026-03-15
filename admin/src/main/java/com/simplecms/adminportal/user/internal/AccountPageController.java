package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the account / change password page.
 *
 * Traces: USA000015
 */
@Controller
class AccountPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    AccountPageController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account")
    String showAccount(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("view", AccountView.of(userDetails.getUsername()));
        return "user/AccountPage";
    }

    @PostMapping("/account")
    String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("view", AccountView.withError(
                userDetails.getUsername(), "Passwords do not match."));
            return "user/AccountPage";
        }

        UserDTO user = userService.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        userService.changePassword(user.id(), passwordEncoder.encode(newPassword));

        model.addAttribute("view", AccountView.withSuccess(
            userDetails.getUsername(), "Password changed successfully."));
        return "user/AccountPage";
    }
}
