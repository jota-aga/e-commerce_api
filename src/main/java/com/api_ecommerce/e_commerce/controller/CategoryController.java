package com.api_ecommerce.e_commerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.category.Category;
import com.api_ecommerce.e_commerce.models.category.CategoryRequest;
import com.api_ecommerce.e_commerce.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveCategory(@RequestBody CategoryRequest categoryDTO){
		Category category = new Category(categoryDTO.name());
		
		categoryService.saveCategory(category);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
