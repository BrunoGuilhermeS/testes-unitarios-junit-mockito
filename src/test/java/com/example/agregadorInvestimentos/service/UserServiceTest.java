package com.example.agregadorInvestimentos.service;

import com.example.agregadorInvestimentos.controller.CreateUserDTO;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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

            doReturn(user).when(userRepository).save(any());
            var input = new CreateUserDTO("usernameteste", "teste@email.com", "senhateste123");

            //Act
            var output = userService.createUser(input);

            //Assert

            assertNotNull(output);
        }

    }

}