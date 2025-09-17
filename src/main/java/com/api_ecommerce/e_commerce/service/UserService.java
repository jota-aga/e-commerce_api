package com.api_ecommerce.e_commerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.user.RegisterRequest;
import com.api_ecommerce.e_commerce.models.user.User;
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
}
