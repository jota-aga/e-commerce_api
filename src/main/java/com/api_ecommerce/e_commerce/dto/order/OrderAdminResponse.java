package com.api_ecommerce.e_commerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.entity.Buyer;

public record OrderAdminResponse(Buyer buyer, List<OrderItemResponse> ordersItemResponse, LocalDate createdAt, BigDecimal totalValue)
{
}
