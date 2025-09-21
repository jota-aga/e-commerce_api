package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.models.order_item.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
