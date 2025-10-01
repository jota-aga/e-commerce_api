package com.api_ecommerce.e_commerce.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.cart.CartResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemAdminRequest;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemClientRequest;
import com.api_ecommerce.e_commerce.dto.order.OrderResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.mapper.Mappers;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.api_ecommerce.e_commerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartItemService cartItemService;
	
	@GetMapping("/order")
	public ResponseEntity<List<OrderResponse>> getAllOrderClient(JwtAuthenticationToken token){
		List<Order> orders = orderService.findOrdersByUserId( Long.valueOf(token.getToken().getSubject()));
		List<OrderResponse> ordersResponse = Mappers.toListOrderDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
	}
	
	@GetMapping("/cart")
	public ResponseEntity<CartResponse> getCartClient(JwtAuthenticationToken token){
		Cart cart = cartService.findCartByUserId( Long.valueOf(token.getToken().getSubject()));
		CartResponse cartResponse = Mappers.toDTO(cart);
		
		return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
	}
	
	@PostMapping("/cart/item")
	public ResponseEntity<?> saveCartItemCliente(@Valid @RequestBody CartItemClientRequest request, JwtAuthenticationToken token){
		Cart cart = cartService.findCartByUserId( Long.valueOf(token.getToken().getSubject()));
		Product product = productService.findProductById(request.productId());
		CartItem cartItem = new CartItem(product, request.quantity(), cart);
		cartItemService.saveCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/cart/item/{cartItemId}")
	public ResponseEntity<?> editCartItemCliente(@PathVariable Long cartItemId, @Valid @RequestBody CartItemAdminRequest request, JwtAuthenticationToken token){
		CartItem cartItem = cartItemService.findCartItemById(cartItemId);
		cartItemService.validateCartItemId(cartItem, Long.valueOf(token.getToken().getSubject()));
		cartItemService.editCartItem(cartItem, request);
		cartItemService.saveCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/cart/item/{cartItemId}")
	public ResponseEntity<?> deleteCartItemCliente(@PathVariable Long cartItemId, JwtAuthenticationToken token){
		CartItem cartItem = cartItemService.findCartItemById(cartItemId);
		cartItemService.validateCartItemId(cartItem, Long.valueOf(token.getToken().getSubject()));
		cartItemService.deleteCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<?> checkoutCart(JwtAuthenticationToken token){
		User user = userService.findUserById(Long.valueOf(token.getToken().getSubject()));
		Cart cart = cartService.findCartByUserId(Long.valueOf(token.getToken().getSubject()));
		
		cartService.checkout(user, cart);
		
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
}
