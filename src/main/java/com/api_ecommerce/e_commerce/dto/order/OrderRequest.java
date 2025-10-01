package com.api_ecommerce.e_commerce.dto.order;


import jakarta.validation.constraints.NotNull;

public record OrderRequest(
		@NotNull
		Long userId
		) 
{}
