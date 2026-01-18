package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart_item")
public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/{cartId}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> saveCartItem(@Valid @RequestBody CartItemRequest cartItemDTO, @PathVariable Long cartId){
		cartItemService.createCartItem(cartItemDTO, cartId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> deleteCartItem(@PathVariable Long id){
		Long adminUserId = tokenService.getCurrentUserId();
		
		cartItemService.deleteCartItem(id, adminUserId);
		
		return ResponseEntity.status(HttpStatus.OK).build();		
		
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<?> editCartItem(@PathVariable Long id, @Valid @RequestBody CartItemRequest cartItemDTO){
		Long adminUserId = tokenService.getCurrentUserId();
		
		cartItemService.editCartItem(id, cartItemDTO, adminUserId);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
