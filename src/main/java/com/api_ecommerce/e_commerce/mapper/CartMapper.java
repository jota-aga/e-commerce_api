package com.api_ecommerce.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.dto.cart.CartClientResponse;
import com.api_ecommerce.e_commerce.entity.Cart;

@Mapper
public interface CartMapper {
	
	CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
	
	CartClientResponse cartToCartClienteResponse(Cart cart);
	
	CartAdminResponse cartToAdminCartResponse(Cart cart);
}
