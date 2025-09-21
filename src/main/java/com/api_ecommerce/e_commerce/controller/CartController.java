package com.api_ecommerce.e_commerce.controller;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.cart.CartResponse;
import com.api_ecommerce.e_commerce.models.cart_item.CartItem;
import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.order_item.OrderItem;
import com.api_ecommerce.e_commerce.models.user.User;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderItemService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> findCartById(@PathVariable("id") Long id){
		Cart cart = cartService.findCartById(id);
		CartResponse cartResponse = new CartResponse(cart.getUser(), cart.getCartItems());
		
		return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<List<CartItem>> checkoutCart(@PathVariable("id") Long userId){
		User user = userService.findUserById(userId);
		Cart cart = cartService.findCartByUserId(userId);
		
		cartService.checkout(user, cart);
		
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
}
