package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.api_ecommerce.e_commerce.creator.CartCreator;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.mapper.OrderItemMapper;
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
	
	@Mock
	private OrderItemMapper orderItemMapper;
	
	private User user;
	private Buyer buyer;
	private Cart cart;
	private CartItem ci1;
	private CartItem ci2;
	
	
	
	@BeforeEach
	public void setUp() {
		cart = CartCreator.completeCart();
		buyer = cart.getBuyer();
		user = buyer.getUser();
		ci1 = cart.getCartItems().get(0);
		ci2 = cart.getCartItems().get(1);
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
		
		cartService.checkoutForUserAuthenticated();
		
		verify(orderRepository).save(orderCaptor.capture());
		verify(cartRepository).save(cartCaptor.capture());
		
		assertNotNull(orderCaptor.getValue());
		assertEquals(orderCaptor.getValue().getOrderItems().size(), 2);
		assertEquals(cartCaptor.getValue().getCartItems().size(), 0);
	}
	
	@Test
	public void checkoutForUserAuthenticatedCartEmpty() {
		cart.setCartItems(List.of());
		
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
		assertEquals(orderCaptor.getValue().getOrderItems().size(), 2);
		assertEquals(cartCaptor.getValue().getCartItems().size(), 0);
	}
	
	@Test
	public void checkoutByBuyerIdCartEmpty() {
		cart.setCartItems(List.of());
		
		when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		
		assertThrows(ConflictException.class, () -> cartService.checkoutByBuyerId(1L));
	}
}
