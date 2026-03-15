package com.simplecms.adminportal.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    @GetMapping("/")
    String home(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .findFirst()
                .orElse("");

        return switch (role) {
            case "ADMIN" -> "redirect:/users";
            case "EDITOR" -> "redirect:/hero-section";
            default -> "redirect:/profile";
        };
    }
}
