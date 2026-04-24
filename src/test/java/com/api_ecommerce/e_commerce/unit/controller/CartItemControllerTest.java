package com.api_ecommerce.e_commerce.unit.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

import com.api_ecommerce.e_commerce.controller.CartItemController;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.infra.SecurityConfiguration;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartItemController.class)
@Import(SecurityConfiguration.class)
public class CartItemControllerTest {
	
	private static String BASE_URL = "/cart-item";
	
	private static String ROLE = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private CartItemService cartItemService;
	
	private CartItemRequest cartItemRequest;
	
	@BeforeEach
	public void setUp() {
		cartItemRequest = new CartItemRequest(Long.MAX_VALUE, 5);
	}
	
	@Test
	public void addItemToCart_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL +"/"+ Long.MAX_VALUE)
			   .with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(cartItemRequest)))
			   .andExpect(status().isCreated());
	}
	
	@Test
	public void addItemToCart_WhenNotFoundException() throws JsonProcessingException, Exception {
		doThrow(new NotFoundException("not found")).when(cartItemService).addItemToCart(cartItemRequest, Long.MAX_VALUE);
		
		mockMvc.perform(post(BASE_URL +"/"+ Long.MAX_VALUE)
			   .with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(cartItemRequest)))
			   .andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteCartItem_Sucess() throws JsonProcessingException, Exception {
		
		mockMvc.perform(delete(BASE_URL +"/"+ Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
			   .andExpect(status().isOk());
		
		verify(cartItemService, atLeastOnce()).deleteCartItem(Long.MAX_VALUE);

	}
	
	@Test
	public void deleteCartItem_whenIsNotTheAdmin() throws JsonProcessingException, Exception {
		doThrow(new NotAuthorizedException("not authorized")).when(cartItemService).deleteCartItem(Long.MAX_VALUE);
		
		mockMvc.perform(delete(BASE_URL +"/"+ Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
			   .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void updateCartItem_Sucess() throws JsonProcessingException, Exception {
		
		mockMvc.perform(put(BASE_URL +"/"+ Long.MAX_VALUE)
			   .with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(cartItemRequest)))
			   .andExpect(status().isOk());
		
		verify(cartItemService, atLeastOnce()).updateCartItem(Long.MAX_VALUE, cartItemRequest);
	}
	
	@Test
	public void updateCartItem_WhenNotFound() throws JsonProcessingException, Exception {
		doThrow(new NotFoundException("not found")).when(cartItemService).updateCartItem(Long.MAX_VALUE, cartItemRequest);

		
		mockMvc.perform(put(BASE_URL +"/"+ Long.MAX_VALUE)
			   .with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(cartItemRequest)))
			   .andExpect(status().isNotFound());
	}
	
	@Test
	public void updateCartItem_WhenNotAuthorized() throws JsonProcessingException, Exception {
		doThrow(new NotAuthorizedException("not authorized")).when(cartItemService).updateCartItem(Long.MAX_VALUE, cartItemRequest);

		
		mockMvc.perform(put(BASE_URL +"/"+ Long.MAX_VALUE)
			   .with(jwt().authorities(new SimpleGrantedAuthority(ROLE)))
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(cartItemRequest)))
			   .andExpect(status().isUnauthorized());
	}
}
