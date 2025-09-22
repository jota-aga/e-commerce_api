package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	public void saveOrder(Order order) {
		
		orderRepository.save(order);
	}
	
	public List<Order> findOrdersByUserId(Long id) {
		List<Order> orders = orderRepository.findAllOrderByUserId(id);
		
		return orders;
	}
	
	public Order findOrderById(Long id) {
		Optional<Order> order = orderRepository.findById(id);
	
		return order.orElseThrow(() -> new IdNotFoundException("Order"));
	}
	
	public void deleteOrderById(Order order) {
		orderRepository.delete(order);	
	}
}
