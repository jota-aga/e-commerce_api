package com.api_ecommerce.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.cart.CartRequest;
import com.api_ecommerce.e_commerce.dto.cart.CartResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.mapper.Mappers;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
	public ResponseEntity<CartResponse> findCartById(@PathVariable("id") Long id){
		Cart cart = cartService.findCartById(id);
		CartResponse cartResponse = Mappers.toDTO(cart);
		
		return ResponseEntity.status(HttpStatus.OK).body(cartResponse);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<List<CartItem>> checkoutCart(@PathVariable("id") Long userId){
		User user = userService.findUserById(userId);
		Cart cart = cartService.findCartByUserId(userId);
		
		cartService.checkout(user, cart);
		
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
	
	@PostMapping()
	public ResponseEntity<List<CartItem>> createCart(@RequestBody CartRequest cartRequest){
		User user = userService.findUserById(cartRequest.userId());
		
		Cart cart = new Cart(user);
		
		cartService.saveCart(cart);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCart(@PathVariable Long id){
		Cart cart = cartService.findCartById(id);
		
		cartService.deleteCart(cart);
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
}
