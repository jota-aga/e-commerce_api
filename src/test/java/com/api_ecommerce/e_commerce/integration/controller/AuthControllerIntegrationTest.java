package com.api_ecommerce.e_commerce.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.creator.UserCreator;
import com.api_ecommerce.e_commerce.dto.user.LoginRequest;
import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	private RegisterBuyerRequest buyerRequest;
	private LoginRequest loginRequest;
	
	@BeforeEach
	public void setUp() {
		buyerRequest = UserCreator.registerBuyerRequest();
		loginRequest = new LoginRequest(buyerRequest.username(), buyerRequest.password());
	}
	
	@Test
	public void registerBuyer_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)))
				.andExpect(status().isCreated());
		
		Optional<User> optionalUser = userRepository.findByUsername(buyerRequest.username());
		Optional<Buyer> optionalBuyer = buyerRepository.findByCpf(buyerRequest.cpf());
		assertTrue(optionalUser.isPresent());
		assertTrue(optionalBuyer.isPresent());
		
		Optional<Cart> optionalCart = cartRepository.findByBuyerId(optionalBuyer.get().getId());
		assertTrue(optionalCart.isPresent());
	}
	
	@Test
	public void registerBuyer_whenUsernameAlreadyExists() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)));
		
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)))
				.andExpect(status().isConflict())
				.andExpect(content().string("Username already registered"));;
	}
	
	@Test
	public void registerBuyer_whenCpfAlreadyExists() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)));
		
		RegisterBuyerRequest differentBuyerRequest = new RegisterBuyerRequest("username diff", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");

		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(differentBuyerRequest)))
				.andExpect(status().isConflict())
				.andExpect(content().string("CPF already registered"));
	}
	
	@Test
	public void doLogin_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)));
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.acessToken").exists());			
	}
	
	@Test
	public void doLogin_WhenUsernameIsIncorrect() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)));
		
		LoginRequest wrongLogin = new LoginRequest("different username", buyerRequest.password());
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongLogin)))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void doLogin_WhenPasswordIsIncorrect() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(buyerRequest)));
		
		LoginRequest wrongLogin = new LoginRequest(buyerRequest.username(), "wrong password");
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(wrongLogin)))
				.andExpect(status().isUnauthorized());
	}
}
