package com.api_ecommerce.e_commerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemRequest;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class OrderItemService {
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	public void save(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
	}
	
	public OrderItem findOrderItemById(Long id) {
		Optional<OrderItem> orderItem = orderItemRepository.findById(id);
		
		return orderItem.orElseThrow(() -> new IdNotFoundException("Order Item id"));
	}
	
	public void deleteOrderItem(Long id) {
		OrderItem orderItem = findOrderItemById(id);
		orderItemRepository.delete(orderItem);
	}
	
	public void editOrderItem(Long id, OrderItemRequest orderItemDTO) {
		OrderItem orderItem = findOrderItemById(id);
		
		orderItem.setProductDescription(orderItemDTO.productDescription());
		orderItem.setProductName(orderItemDTO.productName());
		orderItem.setProductPrice(orderItemDTO.productPrice());
		orderItem.setQuantity(orderItemDTO.quantity());
		
		save(orderItem);
	}

	public void createOrderItem(OrderItemRequest orderItemRequest, Long orderId) {
		Order order = findOrderById(orderId);
		
		OrderItem orderItem = new OrderItem(orderItemRequest.productName(), orderItemRequest.productDescription(), orderItemRequest.productPrice(), orderItemRequest.quantity(), order);
		
		save(orderItem);
	}
	
	private Order findOrderById(Long orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		
		Order order = optionalOrder.orElseThrow(() -> new IdNotFoundException("Order"));
		
		return order;
	}
}
