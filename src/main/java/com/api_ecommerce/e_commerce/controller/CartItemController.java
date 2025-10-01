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
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.ProductService;

@RestController
@RequestMapping("/cart_item")
public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartService cartService;
	
	@PostMapping()
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> saveCartItem(@RequestBody CartItemRequest cartItemDTO){
		Product product = productService.findProductById(cartItemDTO.getProductId());
		Cart cart = cartService.findCartById(cartItemDTO.getCartId());
		
		CartItem cartItem = new CartItem(product, cartItemDTO.getQuantity(), cart);
		cartItemService.saveCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> deleteCartItem(@PathVariable Long id){
		CartItem cartItem = cartItemService.findCartItemById(id);
		cartItemService.deleteCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.OK).build();		
		
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> editCartItem(@PathVariable Long id, @RequestBody CartItemRequest cartItemDTO){
		CartItem cartItem = cartItemService.findCartItemById(id);
		cartItem = cartItemService.editCartItem(cartItem, cartItemDTO);
		cartItemService.saveCartItem(cartItem);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
