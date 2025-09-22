package com.api_ecommerce.e_commerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.User;

public class OrderResponse {
	private User user;
	
	private List<OrderItemResponse> ordersResponse;
	
	private LocalDate createdAt;
	
	private BigDecimal totalValue;
	
}
