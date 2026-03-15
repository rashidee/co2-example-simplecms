package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialService;
import com.simplecms.adminportal.testimonial.TestimonialStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testimonials/fragments")
@PreAuthorize("hasRole('EDITOR')")
class TestimonialFragmentController {

    private final TestimonialService testimonialService;

    TestimonialFragmentController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }

    @GetMapping("/card-grid")
    String cardGrid(
            @RequestParam(value = "status", required = false) TestimonialStatus status,
            @PageableDefault(size = 16) Pageable pageable,
            Model model) {
        model.addAttribute("testimonials", testimonialService.list(status, pageable));
        return "testimonial/fragments/TestimonialCardGrid";
    }
}
