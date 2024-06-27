package com.example.agregadorInvestimentos.service;

import com.example.agregadorInvestimentos.controller.dto.CreateUserDTO;
import com.example.agregadorInvestimentos.controller.dto.UpdateUserDTO;
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
        @DisplayName("Deve deletar um usuario com sucesso se ele existir")
        void shouldDeleteUserWithSuccessWhenUserExists() {
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

            //verify -> verificar se os metodos estão sendo chamados
            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Não deve deletar um usuario com sucesso se ele NÃO existir")
        void shouldNotDeleteUserWithSuccessWhenUserNotExists() {
            //Arrange
            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();
            //Act

            userService.deleteById(userId.toString());

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            //verify -> verificar se os metodos estão sendo chamados
            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }

    }

    @Nested
    class updateById {

        @Test
        @DisplayName("Deve atualizar o usuário caso ele existir")
        void shouldUpdateUserWhenUserExists() {
            //Arrange

            var updateUserDTO = new UpdateUserDTO(
                    "newmail@mail.com",
                    "newpassword"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "usernameteste",
                    "teste@email.com",
                    "senhateste123",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());
            //Act
            userService.updateUserById(user.getUserId().toString(), updateUserDTO);

            //Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(updateUserDTO.email(), userCaptured.getEmail());
            assertEquals(updateUserDTO.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName(" Não deve atualizar o usuário caso ele não existir")
        void shouldNotUpdateUserWhenUserNOTExists() {
            //Arrange

            var updateUserDTO = new UpdateUserDTO(
                    "newmail@mail.com",
                    "newpassword"
            );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            //Act
            userService.updateUserById(userId.toString(), updateUserDTO);

            //Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(0)).save(any());
        }
    }
}