package com.api_ecommerce.e_commerce.mapper;

import java.util.List;

import org.mapstruct.factory.Mappers;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;

public interface CategoryMapper {
	
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	CategoryDTO categoryToCategoryDTO(Category category);
	
	List<CategoryDTO> listCategoryToListCategoryDTO(List<Category> categorys);
}
