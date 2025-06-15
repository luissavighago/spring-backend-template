package com.template.spring_backend_template.controller;

import com.template.spring_backend_template.common.exceptions.UserException;
import com.template.spring_backend_template.domain.auth.request.RegisterRequest;
import com.template.spring_backend_template.domain.auth.response.AuthResponse;
import com.template.spring_backend_template.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    private String jsonLogin;

    private String registerRequestJson;

    @BeforeEach
    void setup() {
        jsonLogin = """
            {
                "login": "user.test@exemple.com",
                "password": "1234567"
            }
        """;

        registerRequestJson = """
            {
                "name": "User",
                "lastname": "Test",
                "email": "user.test@exemple.com",
                "password": "1234567"
            }
        """;
    }

    @Test
    @DisplayName("Deve retornar 200 ao fazer login com credenciais válidas")
    void shouldReturn200LoginWithValidCredentials() throws Exception {

        BDDMockito.given(authService.login("user.test@exemple.com","1234567"))
                .willReturn(new AuthResponse("token"));

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content(jsonLogin)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deve retornar 403 ao fazer login com credenciais inválidas")
    void shouldReturn403LoginWithInvalidCredentials() throws Exception {

        BDDMockito.given(authService.login("user.test@exemple.com","1234567"))
                .willThrow(new AccessDeniedException("Access Denied"));

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType("application/json")
                        .content(jsonLogin)
        ).andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }

    @Test
    @DisplayName("Deve retornar 200 ao registrar um novo usuário")
    void shouldReturn200RegisterNewUser() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(registerRequestJson)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deve retornar 422 ao tentar registrar um usuário com dados inválidos")
    void shouldReturn422RegisterUserWithInvalidData() throws Exception {

        BDDMockito.willThrow(new UserException("Usuário já existe"))
                .given(authService).register(
                        new RegisterRequest("User", "Test", "user.test@exemple.com", "1234567")
                );

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(registerRequestJson)
        ).andReturn().getResponse();

        assertEquals(422, response.getStatus());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar registrar com dados inválidos")
    void shouldReturn400RegisterUserWithInvalidData() throws Exception {

        String json = """
            {"name": "User"}
        """;

        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType("application/json")
                        .content(json)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
}