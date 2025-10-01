package com.api_ecommerce.e_commerce.dto.cart_item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;

public record CartItemAdminRequest
(
	@NotBlank
	Long productId,
	
	@NotBlank
	Long cartId,
	
	@Positive
	@NotBlank
	int quantity
)
{}

	