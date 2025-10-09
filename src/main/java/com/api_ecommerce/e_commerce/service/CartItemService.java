package com.api_ecommerce.e_commerce.service;

import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;

@Service
public class CartItemService {
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductService productService;
	
	public List<CartItem> findCartItemsByCartId(Long id){
		List<CartItem> cartItems = cartItemRepository.findAllByCartId(id);
		
		return cartItems;
	}
	
	public void saveCartItem(CartItem cartItem) {
		cartItemRepository.save(cartItem);
	}
	
	public CartItem findCartItemById(Long id) {
		Optional<CartItem> cartItem = cartItemRepository.findById(id);
		
		return cartItem.orElseThrow(() -> new IdNotFoundException("Cart Item"));
	}
	
	public void deleteCartItem(CartItem cartItem) {
		cartItemRepository.delete(cartItem);
	}
	
	public void deleteAllCartItem(List<CartItem> cartItems) {
		cartItemRepository.deleteAll(cartItems);
	}
	
	public CartItem editCartItem(CartItem cartItem, CartItemRequest cartItemDTO) {
		
		Product product = productService.findProductById(cartItemDTO.productId());
		
		cartItem.setProduct(product);
		cartItem.setQuantity(cartItemDTO.quantity());
		
		return cartItem;
	}
	
	public void validateCartItemId(CartItem cartItem, Long userId) {
		if(cartItem.getCart().getUser().getId()!= userId) throw new NotAuthorizedException();
	}
}
