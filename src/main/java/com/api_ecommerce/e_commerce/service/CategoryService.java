package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.category.CategoryRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public Category findCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		
		return category.orElseThrow(() -> new IdNotFoundException("Category"));
	}
	
	public Category findCategoryByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		
		return category.orElseThrow(() -> new RuntimeException("Category not found"));
	}
	
	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}
	
	public void deleteCategory(Category category) {
		categoryRepository.delete(category);
	}
	
	public Category editCategory(Category category, CategoryRequest categoryRequest) {
		category.setName(categoryRequest.name());
		
		return category;
	}
}
