package com.api_ecommerce.e_commerce.controller;


import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterRequest;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.service.RoleService;
import com.api_ecommerce.e_commerce.service.TokenService;
import com.api_ecommerce.e_commerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest register){
		Role role = roleService.getRoleByName(Role.Value.CLIENT.name());
		
		User user = new User(register.username(), passwordEncoder.encode(register.password()), Set.of(role));
		userService.saveUser(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
		
		User user = userService.findUserByLogin(loginRequest.username());
		userService.isLoginCorrect(loginRequest, user, passwordEncoder);
		
		String token = tokenService.generateToken(user);
		LoginResponse loginResponse = new LoginResponse(token);						 
		
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
	}
	
}
