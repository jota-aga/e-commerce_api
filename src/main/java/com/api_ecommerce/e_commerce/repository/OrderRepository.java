package com.api_ecommerce.e_commerce.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.Product;

public interface OrderRepository extends JpaRepository<Order, Long>{
	Page<Order> findAllOrderByBuyerId(Long buyerId, Pageable pageable);
	
	Page<Order> findAllByCreatedAtBetween(LocalDate start, LocalDate end, Pageable pageable);

	Page<Order> findDistinctByOrderItemsProduct(Product product, Pageable pageable);
}
