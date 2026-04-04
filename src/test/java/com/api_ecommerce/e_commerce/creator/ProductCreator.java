package com.api_ecommerce.e_commerce.creator;

import java.math.BigDecimal;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;

public class ProductCreator {
	
	public static Product productAvaliable() {
		Category category = CategoryCreator.simpleCategory();
		return new Product(1L, "Product", "Description", new BigDecimal(50), 100, ProductStatus.AVAILABLE, category);
	}
	
	public static Product productUnavaliable() {
		Category category = CategoryCreator.simpleCategory();
		return new Product(1L, "Product", "Description", new BigDecimal(50), 100, ProductStatus.UNAVAILABLE, category);
	}
	
	public static ProductRequest unavailableProductRequest() {
		return new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.UNAVAILABLE);
	}
	
	public static ProductRequest availableProductRequest() {
		return new ProductRequest("name", "description", new BigDecimal(100), 1L, 100, ProductStatus.AVAILABLE);
	}
}
