package com.example.agregadorInvestimentos.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

import com.example.agregadorInvestimentos.controller.CreateUserDTO;
import com.example.agregadorInvestimentos.controller.UpdateUserDTO;
import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
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
	//Buscar usuário pela iD
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

}
