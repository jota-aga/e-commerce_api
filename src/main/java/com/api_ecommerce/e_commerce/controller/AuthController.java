package com.api_ecommerce.e_commerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterRequest;
import com.api_ecommerce.e_commerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest register){
		userService.createUser(register);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
		
		LoginResponse loginResponse = userService.doLogin(loginRequest); 					 
		
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
	}
	
}
