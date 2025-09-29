package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterRequest;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.service.TokenService;
import com.api_ecommerce.e_commerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenService tokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest register){
		User user = new User(register.getLogin(), register.getPassword(), register.getRole());
		userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest login){
		var usernamePassword =  new UsernamePasswordAuthenticationToken(login.username(), login.password()); 
		var auth = authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((User) auth.getPrincipal());
		LoginResponse loginResponse = new LoginResponse(token);
		
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
	}
	
}
