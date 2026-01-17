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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart_item")
public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
	
	@PostMapping("/{cartId}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> saveCartItem(@Valid @RequestBody CartItemRequest cartItemDTO, @PathVariable Long cartId){
		cartItemService.createCartItem(cartItemDTO, cartId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> deleteCartItem(@PathVariable Long id){
		cartItemService.deleteCartItem(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();		
		
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> editCartItem(@PathVariable Long id, @Valid @RequestBody CartItemRequest cartItemDTO){
		cartItemService.editCartItem(id, cartItemDTO);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
