package com.api_ecommerce.e_commerce.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class CartServiceTest {
	
	@InjectMocks
	private CartService cartService;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private OrderRepository orderRepository;
	
	@Mock
	private BuyerRepository buyerRepository;
	
	@Mock
	private SecurityService securityService;
	
	private Category category;
	private Product product;
	private Role role;
	private User user;
	private Buyer buyer;
	private Cart cart;
	private CartItem cartItem1;
	private CartItem cartItem2;
	
	
	@BeforeEach
	public void setUp() {
		category = new Category("Category");
		product = new Product("Product", "Description", 100, new BigDecimal(50), category, ProductStatus.DISPONIVEL);
		role = new Role(Role.Value.BUYER.name());
		user = new User(1L, "username", "password", role);
		buyer = new Buyer("name", LocalDate.now().minusYears(20), "11237419484", "adress", user);
		
		cart = new Cart(buyer);
	}
	
	
	@Test
	public void checkoutForUserAuthenticatedSucess() {
		
	}
	
}
