package com.simplecms.adminportal.authentication.internal;

import com.simplecms.adminportal.authentication.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the forgot password page.
 *
 * Traces: USA000006, NFRA00003
 */
@Controller
class ForgotPasswordPageController {

    private final AuthService authService;

    ForgotPasswordPageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/forgot-password")
    String showForgotPasswordPage(Model model) {
        model.addAttribute("view", ForgotPasswordView.empty());
        return "authentication/ForgotPasswordPage";
    }

    @PostMapping("/forgot-password")
    String processForgotPassword(@RequestParam("email") String email, Model model) {
        authService.requestPasswordReset(email);
        model.addAttribute("view", ForgotPasswordView.withSuccess(
            "If an account with that email exists, a password reset link has been sent."
        ));
        return "authentication/ForgotPasswordPage";
    }
}
