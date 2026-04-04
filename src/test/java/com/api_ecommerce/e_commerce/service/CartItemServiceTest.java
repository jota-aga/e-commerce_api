package com.api_ecommerce.e_commerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.api_ecommerce.e_commerce.creator.BuyerCreator;
import com.api_ecommerce.e_commerce.creator.CartCreator;
import com.api_ecommerce.e_commerce.creator.ProductCreator;
import com.api_ecommerce.e_commerce.creator.RoleCreator;
import com.api_ecommerce.e_commerce.creator.UserCreator;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
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
	
	@Mock
	private SecurityService securityService;
	
	private Product product;
	private Buyer buyer;
	private Cart cart;
	private User user;
	private Role roleBuyer;
	
	@BeforeEach
	public void setUp() {	
		product = ProductCreator.productAvaliable();
		buyer = BuyerCreator.simpleBuyer();
		user = UserCreator.userBuyer();
		cart = CartCreator.cartWithBuyer();
		roleBuyer = RoleCreator.roleBuyer();
	}
	
	@Test
	public void deleteCartItemSucess() {
		CartItem cartItem = new CartItem(1L, product, 1, cart);
		
		when(cartItemRepository.findById((any()))).thenReturn(Optional.of(cartItem));
		when(securityService.getCurrentUser()).thenReturn(user);
		
		cartItemService.deleteCartItem(1L);
		
		verify(cartItemRepository).findById(1L);
	    verify(cartItemRepository).delete(cartItem);	
	}
	
	@Test
	public void deleteCartItemWithAnotherUser() {
		CartItem cartItem = new CartItem(1L, product, 1, cart);
		User anotherUser = User.builder()
							   .id(Long.MAX_VALUE)
							   .role(roleBuyer)
							   .build();
		
		when(cartItemRepository.findById((any()))).thenReturn(Optional.of(cartItem));
		when(securityService.getCurrentUser()).thenReturn(anotherUser);
		
		assertThrows(NotAuthorizedException.class, () -> cartItemService.deleteCartItem(1L));	
	}
	
	@Test
	public void findCartItemById() {		
		when(cartItemRepository.findById((any()))).thenReturn(Optional.of(new CartItem()));
		
		cartItemService.findCartItemById(any());
		verify(cartItemRepository).findById(any());	
	}
	
	@Test
	public void findCartItemByIdNotFound() {		
		when(cartItemRepository.findById((any()))).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class,() -> cartItemService.findCartItemById(any()));
	}
	
	@Test
	public void createCartItemForUserAuthenticatedSucess() {
		ArgumentCaptor<CartItem> captorCartItem = ArgumentCaptor.forClass(CartItem.class);
		CartItemRequest dto = new CartItemRequest(1L, 10);
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		
		cartItemService.addItemToCartToCurrentUser(dto);
		
		verify(cartItemRepository).save(captorCartItem.capture());
		
		assertEquals(captorCartItem.getValue().getCart(), cart);
	}
	
	@Test
	public void createCartItemForUserAuthenticatedForProductUnavailable() {
		CartItemRequest dto = new CartItemRequest(1L, 10);
		product.setStatus(ProductStatus.UNAVAILABLE);
		
		when(securityService.getCurrentUser()).thenReturn(user);
		when(buyerRepository.findByUser(user)).thenReturn(Optional.of(buyer));
		when(cartRepository.findByBuyerId(any())).thenReturn(Optional.of(cart));
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		
		assertThrows(ConflictException.class,() -> cartItemService.addItemToCartToCurrentUser(dto));
	}
	
	@Test
	public void editCartItemSucess() {
		ArgumentCaptor<CartItem> captorCartItem = ArgumentCaptor.forClass(CartItem.class);
		CartItemRequest dto = new CartItemRequest(1L, 50);
		CartItem cartItem = new CartItem(1L, product, 1, cart);
		
		when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
		when(securityService.getCurrentUser()).thenReturn(user);
		when(productRepository.findById(any())).thenReturn(Optional.of(product));
		
		cartItemService.editCartItem(1L, dto);
		
		verify(cartItemRepository).save(captorCartItem.capture());
		
		assertEquals(captorCartItem.getValue().getQuantity(), 50);
	}
	
	@Test
	public void editCartItemWithAnotherUser() {
		CartItemRequest dto = new CartItemRequest(1L, 50);
		CartItem cartItem = new CartItem(1L, product, 1, cart);
		User anotherUser = new User(5L, "username", "password", roleBuyer);
		
		when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
		when(securityService.getCurrentUser()).thenReturn(anotherUser);
		
		assertThrows(NotAuthorizedException.class, () -> cartItemService.editCartItem(1L, dto));
	}
}
