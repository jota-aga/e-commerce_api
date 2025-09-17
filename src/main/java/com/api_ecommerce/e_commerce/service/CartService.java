package com.api_ecommerce.e_commerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.repository.CartRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	public void saveCart(Cart cart) {
		cartRepository.save(cart);
	}
	
	public Cart findCartById(Long id) {
		Optional<Cart> cart = cartRepository.findById(id);
		
		return cart.get();
	}
}
