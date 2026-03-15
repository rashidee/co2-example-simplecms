package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;
import com.simplecms.adminportal.testimonial.TestimonialStatus;

public record TestimonialFormView(
    TestimonialDTO testimonial,
    boolean isEdit,
    TestimonialStatus[] statuses,
    String errorMessage,
    boolean hasError
) {
    public static TestimonialFormView forCreate() {
        return new TestimonialFormView(null, false, TestimonialStatus.values(), null, false);
    }

    public static TestimonialFormView forEdit(TestimonialDTO testimonial) {
        return new TestimonialFormView(testimonial, true, TestimonialStatus.values(), null, false);
    }

    public static TestimonialFormView withError(TestimonialDTO testimonial, boolean isEdit, String message) {
        return new TestimonialFormView(testimonial, isEdit, TestimonialStatus.values(), message, true);
    }
}
