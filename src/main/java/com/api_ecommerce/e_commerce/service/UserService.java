package com.api_ecommerce.e_commerce.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.UsernameOrPasswordIncorrectException;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	public void saveUser(User user) {
		userRepository.save(user);
		Cart cart = new Cart(user);
		cartRepository.save(cart);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		
		return user.orElseThrow(() -> new IdNotFoundException("User"));
	}
	
	public User findUserByLogin(String login) {
		Optional<User> user = userRepository.findByLogin(login);
		
		return user.orElseThrow(() -> new UsernameOrPasswordIncorrectException());
	}
	
	public void isLoginCorrect(LoginRequest loginRequest, User user, PasswordEncoder passwordEncoder) {
		if(passwordEncoder.matches(loginRequest.password(), user.getPassword()) == false) throw new UsernameOrPasswordIncorrectException();
	}
}
