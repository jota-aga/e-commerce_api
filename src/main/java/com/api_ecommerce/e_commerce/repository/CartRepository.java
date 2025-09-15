package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.models.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
}
