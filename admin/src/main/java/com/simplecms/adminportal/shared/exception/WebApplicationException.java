package com.simplecms.adminportal.shared.exception;

import org.springframework.http.HttpStatus;

public class WebApplicationException extends RuntimeException {

    private final HttpStatus status;

    public WebApplicationException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public WebApplicationException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static WebApplicationException notFound(String message) {
        return new WebApplicationException(HttpStatus.NOT_FOUND, message);
    }

    public static WebApplicationException badRequest(String message) {
        return new WebApplicationException(HttpStatus.BAD_REQUEST, message);
    }

    public static WebApplicationException forbidden(String message) {
        return new WebApplicationException(HttpStatus.FORBIDDEN, message);
    }

    public static WebApplicationException conflict(String message) {
        return new WebApplicationException(HttpStatus.CONFLICT, message);
    }
}
