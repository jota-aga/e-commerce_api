package com.api_ecommerce.e_commerce.mapper;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.OrderItem;

public class OrderItemMapper {
	public static OrderItemResponse toDTO(OrderItem orderItem) {
		OrderItemResponse orderItemResponse = new OrderItemResponse(orderItem.getProductName(), orderItem.getProductDescription(), 
																	orderItem.getProductPrice(), orderItem.getQuantity());
		
		return orderItemResponse;
	}
}
