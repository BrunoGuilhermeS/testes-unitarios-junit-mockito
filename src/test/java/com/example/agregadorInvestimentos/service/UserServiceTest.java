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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

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

    @Nested
    class getUserById {

        @Test
        @DisplayName("Deve encontrar o usuario por ID com sucesso quando o optional está presente")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {

            //Arrange

            var user = new User(
                    UUID.randomUUID(),
                    "usernameteste",
                    "teste@email.com",
                    "senhateste123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act

            var output = userService.getUserById(user.getUserId().toString());

            //Assert

            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Deve encontrar o usuario por ID com sucesso quando o optional está vazio")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {

            //Arrange

            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act

            var output = userService.getUserById(userId.toString());

            //Assert

            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUser {

        @Test
        @DisplayName("Deve retornar todos os usuarios com sucesso")
        void shouldReturnAllUsersWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "usernameteste",
                    "teste@email.com",
                    "senhateste123",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(List.of(user)).when(userRepository).findAll();
            //Act

            var output = userService.listUsers();

            //Assert

            assertNotNull(output);
            assertEquals(userList.size(), output.size());

        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Deve deletar um usuario com sucesso")
        void shouldDeleteUserWithSuccess() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "usernameteste",
                    "teste@email.com",
                    "senhateste123",
                    Instant.now(),
                    null
            );

            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());

            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            //Act

            userService.deleteById(userId.toString());

            //Assert
            var idList = uuidArgumentCaptor.getAllValues();

            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));
            assertEquals(userId, uuidArgumentCaptor.getValue());

        }
    }

}