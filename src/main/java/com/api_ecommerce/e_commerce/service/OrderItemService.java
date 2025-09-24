package com.api_ecommerce.e_commerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemRequest;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;

@Service
public class OrderItemService {
	@Autowired
	OrderItemRepository orderItemRepository;
	
	public void save(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
	}
	
	public OrderItem findOrderItemById(Long id) {
		Optional<OrderItem> orderItem = orderItemRepository.findById(id);
		
		return orderItem.orElseThrow(() -> new IdNotFoundException("Order Item id"));
	}
	
	public void deleteOrderItem(OrderItem orderItem) {
		orderItemRepository.delete(orderItem);
	}
	
	public OrderItem editOrderItem(OrderItem orderItem, OrderItemRequest orderItemDTO) {
		orderItem.setProductDescription(orderItemDTO.productDescription());
		orderItem.setProductName(orderItemDTO.productName());
		orderItem.setProductPrice(orderItemDTO.productPrice());
		orderItem.setQuantity(orderItemDTO.quantity());
		
		return orderItem;
	}
}
