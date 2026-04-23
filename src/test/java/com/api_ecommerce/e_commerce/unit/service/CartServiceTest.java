package com.api_ecommerce.e_commerce.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.api_ecommerce.e_commerce.creator.CartCreator;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.SecurityService;

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
	private CartItemRepository cartItemRepository;
	
	@Mock
	private SecurityService securityService;
	
	private User user;
	private Buyer buyer;
	private Cart cart;
	
	@BeforeEach
	public void setUp() {
		cart = CartCreator.completeCart();
		buyer = cart.getBuyer();
		user = buyer.getUser();
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
	public void getCartByUserAuthenticatedNotFound() {
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(any())).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(1L)).thenReturn(Optional.empty());
				
		assertThrows(NotFoundException.class, () -> cartService.getCartOfUserAuthenticated());
	}
	
	@Test
	public void checkoutForUserAuthenticatedSucess() {
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(buyer.getId())).thenReturn(Optional.of(cart));
		when(cartItemRepository.existsByCart(cart)).thenReturn(true);
		when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(cart.getCartItems());
		
		cartService.checkoutForUserAuthenticated();
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(cartItemRepository, atLeastOnce()).deleteAll(cart.getCartItems());
		
		assertNotNull(orderCaptor.getValue());
		assertEquals(orderCaptor.getValue().getOrderItems().size(), 2);
	}
	
	@Test
	public void checkoutForUserAuthenticatedCartEmpty() {
		cart.setCartItems(List.of());
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(cart.getId())).thenReturn(Optional.of(cart));
		when(cartItemRepository.existsByCart(cart)).thenReturn(false);
		
		assertThrows(ConflictException.class, () -> cartService.checkoutForUserAuthenticated());
	}
	
	@Test
	public void checkoutByBuyerIdSucess() {
		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
		
		when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		when(cartItemRepository.existsByCart(cart)).thenReturn(true);
		when(cartItemRepository.findAllByCartId(cart.getId())).thenReturn(cart.getCartItems());

		
		cartService.checkoutByBuyerId(1L);
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(cartItemRepository, atLeastOnce()).deleteAll(cart.getCartItems());
		
		Order order = orderCaptor.getValue();
		
		assertNotNull(order);
		assertEquals(order.getOrderItems().size(), 2);
	}
	
	@Test
	public void checkoutByBuyerIdCartEmpty() {
		cart.setCartItems(List.of());
		
		when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(cart.getBuyer().getId())).thenReturn(Optional.of(cart));
		when(cartItemRepository.existsByCart(cart)).thenReturn(false);
		
		assertThrows(ConflictException.class, () -> cartService.checkoutByBuyerId(1L));
	}
}
