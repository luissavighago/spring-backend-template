package com.template.spring_backend_template.domain.auth;

public record RegisterRequest(
        String name,
        String lastname,
        String email,
        String password) {}
