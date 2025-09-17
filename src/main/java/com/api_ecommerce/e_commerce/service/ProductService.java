package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.ProductAlreadyExistsException;
import com.api_ecommerce.e_commerce.models.product.Product;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	ProductRepository productRepository;
	
	public void saveProduct(Product product) {
		
		if(product.getId() != null) {
			Optional<Product> productRepeat = productRepository.findByName(product.getName());
			if(productRepeat.isPresent()) {
				throw new ProductAlreadyExistsException();
			}
		}
		productRepository.save(product);
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
}
