package com.api_ecommerce.e_commerce.unit.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.api_ecommerce.e_commerce.controller.CategoryController;
import com.api_ecommerce.e_commerce.creator.CategoryCreator;
import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.infra.SecurityConfiguration;
import com.api_ecommerce.e_commerce.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
@Import(SecurityConfiguration.class)
public class CategoryControllerTest {
	
	public static String BASE_URL = "/category";
	
	public static String role = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private CategoryService categoryService;
	
	private Category category;
	
	private CategoryDTO categoryDTO;
	
	@BeforeEach
	public void setUp() {
		category = CategoryCreator.simpleCategory();
		categoryDTO = new CategoryDTO("category dto");
	}
	
	@Test
	public void createCategory_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL)
						.with(jwt().authorities(new SimpleGrantedAuthority(role)))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(categoryDTO)))
						.andExpect(status().isCreated());
		
		verify(categoryService, atLeastOnce()).createCategory(categoryDTO);
	}
	
	@Test
	public void createCategory_WhenRepeatedName() throws JsonProcessingException, Exception {
		doThrow(new ConflictException("name alread exists")).when(categoryService).createCategory(categoryDTO);
		
		mockMvc.perform(post(BASE_URL)
						.with(jwt().authorities(new SimpleGrantedAuthority(role)))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(categoryDTO)))
						.andExpect(status().isConflict());
	}
	
	@Test
	public void updateCategory_Sucess() throws JsonProcessingException, Exception {
		when(categoryService.findCategoryById(category.getId())).thenReturn(category);
		
		mockMvc.perform(put(BASE_URL + "/" + category.getId())
						.with(jwt().authorities(new SimpleGrantedAuthority(role)))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(categoryDTO)))
						.andExpect(status().isOk());
		
		verify(categoryService, atLeastOnce()).updateCategory(category.getId(), categoryDTO);
	}
	
	@Test
	public void updateCategory_whenNotFound() throws JsonProcessingException, Exception {
		doThrow(new NotFoundException("category id")).when(categoryService).updateCategory(category.getId(), categoryDTO);
		
		mockMvc.perform(put(BASE_URL + "/" + category.getId())
						.with(jwt().authorities(new SimpleGrantedAuthority(role)))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(categoryDTO)))
						.andExpect(status().isNotFound());		
	}
	
	@Test
	public void updateCategory_whenNameAlreadyExists() throws JsonProcessingException, Exception {
		doThrow(new ConflictException("name already exists")).when(categoryService).updateCategory(category.getId(), categoryDTO);
		
		mockMvc.perform(put(BASE_URL + "/" + category.getId())
						.with(jwt().authorities(new SimpleGrantedAuthority(role)))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(categoryDTO)))
						.andExpect(status().isConflict());		
	}
	
	@Test
	public void deleteCategory_Sucess() throws JsonProcessingException, Exception {
		when(categoryService.findCategoryById(category.getId())).thenReturn(category);
		
		mockMvc.perform(delete(BASE_URL + "/" + category.getId())
						.with(jwt().authorities(new SimpleGrantedAuthority(role))))
						.andExpect(status().isOk());
		
		verify(categoryService, atLeastOnce()).deleteCategory(category.getId());
	}
	
	@Test
	public void deleteCategory_NotFound() throws JsonProcessingException, Exception {
		when(categoryService.findCategoryById(category.getId())).thenThrow(new NotFoundException("category id"));
		
		mockMvc.perform(delete(BASE_URL + "/" + category.getId())
						.with(jwt().authorities(new SimpleGrantedAuthority(role))))
						.andExpect(status().isOk());
		
		verify(categoryService, atLeastOnce()).deleteCategory(category.getId());
	}
}
