package com.api_ecommerce.e_commerce.controller.product;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.api_ecommerce.e_commerce.controller.ProductController;
import com.api_ecommerce.e_commerce.creator.ProductCreator;
import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.infra.SecurityConfiguration;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@Import(SecurityConfiguration.class)
public class ProductControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private ProductService productService;
	
	private ProductRequest productRequest;
	
	private Product productAvailable;
	
	private Product productUnavailable;
	
	@BeforeEach
	public void setUp() {
		productRequest = ProductCreator.availableProductRequest();
		productAvailable = ProductCreator.productAvaliable();
		productUnavailable = ProductCreator.productUnavaliable();
	}
	
	@Test
	public void createProduct_Sucess() throws Exception {
		mockMvc.perform(post("/product")
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isCreated());
		
		verify(productService).createProduct(productRequest);
	}
	
	@Test
	public void createProduct_whenNameIsRepeated() throws Exception {
		doThrow(new ConflictException("message")).when(productService).createProduct(productRequest);
		
		mockMvc.perform(post("/product")
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void findProductsByName() throws Exception {
		String paramNameValue = productAvailable.getName();
		when(productService.findAllByName(paramNameValue)).thenReturn(List.of(productAvailable, productUnavailable));
		
		mockMvc.perform(get("/product/name")
				.param("name", paramNameValue))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void findProductsByCategory() throws Exception {
		String paramCategoryValue = productAvailable.getCategory().getName();
		when(productService.findAllByCategory(paramCategoryValue)).thenReturn(List.of(productAvailable, productUnavailable));
		
		mockMvc.perform(get("/product/category")
				.param("categoryName", paramCategoryValue))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void updateProduct_Sucess() throws Exception {
		
		mockMvc.perform(put("/product/"+productAvailable.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void updateProduct_WhenProductOrCategoryNotFound() throws Exception {
		doThrow(new NotFoundException("message")).when(productService).updateProduct(productAvailable.getId(), productRequest);
		
		mockMvc.perform(put("/product/"+productAvailable.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isNotFound());
	}
}
