package com.api_ecommerce.e_commerce.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.LoginResponse;
import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.infra.SecurityConfiguration;
import com.api_ecommerce.e_commerce.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private AuthService authService;
	
	private RegisterBuyerRequest dto;
	private LoginRequest login;
	
	@BeforeEach
	public void setUp() {
		dto = new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
		login = new LoginRequest("username", "password");
	}
	
	@Test
	public void registerBuyer_Sucess() throws Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated());
		
		verify(authService, atLeastOnce()).registerBuyer(dto);
	}
	
	@Test
	public void registerBuyer_whenThrowsConflictException() throws Exception {
		doThrow(new ConflictException("Conflict"))
		.when(authService)
		.registerBuyer(dto);
		
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict());
		
	}
	
	@Test
	public void doLogin_Sucess() throws Exception {
		when(authService.doLogin(login)).thenReturn(new LoginResponse("token"));
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.acessToken").value("token"));
	}
	
	@Test
	public void doLogin_whenThrowsNotAuthorizedException() throws Exception {
		when(authService.doLogin(login)).thenThrow(new NotAuthorizedException("Username or Password Incorrect"));
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
				.andExpect(status().isUnauthorized());
	}
}
