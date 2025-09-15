package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.cart.CartDTO;
import com.api_ecommerce.e_commerce.models.user.RegisterRequest;
import com.api_ecommerce.e_commerce.models.user.User;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("User")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegisterRequest register){
		User user = new User(register.getUsername(), register.getPassword(), register.getRole());
		CartDTO cartDTO = new CartDTO(user);
		
		Cart cart = new Cart(cartDTO.getUser(), cartDTO.getTotalValue());
		
		userService.saveUser(user);
		cartService.saveCart(cart);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}	
}
