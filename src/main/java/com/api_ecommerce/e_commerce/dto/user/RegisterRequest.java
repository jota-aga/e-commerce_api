package com.api_ecommerce.e_commerce.dto.user;




import jakarta.validation.constraints.NotNull;


public record RegisterRequest(@NotNull String username, @NotNull String password) {
	
	
}
