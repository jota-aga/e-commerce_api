package com.api_ecommerce.e_commerce.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.repository.CartRepository;

public class CartService {
	@Autowired
	private CartRepository repo;
	
	public void saveCart(Cart cart) {
		repo.save(cart);
	}
}
