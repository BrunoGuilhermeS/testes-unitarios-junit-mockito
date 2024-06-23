package com.example.agregadorInvestimentos.service;

import com.example.agregadorInvestimentos.controller.CreateUserDTO;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Deve criar um usuario com sucesso")
        void deveCriarUsuarioComSucesso() {

            //Arrange

            var user = new User(
                    UUID.randomUUID(),
                    "usernameteste",
                    "teste@email.com",
                    "senhateste123",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDTO("usernameteste", "teste@email.com", "senhateste123");

            //Act
            var output = userService.createUser(input);

            //Assert

            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }
        @Test
        @DisplayName("Should throw exption when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            //Arrange

            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDTO("usernameteste", "teste@email.com", "senhateste123");

            //Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }
    }

}