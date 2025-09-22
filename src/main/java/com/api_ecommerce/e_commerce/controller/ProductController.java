package com.api_ecommerce.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.service.CategoryService;
import com.api_ecommerce.e_commerce.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping()
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
	public ResponseEntity<List<Product>> findAllProductsByName(@RequestParam("name") String name){
		List<Product> products = productService.findAllByName(name);
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@GetMapping("/search-category")
	public ResponseEntity<List<Product>> findAllProductsByCategory(@RequestParam String categoryName){
		Category category = categoryService.findCategoryByName(categoryName);
		List<Product> products = productService.findAllByCategory(category.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductById(@PathVariable Long id){
		productService.deleteProduct(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> editProductById(@PathVariable Long id,  @RequestBody ProductRequest productDTO){
		Product product = productService.findProductById(id);
		
		product = productService.editProduct(product, productDTO);
		
		productService.saveProduct(product);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
