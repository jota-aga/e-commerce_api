package com.api_ecommerce.e_commerce.mapper;

import java.util.List;

import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.dto.order.OrderBuyerResponse;
import com.api_ecommerce.e_commerce.entity.Order;

public interface OrderMapper {
	
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
	
	OrderAdminResponse orderToOrderAdminResponse(Order order);
	OrderBuyerResponse orderToOrderBuyerResponse(Order order);
	List<OrderAdminResponse> listOrderToListOrderAdminResponse(List<Order> orders);
	List<OrderBuyerResponse> listOrderToListOrderBuyerResponse(List<Order> orders);
}	
