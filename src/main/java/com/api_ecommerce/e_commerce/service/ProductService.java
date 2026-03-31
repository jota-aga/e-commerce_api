package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Transactional
	public void createProduct(ProductRequest productRequest) {
		Category category = findCategoryById(productRequest.categoryId());
		
		Product product = new Product(productRequest.name(), productRequest.description(), 
									  productRequest.quantity(), productRequest.price(), category, ProductStatus.AVAILABLE);
		
		validateNameOfProduct(product);
		
		productRepository.save(product);
		
	}
	
	
	@Transactional
	public void editProduct(Long id, ProductRequest productRequest) {
		Product product = findProductById(id);
		
		product.setName(productRequest.name());
		product.setDescricao(productRequest.description());
		product.setPrice(productRequest.price());
		product.setQuantity(productRequest.quantity());
		product.setStatus(productRequest.status());
		
		validateNameOfProduct(product);
		
		Category category = findCategoryById(productRequest.categoryId());
		
		product.setCategory(category);
		
		productRepository.save(product);
	}
	
	public Product findProductById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		
		return product.orElseThrow(() -> new NotFoundException("Product's id"));
	}
	
	public List<Product> findAllProducts(){
		List<Product> products = productRepository.findAll();
		
		return products;
	}
	
	public List<Product> findAllByName(String name){
		List<Product> products = productRepository.findAllByNameIgnoreCase(name);
		
		return products;
	}
	
	public List<Product> findAllByCategory(String categoryName){
		Category category = findCategoryByName(categoryName);
		
		List<Product> products = productRepository.findAllByCategoryId(category.getId());
				
		return products;
	}
	
	@Transactional
	public void deleteProduct(Long id){
		Product product = this.findProductById(id);
		
		productRepository.delete(product);
	}
	
	private Category findCategoryById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		
		Category category = optionalCategory.orElseThrow(() -> new NotFoundException("Category's id"));
		
		return category;
	}
	
	private Category findCategoryByName(String categoryName) {
		Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
		
		Category category = optionalCategory.orElseThrow(() -> new RuntimeException("Name of Category not found"));
		
		return category;
	}
	
	private void validateNameOfProduct(Product product) {
		Optional<Product> optionalProduct = productRepository.findByName(product.getName());
		
		if(optionalProduct.isPresent()) {
			Product repeatedProduct = optionalProduct.get();
			
			if(product.getId() == null) {
				throw new ConflictException("This name of product is already registered");
			}
			else if(!product.getId().equals(repeatedProduct.getId())){
				throw new ConflictException("This name of product is already registered");
			}
		}
	}
}
