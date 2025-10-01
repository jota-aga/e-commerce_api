package com.api_ecommerce.e_commerce.dto.product;

import java.math.BigDecimal;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDTO(
	@NotBlank
	String name,
	
	@NotBlank
	String description,
	
	@Positive
	@NotNull
	BigDecimal price,
	
	@NotNull
	Long categoryId,
	
	@PositiveOrZero
	@NotNull
	int quantity
	)
{
}
