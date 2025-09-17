package com.api_ecommerce.e_commerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.models.category.Category;
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
}
