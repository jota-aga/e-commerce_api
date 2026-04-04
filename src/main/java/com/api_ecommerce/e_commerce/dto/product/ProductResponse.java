package com.api_ecommerce.e_commerce.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
		String name,
		
		String description,
		
		BigDecimal price,
		
		String categoryName,
		
		int quantity) {

}
