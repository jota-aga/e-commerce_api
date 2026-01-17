package com.api_ecommerce.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.cart.CartClientResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.dto.order.OrderClientResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.mapper.CartMapper;
import com.api_ecommerce.e_commerce.mapper.OrderMapper;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.api_ecommerce.e_commerce.service.TokenService;
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
	
	@Autowired
	TokenService tokenService;
	
	@GetMapping("/order")
	public ResponseEntity<List<OrderClientResponse>> getAllOrderClient(JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		List<Order> orders = orderService.findOrdersByUserId(userId);
		
		List<OrderClientResponse> ordersResponse = OrderMapper.toListClientDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
	}
	
	@GetMapping("/cart")
	public ResponseEntity<CartClientResponse> getCartClient(JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		Cart cart = cartService.findCartByUserId(userId);
		
		CartClientResponse cartClientResponse = CartMapper.toClientDTO(cart);
		
		return ResponseEntity.status(HttpStatus.OK).body(cartClientResponse);
	}
	
	@PostMapping("/cart/item")
	public ResponseEntity<?> saveCartItemCliente(@Valid @RequestBody CartItemRequest request, JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		Cart cart = cartService.findCartByUserId(userId);
		
		cartItemService.createCartItem(request, cart.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/cart/item/{cartItemId}")
	public ResponseEntity<?> editCartItemCliente(@PathVariable Long cartItemId, @Valid @RequestBody CartItemRequest request, JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		cartItemService.editCartItem(cartItemId, request, userId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/cart/item/{cartItemId}")
	public ResponseEntity<?> deleteCartItemCliente(@PathVariable Long cartItemId, JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		cartItemService.deleteCartItem(cartItemId, userId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<?> checkoutCart(JwtAuthenticationToken token){
		Long userId = tokenService.getCurrentUserId();
		
		cartService.checkout(userId);
		
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
}
