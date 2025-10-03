package com.api_ecommerce.e_commerce.mapper;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;

public class CategoryMapper {
	public static CategoryDTO toDTO(Category category) {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		return dto;
	}
}
