package com.api_ecommerce.e_commerce.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.exceptions.AlreadyExistsException;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.mapper.ProductMapper;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

import jakarta.validation.Valid;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public void saveProduct(Product product) {
		
		productRepository.save(product);
	}
	
	public void createProduct(ProductRequest productRequest) {
		Category category = findCategoryById(productRequest.categoryId());
		
		Product product = new Product(productRequest.name(), productRequest.description(), 
									  productRequest.quantity(), productRequest.price(), category);
		
		validateNameOfProduct(productRequest, product);
		
		saveProduct(product);
		
	}
	
	public void editProduct(Long id, ProductRequest productRequest) {
		Product product = findProductById(id);
		
		validateNameOfProduct(productRequest, product);
		
		product.setName(productRequest.name());
		product.setDescricao(productRequest.description());
		product.setPrice(productRequest.price());
		product.setQuantity(productRequest.quantity());
		
		Category category = findCategoryById(productRequest.categoryId());
		
		product.setCategory(category);
		
		saveProduct(product);
	}
	
	public Product findProductById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		
		return product.orElseThrow(() -> new IdNotFoundException("Product"));
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
	
	public void deleteProduct(Long id){
		Product product = this.findProductById(id);
		
		productRepository.delete(product);
	}
	
	private Category findCategoryById(Long categoryId) {
		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
		
		Category category = optionalCategory.orElseThrow(() -> new IdNotFoundException("Category"));
		
		return category;
	}
	
	private Category findCategoryByName(String categoryName) {
		Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);
		
		Category category = optionalCategory.orElseThrow(() -> new RuntimeException("Name of Category not found"));
		
		return category;
	}
	
	private void validateNameOfProduct(ProductRequest productRequest, Product product) {
		Optional<Product> optionalProduct = productRepository.findByName(productRequest.name());
		
		if(optionalProduct.isPresent()) {
			Product repeatedProduct = optionalProduct.get();
			
			if(product.getId() == null) {
				throw new AlreadyExistsException("Product's name");
			}
			else if(!product.getId().equals(repeatedProduct.getId())){
				throw new AlreadyExistsException("Product's name");
			}
		}
	}
}
