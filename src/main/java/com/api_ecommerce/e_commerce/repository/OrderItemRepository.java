package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
