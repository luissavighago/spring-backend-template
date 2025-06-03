package com.template.spring_backend_template.service.auth;

import com.template.spring_backend_template.domain.auth.request.RegisterRequest;
import com.template.spring_backend_template.domain.auth.response.AuthResponse;
import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import com.template.spring_backend_template.exceptions.UserException;
import com.template.spring_backend_template.infra.security.TokenService;
import com.template.spring_backend_template.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    UserService userService;

    @Mock
    private TokenService tokenService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    RegisterRequest registerRequest;

    @BeforeEach
    void setup() {
        registerRequest = new RegisterRequest(
                "User",
                "Test",
                "emailtest@exemple.com",
                "password123"
        );
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void shouldLoginWithSuccess() {
        String login = "useremail@exemple.com";
        String password = "password";
        String token = "generatedToken";
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(login, password);

        User user = new User(
                UUID.randomUUID(),
                "User",
                "Test",
                "useremail@exemple.com",
                "encryptedPassword",
                UserRoleEnum.USER,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, simpleGrantedAuthorities);

        BDDMockito.given(authenticationManager.authenticate(usernamePassword)).willReturn(authentication);
        BDDMockito.given(tokenService.generateToken((User) authentication.getPrincipal()))
                .willReturn(token);

        AuthResponse authResponse = authService.login(login, password);

        Assertions.assertNotNull(authResponse);
        Assertions.assertNotNull(authResponse.token());
        Assertions.assertFalse(authResponse.token().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer login com usuário inválido")
    void shouldThrowExceptionWhenLoginWithInvalidUser() {
        String login = "useremail@exemple.com";
        String password = "password";
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(login, password);

        BDDMockito.given(authenticationManager.authenticate(usernamePassword)).willThrow(new AccessDeniedException("Access Denied"));

        Assertions.assertThrows(AccessDeniedException.class, () -> authService.login(login, password));
    }

    @Test
    @DisplayName("Deve salvar usuário com sucesso")
    void shouldSaveUserWithSuccess() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        BDDMockito.given(authorizationService.loadUserByUsername(registerRequest.email())).willReturn(null);

        authService.register(registerRequest);
        Mockito.verify(userService).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        Assertions.assertEquals(registerRequest.email(), savedUser.getEmail());
        Assertions.assertEquals(registerRequest.name(), savedUser.getName());
        Assertions.assertEquals(registerRequest.lastname(), savedUser.getLastname());
        assertTrue(passwordEncoder.matches(registerRequest.password(), savedUser.getPassword()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar usuário já existente")
    void shouldThrowExceptionWhenRegisterUserWithInvalidEmail() {

        BDDMockito.given(authorizationService.loadUserByUsername(registerRequest.email()))
                .willThrow(new UserException("Usuário já existe"));

        Assertions.assertThrows(UserException.class, () -> authService.register(registerRequest));
    }
}