package com.api_ecommerce.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	List<OrderItem> findAllByProductId(Long productId);
}
