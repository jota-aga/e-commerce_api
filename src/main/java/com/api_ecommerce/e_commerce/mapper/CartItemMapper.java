package com.api_ecommerce.e_commerce.mapper;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.entity.CartItem;

public class CartItemMapper {
	
	public static CartItemResponse toDTO(CartItem cartItem) {
		CartItemResponse dto = new CartItemResponse(ProductMapper.toDTO(cartItem.getProduct()), cartItem.getQuantity());
		
		return dto;
	}
}
