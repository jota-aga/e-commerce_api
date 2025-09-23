package com.api_ecommerce.e_commerce.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findAllOrderByUserId(Long id);
	List<Order> findAllByCreatedAt(LocalDate date);
}
