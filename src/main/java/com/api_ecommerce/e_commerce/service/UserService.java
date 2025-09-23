package com.api_ecommerce.e_commerce.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private CartRepository cartRepository;
	
	public void saveUser(User user) {
		repo.save(user);
		Cart cart = new Cart(user);
		cartRepository.save(cart);
	}
	
	public User findUserById(Long id) {
		Optional<User> user = repo.findById(id);
		
		return user.orElseThrow(() -> new IdNotFoundException("User"));
	}
}
