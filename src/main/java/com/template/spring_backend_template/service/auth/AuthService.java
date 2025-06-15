package com.template.spring_backend_template.service.auth;

import com.template.spring_backend_template.domain.auth.response.AuthResponse;
import com.template.spring_backend_template.domain.auth.request.RegisterRequest;
import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import com.template.spring_backend_template.common.exceptions.UserException;
import com.template.spring_backend_template.common.infra.security.TokenService;
import com.template.spring_backend_template.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    public AuthResponse login(String login, String password) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                login,
                password);

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new AuthResponse(token);
    }

    public void register(RegisterRequest registerRequest) {
        if(authorizationService.loadUserByUsername(registerRequest.email()) != null) {
            throw new UserException("Usuário já existe");
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
    }
}
