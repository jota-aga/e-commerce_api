package com.api_ecommerce.e_commerce.mapper;

import java.math.BigDecimal;

import java.util.List;

import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.dto.cart.CartClientResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.entity.Cart;

public class CartMapper {
	
	public static CartClientResponse toClientDTO(Cart cart) {
		List<CartItemResponse> cartItemDTO = cart.getCartItems().stream()
																.map(cartItem -> CartItemMapper.toDTO(cartItem))
																.toList();
		
		BigDecimal totalValue = cart.getCartItems().stream()
												   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
												   .reduce(BigDecimal.ZERO, BigDecimal::add);
		CartClientResponse dto = new CartClientResponse(cartItemDTO, totalValue);
		
		return dto;
	}

	public static CartAdminResponse toAdminDTO(Cart cart) {
		List<CartItemResponse> cartItemDTO = cart.getCartItems().stream()
				.map(cartItem -> CartItemMapper.toDTO(cartItem))
				.toList();

		BigDecimal totalValue = cart.getCartItems().stream()
		   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
		   .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		CartAdminResponse dto = new CartAdminResponse(cart.getUser(), cartItemDTO, totalValue);
		
		return dto;
	}
}
