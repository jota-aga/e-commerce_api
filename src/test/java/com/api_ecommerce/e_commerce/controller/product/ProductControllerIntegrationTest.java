package com.api_ecommerce.e_commerce.controller.product;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.CategoryRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;
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
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	private Category category;
	
	private ProductRequest productRequest;
	
	@BeforeEach
	public void setUp() {
		category = RealDataCreator.createCategory(categoryRepository);
		
		productRequest = new ProductRequest("name", "description", new BigDecimal(100), category.getId(), 100, ProductStatus.UNAVAILABLE);
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
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)));
		
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isConflict());
	}
	
	public void deleteProduct_whenNoDependencies() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)));
		
		Optional<Product> optionalProduct = productRepository.findByName(productRequest.name());
		Product product = optionalProduct.get();
		
		mockMvc.perform(delete(BASE_URL+"/"+product.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
				.andExpect(status().isOk());
		
		optionalProduct = productRepository.findById(product.getId());
		
		assertTrue(optionalProduct.isEmpty());
	}
	
	public void deleteProduct_whenExistsCartItemsAndOrderItems() throws JsonProcessingException, Exception {
		mockMvc.perform(post(BASE_URL)
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)));
		
		Optional<Product> optionalProduct = productRepository.findByName(productRequest.name());
		Product product = optionalProduct.get();
		
		createOrderItemAndCartItemWithProduct(product);
		
		mockMvc.perform(delete(BASE_URL+"/"+product.getId())
				.with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
				.andExpect(status().isOk());
		
		optionalProduct = productRepository.findById(product.getId());
		
		assertTrue(optionalProduct.isEmpty());
		
		assertTrue(cartItemRepository.findAll().isEmpty());
		assertTrue(orderItemRepository.findAllByProductId(product.getId()).stream()
																		  .allMatch(orderItem -> orderItem.getProduct() == null));
	}
	
	private void createOrderItemAndCartItemWithProduct(Product product) {
		User user = RealDataCreator.createUserBuyer(roleRepository, userRepository);
		
		Buyer buyer = RealDataCreator.createBuyer(user, buyerRepository);
		
		Cart cart = RealDataCreator.createCart(buyer, cartRepository);
		
		CartItem cartItem = RealDataCreator.createCartItem(cartItemRepository, cart, product);
		
		Order order = RealDataCreator.createOrder(orderRepository, buyer);
		
		OrderItem orderItem = RealDataCreator.createOrderItem(orderItemRepository, order, product);
	}
}
