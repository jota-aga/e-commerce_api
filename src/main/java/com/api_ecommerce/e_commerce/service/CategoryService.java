package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;

	public void createCategory(CategoryDTO dto) {
		Category category = Category.builder().name(dto.name()).build();

		validateNameRepeated(category, dto);	
		
		categoryRepository.save(category);
	}

	public Category findCategoryById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);

		return category.orElseThrow(() -> new NotFoundException("Category id"));
	}

	public Category findCategoryByName(String name) {
		Optional<Category> category = categoryRepository.findByName(name);

		return category.orElseThrow(() -> new NotFoundException("Category"));
	}

	public List<Category> findAllCategory() {
		List<Category> categorys = categoryRepository.findAll();

		return categorys;
	}

	@Transactional
	public void deleteCategory(Long id) {
		Category category = findCategoryById(id);

		removeCategoryOfProducts(category);

		categoryRepository.delete(category);
	}
	
	@Transactional
	public void editCategory(Long id, CategoryDTO categoryDTO) {
		Category category = findCategoryById(id);
		
		validateNameRepeated(category, categoryDTO);
		
		category.setName(categoryDTO.name());

		categoryRepository.save(category);
	}

	private void removeCategoryOfProducts(Category category) {
		List<Product> products = productRepository.findAllByCategoryId(category.getId());
		
		if (products != null && !products.isEmpty()) {
			products.forEach(product -> {
				product.setCategory(null);
			});
			
			products.clear();
		}
	}

	private void validateNameRepeated(Category category, CategoryDTO dto) {
		Optional<Category> optionalRepeatedCategory = categoryRepository.findByName(dto.name());

		if (optionalRepeatedCategory.isPresent()) {
			if (category.getId() == null || category.getId() == 0) {
				throw new ConflictException("Category name already exists");

			} else {
				Category repeatedCategory = optionalRepeatedCategory.get();

				if (category.getId() != repeatedCategory.getId())
					throw new ConflictException("Category name already exists");
			}
		}
	}
}
