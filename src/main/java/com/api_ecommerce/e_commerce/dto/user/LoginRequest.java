package com.api_ecommerce.e_commerce.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank
		String username,
		
		@NotBlank
		String password
		) 
{}

