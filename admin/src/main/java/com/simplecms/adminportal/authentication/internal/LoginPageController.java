package com.simplecms.adminportal.authentication.internal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the login page.
 * Spring Security handles POST /login for actual authentication.
 *
 * Traces: USA000003
 */
@Controller
class LoginPageController {

    @GetMapping("/login")
    String showLoginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        LoginView view;
        if (error != null) {
            view = LoginView.withError("Invalid email or password. Please try again.");
        } else if (logout != null) {
            view = LoginView.withLogoutMessage("You have been logged out successfully.");
        } else {
            view = LoginView.empty();
        }

        model.addAttribute("view", view);
        return "authentication/LoginPage";
    }
}
