package com.api_ecommerce.e_commerce.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {
	
	private static String BASE_URL = "/product";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Category category;
	
	private ProductRequest productRequest;
	
	@BeforeEach
	public void setUp() {
		category = RealDataCreator.createCategory(categoryRepository);
		
		productRequest = new ProductRequest("product", "description", new BigDecimal(100), category.getId(), 100, ProductStatus.UNAVAILABLE);
	}
	
	@Test
	public void createProduct_Sucess() throws Exception {
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isCreated());
		
		Optional<Product> optionalProduct = productRepository.findByName(productRequest.name());
		
		assertTrue(optionalProduct.isPresent());
	}
	
	@Test
	public void createProduct_whenNameIsRepeated() throws Exception {
		RealDataCreator.createProduct(category, productRepository);
		
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void updateProduct_Sucess() throws JsonProcessingException, Exception {
		Product product = RealDataCreator.createProduct(category, productRepository);
		
		mockMvc.perform(put(BASE_URL+"/"+product.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void updateProduct_whenNameAlreadyExists() throws JsonProcessingException, Exception {
		Product productToBeUpdated = RealDataCreator.createProduct(category, productRepository);
		
		ProductRequest requestWithRepeatedName = new ProductRequest("product different", "description", new BigDecimal(100), 
				category.getId(), 100, ProductStatus.UNAVAILABLE);
		
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestWithRepeatedName)))
				.andExpect(status().isCreated());
		
		mockMvc.perform(put(BASE_URL+"/"+productToBeUpdated.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(requestWithRepeatedName)))
				.andExpect(status().isConflict());
	}
	
	
}
