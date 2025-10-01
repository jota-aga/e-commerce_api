package com.api_ecommerce.e_commerce.dto.cart_item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartItemClientRequest
(
		@NotBlank
		Long productId,
		
		@Positive
		@NotBlank
		int quantity
) 
{}
