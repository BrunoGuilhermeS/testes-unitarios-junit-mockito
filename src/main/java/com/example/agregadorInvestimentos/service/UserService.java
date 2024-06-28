package com.example.agregadorInvestimentos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.agregadorInvestimentos.controller.dto.AccountResponseDto;
import com.example.agregadorInvestimentos.controller.dto.CreateAccountDTO;
import com.example.agregadorInvestimentos.entity.Account;
import com.example.agregadorInvestimentos.entity.BillingAddress;
import com.example.agregadorInvestimentos.repository.AccountRepository;
import com.example.agregadorInvestimentos.repository.BillingAddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.agregadorInvestimentos.controller.dto.CreateUserDTO;
import com.example.agregadorInvestimentos.controller.dto.UpdateUserDTO;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private AccountRepository accountRepository;

	private BillingAddressRepository billingAddressRepository;
	
	public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
		this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }


	public UUID createUser(CreateUserDTO createUserDTO) {
		//DTO -converter-> ENTITY
		var entity = new User(
				UUID.randomUUID(), 
				createUserDTO.username(), 
				createUserDTO.email(), 
				createUserDTO.password(),
				Instant.now(),
				null);
		
		var userSaved = userRepository.save(entity);
		return userSaved.getUserId();
	}
	public Optional<User> getUserById(String userId) {
		
		return userRepository.findById(UUID.fromString(userId));
		
	}
	
	public List<User> listUsers() {
		return userRepository.findAll();
	}
	
	public void updateUserById(String userId, UpdateUserDTO updateUserDTO) {
		var id = UUID.fromString(userId);
		var userExist = userRepository.findById(id);
		if (userExist.isPresent()) {
			var user = userExist.get();
			
			if (updateUserDTO.email() != null) {
				user.setEmail(updateUserDTO.email());
			}
			if (updateUserDTO.password() != null) {
				user.setPassword(updateUserDTO.password());
			}
			userRepository.save(user);
		}
	}
	
	public void deleteById(String userId) {
		var id = UUID.fromString(userId);
		var userExists = userRepository.existsById(UUID.fromString(userId));	
		if (userExists) {
			userRepository.deleteById(id);
		}
	}

	public void createAccount(String userId, CreateAccountDTO createAccountDTO) {

		var user =	userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		//DTO -> ENTITY

				var account = new Account(
				UUID.randomUUID(),
				createAccountDTO.description(),
				user,
				null,
						new ArrayList<>()
		);

			var accountCreated = accountRepository.save(account);

			var billingAddress = new BillingAddress(
					accountCreated.getAccountId(),
					account,
					createAccountDTO.street(),
					createAccountDTO.number()
			);

			billingAddressRepository.save(billingAddress);
	}

	public List<AccountResponseDto> listAccounts(String userId) {
		var user =	userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		return user.getAccounts()
				.stream()
				.map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
				.toList();
	}
}
