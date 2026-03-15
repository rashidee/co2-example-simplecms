package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import com.simplecms.adminportal.testimonial.TestimonialStatus;
import org.springframework.data.domain.Page;

public record TestimonialListView(
    Page<TestimonialDTO> testimonials,
    TestimonialStatus filterStatus,
    TestimonialStatus[] statuses,
    String successMessage,
    boolean hasSuccess
) {
    public static TestimonialListView of(Page<TestimonialDTO> testimonials, TestimonialStatus filterStatus) {
        return new TestimonialListView(testimonials, filterStatus, TestimonialStatus.values(), null, false);
    }
}
