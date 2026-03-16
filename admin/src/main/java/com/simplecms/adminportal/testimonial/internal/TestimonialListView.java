package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import org.springframework.data.domain.Page;

public record TestimonialListView(
    Page<TestimonialDTO> testimonials,
    String successMessage,
    boolean hasSuccess
) {
    public static TestimonialListView of(Page<TestimonialDTO> testimonials) {
        return new TestimonialListView(testimonials, null, false);
    }
}
