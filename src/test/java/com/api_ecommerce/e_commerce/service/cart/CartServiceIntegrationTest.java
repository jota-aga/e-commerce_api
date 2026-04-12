package com.api_ecommerce.e_commerce.service.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.SecurityService;

@SpringBootTest
@Transactional
public class CartServiceIntegrationTest {

	@Autowired
	private CartService cartService;

	@MockitoBean
	private SecurityService securityService;

	@Autowired
	private BuyerRepository buyerRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	private Role role;

	private User user;

	private Buyer buyer;

	private Cart cart;

	private Product product;

	private CartItem cartItem1;

	private CartItem cartItem2;

	@BeforeEach
	public void setUp() {
		role = roleRepository.findRoleByName(Role.Value.BUYER.name()).get();

		user = User.builder().username("username").password("password").role(role).build();
		buyer = Buyer.builder().name("name").adress("adress").birthday(LocalDate.now().minusYears(20))
				.cpf("11237419484").user(user).build();
		buyer = buyerRepository.save(buyer);

		cart = Cart.builder().buyer(buyer).build();
		cart = cartRepository.save(cart);

		product = Product.builder().name("name").description("description").build();
		product = productRepository.save(product);
	}

	@Test
	public void checkoutByBuyerId() {
		cartItem1 = CartItem.builder().cart(cart).product(product).quantity(1).build();
		cartItem1 = cartItemRepository.save(cartItem1);

		cartItem2 = CartItem.builder().cart(cart).product(product).quantity(2).build();
		cartItem2 = cartItemRepository.save(cartItem2);

		cartService.checkoutByBuyerId(buyer.getId());

		List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
		assertTrue(items.isEmpty());

		List<Order> order = orderRepository.findAllOrderByBuyerId(buyer.getId());
		List<OrderItem> itemsOfFirstOrder = order.getFirst().getOrderItems();

		assertFalse(order.isEmpty());
		assertEquals(2, itemsOfFirstOrder.size());
	}

	@Test
	public void checkoutByBuyerIdWhenCartIsEmpty() {
		cart.setCartItems(List.of());

		assertThrows(ConflictException.class, () -> cartService.checkoutByBuyerId(buyer.getId()));
	}

	@Test
	public void checkoutForCurrentUser() {
		cartItem1 = CartItem.builder().cart(cart).product(product).quantity(1).build();
		cartItem1 = cartItemRepository.save(cartItem1);

		cartItem2 = CartItem.builder().cart(cart).product(product).quantity(2).build();
		cartItem2 = cartItemRepository.save(cartItem2);
		when(securityService.getCurrentUser()).thenReturn(user);

		cartService.checkoutForUserAuthenticated();

		List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
		assertTrue(items.isEmpty());

		List<Order> order = orderRepository.findAllOrderByBuyerId(buyer.getId());
		List<OrderItem> itemsOfFirstOrder = order.getFirst().getOrderItems();

		assertFalse(order.isEmpty());
		assertEquals(2, itemsOfFirstOrder.size());
	}

	@Test
	public void checkoutForCurrentUserWhenCartIsEmpty() {
		cart.setCartItems(List.of());
		when(securityService.getCurrentUser()).thenReturn(user);

		assertThrows(ConflictException.class, () -> cartService.checkoutForUserAuthenticated());
	}
}
