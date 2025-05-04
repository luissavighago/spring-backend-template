package com.template.spring_backend_template.service.auth;

import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import com.template.spring_backend_template.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    User user;

    @BeforeEach
    void setup() {
        user = new User(
                "John",
                "Doe",
                "jhon.doe@exemple.com",
                "encryptedPassword",
                UserRoleEnum.USER,
                true);
    }

    @Test
    @DisplayName("Deve carregar o usuário pelo nome de usuário")
    void shouldLoadUserByUsername() {

        String username = "jhon.doe@exemple.com";
        BDDMockito.given(userService.findByLogin(username)).willReturn(user);

        UserDetails foundUser = authorizationService.loadUserByUsername(username);

        Mockito.verify(userService).findByLogin(stringArgumentCaptor.capture());
        Assertions.assertEquals(username, stringArgumentCaptor.getValue());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    @DisplayName("Deve retorna null quando o usuário não existe")
    void shouldReturnNullWhenUserNotFound(){

        String username = "jhon@exemple.com";
        BDDMockito.given(userService.findByLogin(username)).willReturn(null);

        UserDetails foundUser = authorizationService.loadUserByUsername(username);

        Mockito.verify(userService).findByLogin(stringArgumentCaptor.capture());
        Assertions.assertEquals(username, stringArgumentCaptor.getValue());

        Assertions.assertNull(foundUser);
    }
}