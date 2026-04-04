package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public void createCategory(CategoryDTO dto) {
		Category category = Category.builder()
									.name(dto.name())
									.build();
		
		saveCategory(category);
	}
	
	public Category findCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		
		return category.orElseThrow(() -> new NotFoundException("Category's id"));
	}
	
	public Category findCategoryByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		
		return category.orElseThrow(() -> new NotFoundException("Category"));
	}
	
	public List<Category> findAllCategory() {
		List<Category> categorys = categoryRepository.findAll();
		
		return categorys;		
	}
	
	public void deleteCategory(Long id) {
		Category category = findCategoryById(id);
		
		categoryRepository.delete(category);
	}
	
	public void editCategory(Long id, CategoryDTO CategoryDTO) {
		Category category = findCategoryById(id);
		
		category.setName(CategoryDTO.name());
		
		saveCategory(category);
	}
}
