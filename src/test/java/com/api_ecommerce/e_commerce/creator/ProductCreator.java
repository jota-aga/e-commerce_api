package com.api_ecommerce.e_commerce.creator;

import java.math.BigDecimal;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;

public class ProductCreator {
	
	public static Product productAvaliable() {
		Category category = CategoryCreator.simpleCategory();
		Product product = Product.builder()
								 .id(1L)
								 .name("Product")
								 .description("Description")
								 .price(new BigDecimal(50))
								 .quantity(100)
								 .category(category)
								 .build();
		
		return product;
	}
	
	public static Product productUnavaliable() {
		Category category = CategoryCreator.simpleCategory();
		Product product = Product.builder()
								 .id(2L)
								 .name("Product")
								 .description("Description")
								 .price(new BigDecimal(50))
								 .quantity(100)
								 .status(ProductStatus.UNAVAILABLE)
								 .category(category)
								 .build();
		return product;
	}
	
	public static ProductRequest unavailableProductRequest() {
		return new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.UNAVAILABLE);
	}
	
	public static ProductRequest availableProductRequest() {
		return new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.AVAILABLE);
	}
}
