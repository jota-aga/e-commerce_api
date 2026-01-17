package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.mapper.CategoryMapper;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public void createCategory(CategoryDTO categoryDTO) {
		Category category = new Category(categoryDTO.name());
		
		saveCategory(category);
	}
	
	public Category findCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		
		return category.orElseThrow(() -> new IdNotFoundException("Category"));
	}
	
	public Category findCategoryByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		
		return category.orElseThrow(() -> new RuntimeException("Category not found"));
	}
	
	public List<CategoryDTO> findAllCategory() {
		List<Category> categorys = categoryRepository.findAll();
		
		return CategoryMapper.toListDTO(categorys);
				
				
	}
	
	public void deleteCategory(Long id) {
		Category category = findCategoryById(id);
		
		categoryRepository.delete(category);
	}
	
	public void editCategory(Long id, CategoryDTO categoryRequest) {
		Category category = findCategoryById(id);
		
		category.setName(categoryRequest.name());
		
		saveCategory(category);
	}
}
