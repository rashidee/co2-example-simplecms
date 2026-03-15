package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the reset password page.
 *
 * Traces: USA000006, NFRA00003
 */
@Controller
class ResetPasswordPageController {

    private final AuthService authService;

    ResetPasswordPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/reset-password")
    String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("view", ResetPasswordView.invalidToken());
        } else {
            model.addAttribute("view", ResetPasswordView.forToken(token));
        }
        return "authentication/ResetPasswordPage";
    }

    @PostMapping("/reset-password")
    String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("view", ResetPasswordView.withError(token, "Passwords do not match."));
            return "authentication/ResetPasswordPage";
        }

        try {
            authService.resetPassword(token, newPassword);
            model.addAttribute("view", ResetPasswordView.withSuccess(
                "Your password has been reset successfully. You can now log in with your new password."
            ));
        } catch (IllegalArgumentException e) {
            model.addAttribute("view", ResetPasswordView.withError(token, e.getMessage()));
        }

        return "authentication/ResetPasswordPage";
    }
}
