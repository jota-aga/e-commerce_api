package com.api_ecommerce.e_commerce.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.creator.RealDataCreator;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CartItemControllerIntegrationTest {
	
	private static String BASE_URL = "/cart-item";
	
	private static String ROLE_ADMIN = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private SecurityService securityService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	private Cart cart;
	
	private CartItem cartItem;
	
	private Product product;
	
	private Category category;
	
	private User userAdmin;
	
	private CartItemRequest cartItemRequest;
	
	@BeforeEach
	public void setUp() {
		userAdmin = userRepository.findByRoleName(Role.Value.ADMIN.name()).get();
		Buyer buyer = RealDataCreator.createBuyer(userAdmin, buyerRepository);
		cart = RealDataCreator.createCart(buyer, cartRepository);
		category = RealDataCreator.createCategory(categoryRepository);
		product = RealDataCreator.createProduct(category, productRepository);
		cartItemRequest = new CartItemRequest(product.getId(), 5);
	}
	
	@Test
	public void addItemToCart_Sucess() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isCreated());
		
		List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
		
		assertTrue(!cartItems.isEmpty());
	}
	
	@Test
	public void addItemToCart_WhenCartNotFound() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isNotFound());
		
		List<CartItem> cartItems = cartItemRepository.findAll();
		assertTrue(cartItems.isEmpty());
	}
	
	@Test
	public void addItemToCart_WhenProductNotFound() throws JsonProcessingException, Exception {
		cartItemRequest = new CartItemRequest(Long.MAX_VALUE, 10);
		
		mockMvc.perform(post(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isNotFound());
		
		List<CartItem> cartItems = cartItemRepository.findAll();
		assertTrue(cartItems.isEmpty());
	}
	
	@Test
	public void addItemToCart_WhenQuantityIsLessThanOne() throws JsonProcessingException, Exception {
		cartItemRequest = new CartItemRequest(product.getId(), 0);
		
		mockMvc.perform(post(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isBadRequest());
		
		List<CartItem> cartItems = cartItemRepository.findAll();
		assertTrue(cartItems.isEmpty());
	}
	
	@Test
	public void addItemToCart_WhenProductIsUnavailable() throws JsonProcessingException, Exception {
		product.setStatus(ProductStatus.UNAVAILABLE);
		productRepository.save(product);
		
		mockMvc.perform(post(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isConflict());
		
		List<CartItem> cartItems = cartItemRepository.findAll();
		assertTrue(cartItems.isEmpty());
	}
	
	@Test
	public void deleteCartItem_Sucess() throws JsonProcessingException, Exception {
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		when(securityService.getCurrentUser()).thenReturn(userAdmin);
		
		mockMvc.perform(delete(BASE_URL+"/"+cartItem.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN))))
				.andExpect(status().isOk());
		
		Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItem.getId());
		
		assertTrue(optionalCartItem.isEmpty());
	}
	
	@Test
	public void editCartItem_Sucess() throws JsonProcessingException, Exception {
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		when(securityService.getCurrentUser()).thenReturn(userAdmin);

		Product newProduct = Product.builder()
									.name("name")
									.category(category)
									.description("description")
									.price(new BigDecimal(50))
									.quantity(60)
									.status(ProductStatus.AVAILABLE)
									.build();
		newProduct = productRepository.save(newProduct);
		
		cartItemRequest = new CartItemRequest(newProduct.getId(), 50);
		
		mockMvc.perform(put(BASE_URL+"/"+cartItem.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isOk());
		
		cartItem = cartItemRepository.findById(cartItem.getId()).get();
		
		assertEquals(cartItem.getProduct(), newProduct);
		assertEquals(cartItem.getQuantity(), cartItemRequest.quantity());
	}
	
	@Test
	public void editCartItem_WhenItemNotFound() throws JsonProcessingException, Exception {
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		when(securityService.getCurrentUser()).thenReturn(userAdmin);
				
		mockMvc.perform(put(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isNotFound());
		
		cartItem = cartItemRepository.findById(cartItem.getId()).get();
		
		assertEquals(cartItem.getProduct(), product);
		assertEquals(cartItem.getQuantity(), cartItemRequest.quantity());
	}
	
	@Test
	public void editCartItem_WhenProductNotFound() throws JsonProcessingException, Exception {
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		when(securityService.getCurrentUser()).thenReturn(userAdmin);
		
		CartItemRequest invalidRequest = new CartItemRequest(Long.MAX_VALUE, 50);
				
		mockMvc.perform(put(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isNotFound());
		
		cartItem = cartItemRepository.findById(cartItem.getId()).get();
		
		assertEquals(cartItem.getProduct(), product);
		assertEquals(cartItem.getQuantity(), cartItemRequest.quantity());
	}
	
	@Test
	public void editCartItem_WhenProductIsUnavailable() throws JsonProcessingException, Exception {
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		when(securityService.getCurrentUser()).thenReturn(userAdmin);

		Product newProduct = Product.builder()
				.name("name")
				.category(category)
				.description("description")
				.price(new BigDecimal(50))
				.quantity(60)
				.status(ProductStatus.UNAVAILABLE)
				.build();
		newProduct = productRepository.save(newProduct);
		
		cartItemRequest = new CartItemRequest(newProduct.getId(), 10);
				
		mockMvc.perform(put(BASE_URL+"/"+cartItem.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_ADMIN)))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cartItemRequest)))
				.andExpect(status().isConflict());
		
		cartItem = cartItemRepository.findById(cartItem.getId()).get();
		
		assertNotEquals(cartItem.getProduct(), newProduct);
	}
}
