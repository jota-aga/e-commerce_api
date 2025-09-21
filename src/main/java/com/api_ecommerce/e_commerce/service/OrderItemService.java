package com.api_ecommerce.e_commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.models.order_item.OrderItem;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;

@Service
public class OrderItemService {
	@Autowired
	OrderItemRepository orderItemRepository;
	
	public void saveAll(List<OrderItem> ordersItem) {
		orderItemRepository.saveAll(ordersItem);
	}
}
