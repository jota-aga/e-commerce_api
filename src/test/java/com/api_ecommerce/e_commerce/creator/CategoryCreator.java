package com.api_ecommerce.e_commerce.creator;

import java.util.List;

import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;

public class CategoryCreator {
	
	public static Category simpleCategory() {
		return Category.builder()
					   .name("Category")
					   .id(1L)
					   .build();
	}
	
	public static Category categoryWithProducts() {
		List<Product> products = List.of(ProductCreator.productAvaliable(), ProductCreator.productUnavaliable());
		return new Category(2L, "Category", products);
	}
}
