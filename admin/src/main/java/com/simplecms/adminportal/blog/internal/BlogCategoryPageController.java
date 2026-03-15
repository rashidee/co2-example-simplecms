package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogCategoryDTO;
import com.simplecms.adminportal.blog.BlogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/blog/categories")
@PreAuthorize("hasRole('EDITOR')")
class BlogCategoryPageController {

    private final BlogService blogService;

    BlogCategoryPageController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    String listCategories(Model model) {
        model.addAttribute("view", BlogCategoryListView.of(blogService.listCategories()));
        return "blog/BlogCategoryListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", BlogCategoryFormView.forCreate());
        return "blog/BlogCategoryCreatePage";
    }

    @PostMapping("/create")
    String createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            blogService.createCategory(name, description);
            redirectAttributes.addFlashAttribute("successMessage", "Category created successfully.");
            return "redirect:/blog/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/categories/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        BlogCategoryDTO category = blogService.getCategoryById(id);
        model.addAttribute("view", BlogCategoryFormView.forEdit(category));
        return "blog/BlogCategoryEditPage";
    }

    @PostMapping("/{id}/edit")
    String updateCategory(
            @PathVariable("id") UUID id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            RedirectAttributes redirectAttributes) {
        try {
            blogService.updateCategory(id, name, description);
            redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully.");
            return "redirect:/blog/categories";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deleteCategory(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/blog/categories";
    }
}
