package com.api_ecommerce.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.product.ProductDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.mapper.Mappers;
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
	
	@PostMapping()
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> saveProduct(@Valid @RequestBody ProductDTO productRequest){
		
		Category category = categoryService.findCategoryById(productRequest.categoryId());
		Product product = new Product(productRequest.name(), productRequest.description(), 
									  productRequest.quantity(), productRequest.price(), category);
		productService.saveProduct(product);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping()
	public ResponseEntity<List<ProductDTO>> findAllProducts(){
		List<Product> products = productService.findAllProducts();
		
		List<ProductDTO> productsResponse = Mappers.toDTO(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@GetMapping("/search-name")
	public ResponseEntity<List<ProductDTO>> findAllProductsByName(@RequestParam("name") String name){
		List<Product> products = productService.findAllByName(name);
		
		List<ProductDTO> productsResponse = Mappers.toDTO(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@GetMapping("/search-category")
	public ResponseEntity<List<ProductDTO>> findAllProductsByCategory(@RequestParam String categoryName){
		Category category = categoryService.findCategoryByName(categoryName);
		List<Product> products = productService.findAllByCategory(category.getId());
		List<ProductDTO> productsResponse = Mappers.toDTO(products);
		
		return ResponseEntity.status(HttpStatus.OK).body(productsResponse);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> deleteProductById(@PathVariable Long id){
		productService.deleteProduct(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> editProductById(@PathVariable Long id,  @Valid @RequestBody ProductDTO productDTO){
		Product product = productService.findProductById(id);
		
		product = productService.editProduct(product, productDTO);
		
		productService.saveProduct(product);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
