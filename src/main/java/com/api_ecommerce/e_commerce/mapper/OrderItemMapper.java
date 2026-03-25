package com.api_ecommerce.e_commerce.mapper;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class OrderItemMapper {
	public static OrderItemResponse toDTO(OrderItem orderItem) {
		OrderItemResponse orderItemResponse = new OrderItemResponse(orderItem.getProductName(), orderItem.getProductDescription(), 
																	orderItem.getProductPrice(), orderItem.getQuantity());
		
		return orderItemResponse;
	}
	
	public static OrderItem toEntity(CartItem cartItem) {
		Product product = cartItem.getProduct();
		
		OrderItem orderItem = new OrderItem(
				product, product.getName(), product.getDescricao(), 
				product.getPrice(), cartItem.getQuantity()
				);
		
		return orderItem;
	}
}
