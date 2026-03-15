package com.simplecms.adminportal.user.internal;

import com.simplecms.adminportal.user.UserDTO;
import com.simplecms.adminportal.user.UserRole;
import com.simplecms.adminportal.user.UserService;
import com.simplecms.adminportal.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

/**
 * Controller for user management pages (ADMIN only).
 *
 * Traces: USA000018-027, CONSA0009
 */
@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
class UserPageController {

    private final UserService userService;

    UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String listUsers(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<UserDTO> users = userService.listUsers(pageable);
        model.addAttribute("view", UserListView.of(users));
        return "user/UserListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", UserFormView.forCreate());
        return "user/UserCreatePage";
    }

    @PostMapping("/create")
    String createUser(
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") UserRole role,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(email, firstName, lastName, role, status);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        UserDTO user = userService.getProfile(id);
        model.addAttribute("view", UserFormView.forEdit(user));
        return "user/UserEditPage";
    }

    @PostMapping("/{id}/edit")
    String updateUser(
            @PathVariable("id") UUID id,
            @RequestParam("email") String email,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") UserRole role,
            @RequestParam("status") UserStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            userService.updateUser(id, email, firstName, lastName, role, status);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deleteUser(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
