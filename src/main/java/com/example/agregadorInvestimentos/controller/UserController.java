package com.example.agregadorInvestimentos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.agregadorInvestimentos.entity.User;
import com.example.agregadorInvestimentos.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	private UserService userService;
		
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
		var userId = userService.createUser(createUserDTO);
		return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build(); //.build() para montar a requisição		
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable("userId") String UserId) {
		var user = userService.getUserById(UserId);
		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}else {
			return ResponseEntity.notFound().build();
		}		
	}
	
	@GetMapping
	public ResponseEntity<List<User>> listUsers() {
		var users = userService.listUsers();
		return ResponseEntity.ok(users);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<Void> updateUserById(@PathVariable("userId") String userId, @RequestBody UpdateUserDTO updateUserDTO) {
		userService.updateUserById(userId, updateUserDTO);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deletById(@PathVariable("userId") String userId) {
		userService.deleteById(userId);
		return ResponseEntity.noContent().build();
	}
}
