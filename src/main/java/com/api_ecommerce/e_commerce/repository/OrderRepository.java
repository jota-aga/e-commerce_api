package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.models.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
