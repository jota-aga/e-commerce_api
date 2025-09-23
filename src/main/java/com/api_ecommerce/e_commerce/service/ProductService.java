package com.api_ecommerce.e_commerce.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.product.ProductDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	public void saveProduct(Product product) {
		
		productRepository.save(product);
	}
	
	public Product editProduct(Product product, ProductDTO productDTO) {
		
		product.setName(productDTO.name());
		product.setDescricao(productDTO.description());
		product.setPrice(productDTO.price());
		product.setQuantity(productDTO.quantity());
		
		Optional<Category> category = categoryRepository.findById(productDTO.categoryId());
		
		product.setCategory(category.get());
		
		return product;
	}
	
	public Product findProductById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		
		return product.orElseThrow(() -> new IdNotFoundException("Product"));
	}
	
	public List<Product> findAllProducts(){
		return productRepository.findAll();
	}
	
	public List<Product> findAllByName(String name){
		return productRepository.findAllByNameIgnoreCase(name);
	}
	
	public List<Product> findAllByCategory(Long categoryId){
		return productRepository.findAllByCategoryId(categoryId);
	}
	
	public void deleteProduct(Long id){
		Product product = this.findProductById(id);
		
		productRepository.delete(product);
	}
}
