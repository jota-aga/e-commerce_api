package com.api_ecommerce.e_commerce.mapper;

import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.category.CategoryRequest;
import com.api_ecommerce.e_commerce.entity.Category;

public class CategoryMapper {
	public static CategoryRequest toDTO(Category category) {
		CategoryRequest dto = new CategoryRequest(category.getName());
		
		return dto;
	}
	
	public static List<CategoryRequest> toListDTO(List<Category> categorys){
		List<CategoryRequest> dtos = new ArrayList<CategoryRequest>();
		
		categorys.forEach(category -> {
						  CategoryRequest categoryRequest = new CategoryRequest(category.getName());
						  dtos.add(categoryRequest);
		});
		
		return dtos;
	}
}
