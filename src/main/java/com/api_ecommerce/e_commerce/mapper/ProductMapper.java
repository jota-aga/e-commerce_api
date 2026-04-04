package com.api_ecommerce.e_commerce.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Product;

@Mapper
public interface ProductMapper {
	
	public static final ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	@Mapping(source = "category.name", target = "categoryName")
	ProductResponse productToProductResponse(Product product);
	
	List<ProductResponse> listProductToListProductResponse(List<Product> list);

	Product updateProduct(ProductRequest productRequest, @MappingTarget Product product);
	
}
