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
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.mapper.CategoryMapper;
import com.api_ecommerce.e_commerce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDTO categoryDTO){
		categoryService.createCategory(categoryDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAllCategory(){
		List<Category> categorys = categoryService.findAllCategory();
		
		List<CategoryDTO> categorysResponse = CategoryMapper.toListDTO(categorys);
		
		return ResponseEntity.status(HttpStatus.OK).body(categorysResponse);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> editCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryRequest){
		categoryService.editCategory(id, categoryRequest);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id){
		categoryService.deleteCategory(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
