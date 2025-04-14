package com.template.spring_backend_template.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestResponse<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {

    public RestResponse(boolean success, String loginSuccessful, T data) {
        this(success, loginSuccessful, data, LocalDateTime.now());
    }
}
