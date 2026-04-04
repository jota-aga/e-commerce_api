package com.api_ecommerce.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.entity.CartItem;

@Mapper
public interface CartItemMapper {
	
	CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
	
	CartItemResponse cartItemToCartItemResponse(CartItem cartItem);
}
