package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostDTO;
import com.simplecms.adminportal.blog.BlogPostStatus;
import com.simplecms.adminportal.blog.BlogService;
import com.simplecms.adminportal.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
@PreAuthorize("hasRole('EDITOR')")
class BlogPostPageController {

    private final BlogService blogService;
    private final UserService userService;

    BlogPostPageController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping
    String listPosts(
            @RequestParam(value = "status", required = false) BlogPostStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDateTime effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate).atStartOfDay() : null;
        LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate).atStartOfDay() : null;

        Page<BlogPostDTO> posts = blogService.listPosts(status, effDate, expDate, pageable);
        model.addAttribute("view", BlogPostListView.of(posts, status, effectiveDate, expirationDate));
        return "blog/BlogPostListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", BlogPostFormView.forCreate(
            blogService.listCategories(), userService.listEditors()));
        return "blog/BlogPostCreatePage";
    }

    @PostMapping("/create")
    String createPost(
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("authorId") UUID authorId,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("content") String content,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") BlogPostStatus status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime effDate = LocalDate.parse(effectiveDate).atStartOfDay();
            LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate).atStartOfDay() : null;

            blogService.createPost(categoryId, authorId, title, summary, content,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post created successfully.");
            return "redirect:/blog";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        BlogPostDTO post = blogService.getPostById(id);
        model.addAttribute("view", BlogPostFormView.forEdit(post,
            blogService.listCategories(), userService.listEditors()));
        return "blog/BlogPostEditPage";
    }

    @PostMapping("/{id}/edit")
    String updatePost(
            @PathVariable("id") UUID id,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("authorId") UUID authorId,
            @RequestParam("title") String title,
            @RequestParam("summary") String summary,
            @RequestParam("content") String content,
            @RequestParam("effectiveDate") String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @RequestParam("status") BlogPostStatus status,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime effDate = LocalDate.parse(effectiveDate).atStartOfDay();
            LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
                ? LocalDate.parse(expirationDate).atStartOfDay() : null;

            blogService.updatePost(id, categoryId, authorId, title, summary, content,
                effDate, expDate, status, image);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post updated successfully.");
            return "redirect:/blog";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/blog/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String deletePost(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            blogService.deletePost(id);
            redirectAttributes.addFlashAttribute("successMessage", "Blog post deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/blog";
    }
}
