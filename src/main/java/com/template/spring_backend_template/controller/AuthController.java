package com.template.spring_backend_template.controller;

import com.template.spring_backend_template.domain.RestResponse;
import com.template.spring_backend_template.domain.auth.request.AuthRequest;
import com.template.spring_backend_template.domain.auth.response.AuthResponse;
import com.template.spring_backend_template.domain.auth.request.RegisterRequest;
import com.template.spring_backend_template.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<RestResponse> login(@RequestBody @Valid AuthRequest authRequest) {

        AuthResponse authResponse = authService.login(authRequest.login(), authRequest.password());
        return ResponseEntity.ok(
            new RestResponse(true, "Login successful", authResponse)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {

        authService.register(registerRequest);
        return ResponseEntity.ok(
            new RestResponse(true, "User registered successfully", null)
        );
    }
}
