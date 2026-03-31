package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
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
	private Product product1;
	private Product product2;
	private Role role;
	private User user;
	private Buyer buyer;
	private Cart cart;
	private CartItem ci1;
	private CartItem ci2;
	
	
	
	@BeforeEach
	public void setUp() {
		category = new Category("Category");
		product1 = new Product("Product1", "Description", 100, new BigDecimal(50), category, ProductStatus.AVAILABLE);
		product2 = new Product("Product2", "Description", 60, new BigDecimal(7), category, ProductStatus.AVAILABLE);
		role = new Role(Role.Value.BUYER.name());
		user = new User(1L, "username", "password", role);
		buyer = new Buyer(1L, "name", LocalDate.now().minusYears(20), "11237419484", "adress", user);
		ci1 = new CartItem(product1, 1, cart);
		ci2 = new CartItem(product1, 2, cart);
		cart = new Cart(1L, buyer, null);
	}
	
	@Test
	public void findCartByIdSucess() {
		when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
		
		Cart cart = cartService.findCartById(1L);
		
		assertNotNull(cart);
	}
	
	@Test
	public void findCartByIdNotFound() {
		when(cartRepository.findById(any())).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> cartService.findCartById(any()));
	}
	
	@Test
	public void findCartByBuyerIdSucess() {
		when(cartRepository.findByBuyerId(1L)).thenReturn(Optional.of(cart));
		
		Cart cart = cartService.findCartByBuyerId(1L);
		
		assertNotNull(cart);
	}
	
	@Test
	public void findCartByBuyerIdNotFound() {
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> cartService.findCartByBuyerId(any()));
	}
	
	@Test
	public void getCartByUserAuthenticatedSucess() {
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(buyer.getId())).thenReturn(Optional.of(cart));
		
		Cart cart = cartService.getCartOfUserAuthenticated();
		
		assertNotNull(cart);
	}
	
	@Test
	public void getCartByUserAuthenticatedBuyerNotFound() {
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(any())).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> cartService.getCartOfUserAuthenticated());
	}
	
	@Test
	public void getCartByUserAuthenticatedCartNotFound() {
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(any())).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(1L)).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> cartService.getCartOfUserAuthenticated());
	}
	
	@Test
	public void checkoutForUserAuthenticatedSucess() {
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		
		
		ArrayList<CartItem> itens = new ArrayList();
		itens.addAll(List.of(ci1, ci2));
		cart.setCartItems(itens);
		
		cartService.checkoutForUserAuthenticated();
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(cartRepository).save(cartCaptor.capture());
		
		assertNotNull(orderCaptor.getValue());
		assertEquals(orderCaptor.getValue().getOrdersItem().size(), 2);
		assertEquals(cartCaptor.getValue().getCartItems().size(), 0);
	}
	
	@Test
	public void checkoutForUserAuthenticatedCartEmpty() {
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		
		assertThrows(ConflictException.class, () -> cartService.checkoutForUserAuthenticated());
	}
	
	@Test
	public void checkoutByBuyerIdSucess() {
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
		
		when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		
		
		ArrayList<CartItem> itens = new ArrayList();
		itens.addAll(List.of(ci1, ci2));
		cart.setCartItems(itens);
		
		cartService.checkoutByBuyerId(1L);
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(cartRepository).save(cartCaptor.capture());
		
		assertNotNull(orderCaptor.getValue());
		assertEquals(orderCaptor.getValue().getOrdersItem().size(), 2);
		assertEquals(cartCaptor.getValue().getCartItems().size(), 0);
	}
	
	@Test
	public void checkoutByBuyerIdCartEmpty() {
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
		
		when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		
		assertThrows(ConflictException.class, () -> cartService.checkoutByBuyerId(1L));
	}
}
