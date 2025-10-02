package com.api_ecommerce.e_commerce.dto.cart;

import java.math.BigDecimal;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.entity.User;

public record CartAdminResponse(User user, List<CartItemResponse> cartItems, BigDecimal totalValue) {}