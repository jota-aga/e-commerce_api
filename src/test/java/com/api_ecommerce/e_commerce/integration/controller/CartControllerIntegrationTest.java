package com.api_ecommerce.e_commerce.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.creator.RealDataCreator;
import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.mapper.CartMapper;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
import com.api_ecommerce.e_commerce.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CartControllerIntegrationTest {
	
	private static String BASE_URL = "/cart";
	
	private static String SCOPE_ROLE = "SCOPE_ADMIN";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CartService cartService;
	
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
	
	@Autowired
	private OrderRepository orderRepository;
	
	private Cart cart;
	
	private CartItem cartItem;
	
	private Product product;
	
	private Category category;
	
	private User userAdmin;
	
	private Buyer buyer;
		
	@BeforeEach
	public void setUp() {
		userAdmin = userRepository.findByRoleName(Role.Value.ADMIN.name()).get();
		buyer = RealDataCreator.createBuyer(userAdmin, buyerRepository);
		cart = RealDataCreator.createCart(buyer, cartRepository);
	}
	
	@Test
	public void findCartById_Sucess() throws JsonProcessingException, Exception {
		CartAdminResponse response = CartMapper.INSTANCE.cartToAdminCartResponse(cart);
		
		mockMvc.perform(get(BASE_URL+"/"+cart.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(SCOPE_ROLE))))
				.andExpect(content().json(objectMapper.writeValueAsString(response)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void findCartById_NotFound() throws JsonProcessingException, Exception {
		
		mockMvc.perform(get(BASE_URL+"/"+Long.MAX_VALUE)
				.with(jwt().authorities(new SimpleGrantedAuthority(SCOPE_ROLE))))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void checkoutCart_Sucess() throws JsonProcessingException, Exception {
		category = RealDataCreator.createCategory(categoryRepository);
		product = RealDataCreator.createProduct(category, productRepository);
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);

		mockMvc.perform(post(BASE_URL+"/"+buyer.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(SCOPE_ROLE))))
				.andExpect(status().isOk());
		
		List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
		List<Order> orders = orderRepository.findAll();
		List<OrderItem> items = orders.getFirst().getOrderItems();
		
		assertTrue(cartItems.isEmpty());
		assertTrue(items.getFirst().getProduct().equals(product));
	}
	
	@Test
	public void checkoutCart_WhenCartIsEmpty() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL+"/"+buyer.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(SCOPE_ROLE))))
				.andExpect(status().isConflict());
		
		List<Order> orders = orderRepository.findAll();
		
		assertTrue(orders.isEmpty());
	}
	
	@Test
	public void checkoutCart_WhenProductIsUnavailable() throws JsonProcessingException, Exception {
		category = RealDataCreator.createCategory(categoryRepository);
		
		//setting product status
		product = RealDataCreator.createProduct(category, productRepository);
		product.setStatus(ProductStatus.UNAVAILABLE);
		productRepository.save(product);
		
		cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		
		mockMvc.perform(post(BASE_URL+"/"+buyer.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority(SCOPE_ROLE))))
				.andExpect(status().isConflict());
		
		List<Order> orders = orderRepository.findAll();
		
		assertTrue(orders.isEmpty());
	}
}
