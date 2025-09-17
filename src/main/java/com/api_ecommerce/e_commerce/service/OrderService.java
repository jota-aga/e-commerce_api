package com.api_ecommerce.e_commerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.models.cart_item.CartItem;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	public void saveOrder(CartItem order) {
		
		orderRepository.save(order);
	}
	
	public List<CartItem> findOrdersByCartId(Long id){
		List<CartItem> orders = orderRepository.findAllByCartId(id);
		
		return orders;
	}
	
	public CartItem findOrderById(Long id) {
		Optional<CartItem> order = orderRepository.findById(id);
	
		return order.orElseThrow(() -> new IdNotFoundException("Order"));
	}
	
	public void deleteOrderById(CartItem order) {
		orderRepository.delete(order);	
	}
}
