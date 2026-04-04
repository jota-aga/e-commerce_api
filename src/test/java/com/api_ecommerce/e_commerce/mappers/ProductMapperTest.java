package com.api_ecommerce.e_commerce.mappers;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.mapper.ProductMapper;

public class ProductMapperTest {
	
	private Product product;
	
	@BeforeEach
	public void setUp() {
		product = Product.builder()
					   		 	 .name("product")
					   		 	 .description("description")
					   		 	 .price(new BigDecimal(100))
					   		 	 .quantity(50)
					   		 	 .status(ProductStatus.AVAILABLE)
					   		 	 .category(new Category(1l, "category", null))
					   		 	 .build();
	}
	
	@Test
	public void test() {
		ProductResponse p = ProductMapper.INSTANCE.productToProductResponse(product);
	}
}
