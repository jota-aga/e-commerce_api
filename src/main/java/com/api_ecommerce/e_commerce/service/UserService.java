package com.api_ecommerce.e_commerce.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterRequest;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.AlreadyExistsException;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.RoleNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.UsernameOrPasswordIncorrectException;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public void saveUser(User user) {
		Cart cart = new Cart(user);
		userRepository.save(user);
	}
	
	public void createUser(RegisterRequest register) {
		validateRepeatedUsername(register);
		
		String encryptedPassword = passwordEncoder.encode(register.password());
		
		Role role = findRoleByName(Role.Value.CLIENT.name());
		
		User user = new User(register.username(), encryptedPassword, Set.of(role));
		
		saveUser(user);
	}
	
	public LoginResponse doLogin(LoginRequest login) {
		User user = findUserByUsername(login.username());
				
		isLoginCorrect(login, user, passwordEncoder);
		
		String token = tokenService.generateToken(user);
		
		return new LoginResponse(token);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.orElseThrow(() -> new IdNotFoundException("User"));
	}
	
	public User findUserByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		
		return user.orElseThrow(() -> new UsernameOrPasswordIncorrectException());
	}
	
	private void isLoginCorrect(LoginRequest loginRequest, User user, PasswordEncoder passwordEncoder) {
		if(passwordEncoder.matches(loginRequest.password(), user.getPassword()) == false) throw new UsernameOrPasswordIncorrectException();
	}
	
	private Role findRoleByName(String roleName) {
		Optional<Role> optionalRole = roleRepository.findRoleByName(roleName);
		
		Role role = optionalRole.orElseThrow(() -> new RoleNotFoundException());
		
		return role;
	}
	
	private void validateRepeatedUsername(RegisterRequest register) {
		Optional<User> optionalUser = userRepository.findByUsername(register.username());
		
		if(optionalUser.isPresent()) {
			throw new AlreadyExistsException("Username");
		}
	}
}
