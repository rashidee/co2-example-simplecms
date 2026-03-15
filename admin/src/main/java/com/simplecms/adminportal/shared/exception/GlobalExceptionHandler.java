package com.simplecms.adminportal.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebApplicationException.class)
    public String handleWebApplicationException(WebApplicationException ex, Model model) {
        log.warn("WebApplicationException: {} - {}", ex.getStatus(), ex.getMessage());
        model.addAttribute("status", ex.getStatus().value());
        model.addAttribute("error", ex.getStatus().getReasonPhrase());
        model.addAttribute("message", ex.getMessage());

        return switch (ex.getStatus()) {
            case FORBIDDEN -> "error/403";
            case NOT_FOUND -> "error/404";
            default -> "error/500";
        };
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unhandled exception", ex);
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred. Please try again later.");
        return "error/500";
    }
}
