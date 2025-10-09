package com.api_ecommerce.e_commerce.dto.order_item;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemRequest(
		
		@NotBlank
		String productName,
		
		@NotBlank		
		String productDescription,
		
		@Positive
		@NotNull
		BigDecimal productPrice,
		
		@Positive
		@NotNull
		int quantity
		) 
{}
