package com.api_ecommerce.e_commerce.mapper;

import java.util.List;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Product;

public class ProductMapper {
	public static ProductResponse toDTO(Product product) {
		ProductResponse dto = new ProductResponse(product.getName(), product.getDescricao(), 
										product.getPrice(), product.getCategory().getName(), product.getQuantity());
		
		return dto;
	}
	
	public static List<ProductResponse> toDTOList(List<Product> products){
		List<ProductResponse> dto = products.stream()
									   .map(product -> ProductMapper.toDTO(product))
									   .toList();
		
		return dto;
	}
	
	public static Product toEntity(ProductRequest productRequest, Product product) {
		product.setName(productRequest.name());
		product.setDescricao(productRequest.description());
		product.setPrice(productRequest.price());
		product.setQuantity(productRequest.quantity());
		
		return product;
	}
}
