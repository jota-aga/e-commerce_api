package com.api_ecommerce.e_commerce.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
		@NotBlank
		String name
		) 		
{}
