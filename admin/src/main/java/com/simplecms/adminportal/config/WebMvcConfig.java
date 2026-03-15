package com.simplecms.adminportal.config;

import com.simplecms.adminportal.shared.vite.ViteManifestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class WebMvcConfig {

    private final ViteManifestService viteManifestService;

    public WebMvcConfig(ViteManifestService viteManifestService) {
        this.viteManifestService = viteManifestService;
    }

    @ModelAttribute("vite")
    public ViteManifestService vite() {
        return viteManifestService;
    }

    @ModelAttribute("userRole")
    public String userRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> a.startsWith("ROLE_"))
                    .map(a -> a.substring(5))
                    .findFirst()
                    .orElse("");
        }
        return "";
    }

    @ModelAttribute("userName")
    public String userName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return "";
    }

    @ModelAttribute("userInitials")
    public String userInitials() {
        String name = userName();
        if (name.isEmpty()) return "";
        // Use first letter of email as initials
        return name.substring(0, 1).toUpperCase();
    }

    @ModelAttribute("cspNonce")
    public String cspNonce(HttpServletRequest request) {
        Object nonce = request.getAttribute("cspNonce");
        return nonce != null ? nonce.toString() : "";
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
