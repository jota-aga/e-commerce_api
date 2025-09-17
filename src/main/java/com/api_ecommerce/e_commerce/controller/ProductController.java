package com.api_ecommerce.e_commerce.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.category.Category;
import com.api_ecommerce.e_commerce.models.product.Product;
import com.api_ecommerce.e_commerce.models.product.ProductRequest;
import com.api_ecommerce.e_commerce.service.CategoryService;
import com.api_ecommerce.e_commerce.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveProduct(@RequestBody ProductRequest productRequest){
		
		Category category = categoryService.findCategoryById(productRequest.getCategoryId());
		Product product = new Product(productRequest.getName(), productRequest.getDescription(), 
									  productRequest.getQuantity(), productRequest.getPrice(), category);
		productService.saveProduct(product);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping()
	public ResponseEntity<List<Product>> findAllProducts(){
		List<Product> products = productService.findAllProducts();
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/search-name")
	public ResponseEntity<List<Product>> findAllProductsByName(@RequestParam String name){
		List<Product> products = productService.findAllByName(name);
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/search-category")
	public ResponseEntity<List<Product>> findAllProductsByCategory(@RequestParam Long categoryId){
		List<Product> products = productService.findAllByCategory(categoryId);
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
}
