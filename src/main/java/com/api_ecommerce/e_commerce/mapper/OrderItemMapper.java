package com.api_ecommerce.e_commerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.OrderItem;

@Mapper
public interface OrderItemMapper {
	
	OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);
	
	OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);
	
	@Mappings(
			{
				@Mapping(source = "product.name", target = "productName"),
				@Mapping(source = "product.description", target = "productDescription"),
				@Mapping(source = "product.price", target = "productPrice"),
			})
	OrderItem cartItemToOrderItem(CartItem cartItem);
}
