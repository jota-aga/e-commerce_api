package com.api_ecommerce.e_commerce.dto.cart_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemUserRequest(
	@NotNull
	 Long productId,
	
	@Positive
	 int quantity) 
{}
