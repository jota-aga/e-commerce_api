package com.api_ecommerce.e_commerce.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.dto.order.OrderClientResponse;
import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.Order;

public class OrderMapper {
	public static OrderAdminResponse toAdminDTO(Order order) {
		List<OrderItemResponse> orderItemDTO = order.getOrdersItem().stream()
																	.map(orderItem -> OrderItemMapper.toDTO(orderItem))
																	.toList();
		BigDecimal totalValue = order.getOrdersItem().stream()
													 .map(orderItem -> orderItem.getProductPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
													 .reduce(BigDecimal.ZERO, BigDecimal::add);
		OrderAdminResponse dto = new OrderAdminResponse(order.getUser(), orderItemDTO, order.getCreatedAt(), totalValue);
		
		return dto;
	}
	
	public static OrderClientResponse toClientDTO(Order order) {
		List<OrderItemResponse> ordersItemsDTO = order.getOrdersItem().stream()
																	  .map(orderItem -> OrderItemMapper.toDTO(orderItem))
																	  .toList();
		BigDecimal totalValue = order.getOrdersItem().stream()
				 .map(orderItem -> orderItem.getProductPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
				 .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		OrderClientResponse dto = new OrderClientResponse(ordersItemsDTO, order.getCreatedAt(), totalValue);
		return dto;
	}
	
	public static List<OrderAdminResponse> toListAdminDTO(List<Order> orders){
		List<OrderAdminResponse> dto = orders.stream()
				 .map(order -> toAdminDTO(order))
				 .toList();
		return dto;
	}
	
	public static List<OrderClientResponse> toListClientDTO(List<Order> orders){
		List<OrderClientResponse> dto = orders.stream()
				 .map(order -> toClientDTO(order))
				 .toList();
		return dto;
	}
}	
