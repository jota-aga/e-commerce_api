package com.api_ecommerce.e_commerce.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.creator.RealDataCreator;
import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerIntegrationTest {
	
	public static String BASE_URL = "/category";
	
	public static String ROLE = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private CategoryDTO dto;
	
	@BeforeEach
	public void setUp() {
		dto = new CategoryDTO("category");
	}
	
	@Test
	public void saveCategory_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated());
				
		Optional<Category> optionalCategory = categoryRepository.findByName(dto.name());
		
		assertTrue(optionalCategory.isPresent());
	}
	
	@Test
	public void saveCategory_WhenNameAlreadyExists() throws JsonProcessingException, Exception {
		RealDataCreator.createCategory(categoryRepository);
		
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict());
		
		List<Category> categorys = categoryRepository.findAll();
		
		assertEquals(1, categorys.size());
	}
	
	@Test
	public void updateCategory_Sucess() throws JsonProcessingException, Exception {
		Category category = RealDataCreator.createCategory(categoryRepository);
		
		dto = new CategoryDTO("new name");
		
		mockMvc.perform(put(BASE_URL+"/"+category.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk());
		
		category = categoryRepository.findById(category.getId()).get();
		
		assertEquals(dto.name(), category.getName());
	}
	
	@Test
	public void updateCategory_WhenNameAlreadyExists() throws JsonProcessingException, Exception {
		Category category = RealDataCreator.createCategory(categoryRepository);
		
		dto = new CategoryDTO("repeated name");
		
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated());
		
		mockMvc.perform(put(BASE_URL+"/"+category.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void updateCategory_WhenNotFound() throws JsonProcessingException, Exception {		
		mockMvc.perform(put(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteCategory_Sucess() throws JsonProcessingException, Exception {
		Category category = RealDataCreator.createCategory(categoryRepository);
				
		mockMvc.perform(delete(BASE_URL+"/"+category.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
				.andExpect(status().isOk());
		
		Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
		
		assertTrue(optionalCategory.isEmpty());
	}
	
	@Test
	public void deleteCategory_ThatContainsProducts() throws JsonProcessingException, Exception {
		Category category = RealDataCreator.createCategory(categoryRepository);
		Product product = RealDataCreator.createProduct(category, productRepository);
				
		mockMvc.perform(delete(BASE_URL+"/"+category.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
				.andExpect(status().isOk());
		
		Optional<Category> optionalCategory = categoryRepository.findById(category.getId());
		product = productRepository.findById(product.getId()).get();
		
		assertTrue(optionalCategory.isEmpty());
		assertEquals(null, product.getCategory());
	}
	
	@Test
	public void deleteCategory_WhenNotFound() throws JsonProcessingException, Exception {
				
		mockMvc.perform(delete(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
				.andExpect(status().isNotFound());
	}
}
