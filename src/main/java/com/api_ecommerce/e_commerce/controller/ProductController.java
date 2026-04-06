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
import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.mapper.ProductMapper;
import com.api_ecommerce.e_commerce.service.CategoryService;
import com.api_ecommerce.e_commerce.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductRequest productRequest){
		productService.createProduct(productRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> findAllProducts(){
		List<Product> products = productService.findAllProducts();
		
		List<ProductResponse> productsResponse = ProductMapper.INSTANCE.listProductToListProductResponse(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@GetMapping("/name")
	public ResponseEntity<List<ProductResponse>> findAllProductsByName(@RequestParam("name") String name){
		List<Product> products = productService.findAllByName(name);
				
		List<ProductResponse> productsResponse = ProductMapper.INSTANCE.listProductToListProductResponse(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<ProductResponse>> findAllProductsByCategory(@RequestParam String categoryName){
		List<Product> products = productService.findAllByCategory(categoryName);
		
		List<ProductResponse> productsResponse = ProductMapper.INSTANCE.listProductToListProductResponse(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductById(@PathVariable Long id){
		productService.deleteProduct(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> editProductById(@PathVariable Long id,  @Valid @RequestBody ProductRequest productRequest){
	
		productService.updateProduct(id, productRequest);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
