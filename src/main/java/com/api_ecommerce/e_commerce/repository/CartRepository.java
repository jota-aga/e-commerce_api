package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByBuyerId(Long buyerId);
	@EntityGraph(attributePaths = {"cartItems", "cartItems.product"})
	Optional<Cart> findById(Long id);
	
}
