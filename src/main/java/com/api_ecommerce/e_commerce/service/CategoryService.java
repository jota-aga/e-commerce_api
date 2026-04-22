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

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional
	public void saveCategory(Category category) {
		validateNameRepeated(category);

		categoryRepository.save(category);
	}

	public void createCategory(CategoryDTO dto) {
		Category category = Category.builder().name(dto.name()).build();

		saveCategory(category);
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
	public void editCategory(Long id, CategoryDTO CategoryDTO) {
		Category category = findCategoryById(id);

		category.setName(CategoryDTO.name());

		saveCategory(category);
	}

	private void removeCategoryOfProducts(Category category) {
		List<Product> products = category.getProducts();
		
		if (products != null && !products.isEmpty()) {
			products.forEach(product -> {
				product.setCategory(null);
			});
		}
		
		category.setProducts(products);
	}

	private void validateNameRepeated(Category category) {
		Optional<Category> optionalRepeatedCategory = categoryRepository.findByName(category.getName());

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
