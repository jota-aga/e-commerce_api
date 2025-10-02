package com.api_ecommerce.e_commerce.dto.cart_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemAdminRequest
(
	@NotNull
	Long productId,
	
	@NotNull
	Long cartId,
	
	@Positive
	@NotNull
	int quantity
)
{}

	