package com.api_ecommerce.e_commerce.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductDTO(
	@NotBlank
	String name,
	
	@NotBlank
	String description,
	
	@Positive
	BigDecimal price,
	
	Long categoryId,
	
	@Min(value=0)
	int quantity
	)
{
}
