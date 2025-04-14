package com.template.spring_backend_template.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestErrorResponse(
        boolean success,
        String error,
        String message,
        List<String> errors,
        LocalDateTime timestamp
) {
    public RestErrorResponse(boolean success, String error, String message, List<String> errors) {
        this(success, error, message, errors, LocalDateTime.now());
    }

    public RestErrorResponse(boolean success, String error, String message) {
        this(success, error, message, null, LocalDateTime.now());
    }
}
