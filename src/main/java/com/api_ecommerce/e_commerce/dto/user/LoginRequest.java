package com.api_ecommerce.e_commerce.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
		@NotEmpty
		String username,
		
		@NotEmpty
		String password
		) 
{}
