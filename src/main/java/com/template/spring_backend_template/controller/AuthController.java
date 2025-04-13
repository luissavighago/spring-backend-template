package com.template.spring_backend_template.controller;

import com.template.spring_backend_template.domain.auth.AuthRequest;
import com.template.spring_backend_template.domain.auth.RegisterRequest;
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
    public ResponseEntity login(@RequestBody @Valid AuthRequest authRequest) {

        String token = authService.login(authRequest.login(), authRequest.password());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest registerRequest) {

        authService.register(registerRequest);
        return ResponseEntity.ok("Register successful");
    }
}
