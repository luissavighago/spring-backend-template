package com.template.spring_backend_template.domain.user;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }
}
