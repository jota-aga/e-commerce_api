package com.api_ecommerce.e_commerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.User;

public record OrderAdminResponse(User user, List<OrderItemResponse> ordersItemResponse, LocalDate createdAt, BigDecimal totalValue)
{
}
