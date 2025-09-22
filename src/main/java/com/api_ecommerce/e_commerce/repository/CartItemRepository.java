package com.api_ecommerce.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	List<CartItem> findAllByCartId(Long id);
	void deleteAllCartItemByCartId(Long cartId);
}
