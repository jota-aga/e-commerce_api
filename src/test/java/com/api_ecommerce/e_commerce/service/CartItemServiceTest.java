package com.api_ecommerce.e_commerce.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class CartItemServiceTest {
	
	@InjectMocks
	private CartItemService cartItemService;
	
	@Mock
	private CartItemRepository cartItemRepository;
	
	@Mock
	private CartRepository cartRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private BuyerRepository buyerRepository;
	
	private Category category;
	private Product product;
	private Role role;
	private User user;
	private Buyer buyer;
	private Cart cart;
	
	@BeforeEach
	public void setUp() {
		Category category = new Category("Category");
		Product product = new Product("Product", "Description", 100, new BigDecimal(50), category, ProductStatus.DISPONIVEL);
		Role role = new Role(Role.Value.BUYER.name());
		User user = new User("username", "password", role);
		Buyer buyer = new Buyer("name", LocalDate.now().minusYears(20), "11237419484", "adress", user);
		Cart cart = new Cart(buyer);
	}
	
	@Test
	public void deleteCartItemSucess() {
		CartItem cartItem = new CartItem(product, 1, cart);
		
		when(cartItemRepository.findById((any()))).thenReturn(Optional.of(cartItem));
		
	}
}
