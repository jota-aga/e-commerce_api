package com.api_ecommerce.e_commerce.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.cart.CartResponse;
import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> findCartById(@PathVariable Long id){
		Cart cart = cartService.findCartById(id);
		List<Order> orders = orderService.findOrdersByCartId(cart.getId());
		CartResponse cartResponse = new CartResponse(cart.getUser(), orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
	}
}
