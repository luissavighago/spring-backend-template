package com.template.spring_backend_template.service.user;

import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.domain.user.UserRoleEnum;
import com.template.spring_backend_template.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

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
    @DisplayName("Deve salvar um usuário com sucesso")
    void saveWithSuccess() {

        Assertions.assertDoesNotThrow(() -> userService.save(user));

        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());

        User userSaved = userArgumentCaptor.getValue();
        Assertions.assertNotNull(userSaved);
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
    }

    @Test
    @DisplayName("Deve retornar um usuário pelo login")
    void shouldFindUserByLogin() {

        String username = "jhon.doe@exemple.com";
        BDDMockito.given(userRepository.findByLogin(username)).willReturn(user);

        UserDetails foundUser = userService.findByLogin(username);

        Mockito.verify(userRepository).findByLogin(stringArgumentCaptor.capture());
        Assertions.assertEquals(username, stringArgumentCaptor.getValue());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    @DisplayName("Deve retornar null quando o usuário não for encontrado")
    void shouldReturnNullWhenUserNotFound() {

        String username = "jhon@exemple.com";
        BDDMockito.given(userRepository.findByLogin(username)).willReturn(null);

        UserDetails foundUser = userService.findByLogin(username);

        Mockito.verify(userRepository).findByLogin(stringArgumentCaptor.capture());
        Assertions.assertEquals(username, stringArgumentCaptor.getValue());

        Assertions.assertNull(foundUser);
    }
}