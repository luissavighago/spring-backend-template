package com.template.spring_backend_template.domain.auth.response;

public record AuthResponse (String token) {
    public AuthResponse(String token) {
        this.token = token;
    }
}
