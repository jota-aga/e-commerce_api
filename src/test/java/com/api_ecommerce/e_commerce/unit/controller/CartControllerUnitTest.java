package com.api_ecommerce.e_commerce.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.contentOf;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.api_ecommerce.e_commerce.controller.CartController;
import com.api_ecommerce.e_commerce.creator.CartCreator;
import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.infra.SecurityConfiguration;
import com.api_ecommerce.e_commerce.mapper.CartMapper;
import com.api_ecommerce.e_commerce.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
@Import(SecurityConfiguration.class)
public class CartControllerUnitTest {
	
	private static String BASE_URL = "/cart";
	
	private static String ROLE = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private CartService cartService;
	
	private Cart cart;
	
	@BeforeEach
	public void setUp() {
		cart = CartCreator.cartWithBuyer();
	}
	
	@Test
	public void findCartById_Sucess() throws Exception {
		when(cartService.findCartById(cart.getId())).thenReturn(cart);
		CartAdminResponse dto = CartMapper.INSTANCE.cartToAdminCartResponse(cart);
		
		mockMvc.perform(get(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(dto)));			
	}
	
	@Test
	public void findCartById_WhenNotFound() throws Exception {
		doThrow(new NotFoundException("not found")).when(cartService).findCartById(cart.getId());
		
		mockMvc.perform(get(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE))))
				.andExpect(status().isNotFound());
				
	}
}
