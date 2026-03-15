package com.simplecms.adminportal.blog.internal;

import com.simplecms.adminportal.blog.BlogPostStatus;
import com.simplecms.adminportal.blog.BlogService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/blog/fragments")
@PreAuthorize("hasRole('EDITOR')")
class BlogPostFragmentController {

    private final BlogService blogService;

    BlogPostFragmentController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) BlogPostStatus status,
            @RequestParam(value = "effectiveDate", required = false) String effectiveDate,
            @RequestParam(value = "expirationDate", required = false) String expirationDate,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        LocalDateTime effDate = effectiveDate != null && !effectiveDate.isBlank()
            ? LocalDate.parse(effectiveDate).atStartOfDay() : null;
        LocalDateTime expDate = expirationDate != null && !expirationDate.isBlank()
            ? LocalDate.parse(expirationDate).atStartOfDay() : null;

        model.addAttribute("posts", blogService.listPosts(status, effDate, expDate, pageable));
        return "blog/fragments/BlogPostCardGrid";
    }
}
