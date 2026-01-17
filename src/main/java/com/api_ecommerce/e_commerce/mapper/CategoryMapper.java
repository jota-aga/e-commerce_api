package com.api_ecommerce.e_commerce.mapper;

import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;

public class CategoryMapper {
	public static CategoryDTO toDTO(Category category) {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		return dto;
	}
	
	public static List<CategoryDTO> toListDTO(List<Category> categorys){
		List<CategoryDTO> dtos = new ArrayList<CategoryDTO>();
		
		categorys.forEach(category -> {
						  CategoryDTO categoryDTO = new CategoryDTO(category.getName());
						  dtos.add(categoryDTO);
		});
		
		return dtos;
	}
}
