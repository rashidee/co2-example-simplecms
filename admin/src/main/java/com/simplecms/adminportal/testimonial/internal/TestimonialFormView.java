package com.simplecms.adminportal.testimonial.internal;

import com.simplecms.adminportal.testimonial.TestimonialDTO;

public record TestimonialFormView(
    TestimonialDTO testimonial,
    boolean isEdit,
    String errorMessage,
    boolean hasError
) {
    public static TestimonialFormView forCreate() {
        return new TestimonialFormView(null, false, null, false);
    }

    public static TestimonialFormView forEdit(TestimonialDTO testimonial) {
        return new TestimonialFormView(testimonial, true, null, false);
    }

    public static TestimonialFormView withError(TestimonialDTO testimonial, boolean isEdit, String message) {
        return new TestimonialFormView(testimonial, isEdit, message, true);
    }
}
