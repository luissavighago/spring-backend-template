package com.template.spring_backend_template.controller;

import com.template.spring_backend_template.domain.auth.AuthRequest;
import com.template.spring_backend_template.domain.auth.RegisterRequest;
import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import com.template.spring_backend_template.infra.security.TokenService;
import com.template.spring_backend_template.service.auth.AuthorizationService;
import com.template.spring_backend_template.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthRequest authRequest) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                authRequest.login(),
                authRequest.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest registerRequest) {

        if(authorizationService.loadUserByUsername(registerRequest.email()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());

        User user = new User(
                registerRequest.name(),
                registerRequest.lastname(),
                registerRequest.email(),
                encryptedPassword,
                UserRoleEnum.USER,
                true
        );

        userService.save(user);

        return ResponseEntity.ok("Register successful");
    }
}
