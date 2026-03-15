package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import com.simplecms.adminportal.testimonial.TestimonialService;
import com.simplecms.adminportal.testimonial.TestimonialStatus;
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
 * Traces: USA000060-069
 */
@Controller
@RequestMapping("/testimonials")
@PreAuthorize("hasRole('EDITOR')")
class TestimonialPageController {

    private final TestimonialService testimonialService;

    TestimonialPageController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping
    String list(
            @RequestParam(value = "status", required = false) TestimonialStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        Page<TestimonialDTO> testimonials = testimonialService.list(status, pageable);
        model.addAttribute("view", TestimonialListView.of(testimonials, status));
        return "testimonial/TestimonialListPage";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        model.addAttribute("view", TestimonialFormView.forCreate());
        return "testimonial/TestimonialCreatePage";
    }

    @PostMapping("/create")
    String create(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerReview") String customerReview,
            @RequestParam("customerRating") int customerRating,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TestimonialStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            testimonialService.create(customerName, customerReview, customerRating, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial created successfully.");
            return "redirect:/testimonials";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/testimonials/create";
        }
    }

    @GetMapping("/{id}/edit")
    String showEditForm(@PathVariable("id") UUID id, Model model) {
        TestimonialDTO testimonial = testimonialService.getById(id);
        model.addAttribute("view", TestimonialFormView.forEdit(testimonial));
        return "testimonial/TestimonialEditPage";
    }

    @PostMapping("/{id}/edit")
    String update(
            @PathVariable("id") UUID id,
            @RequestParam("customerName") String customerName,
            @RequestParam("customerReview") String customerReview,
            @RequestParam("customerRating") int customerRating,
            @RequestParam("displayOrder") int displayOrder,
            @RequestParam("status") TestimonialStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            testimonialService.update(id, customerName, customerReview, customerRating, displayOrder, status);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial updated successfully.");
            return "redirect:/testimonials";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/testimonials/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            testimonialService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Testimonial deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/testimonials";
    }
}
