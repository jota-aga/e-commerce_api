package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class CartItemService {
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public List<CartItem> findCartItemsByCartId(Long id){
		List<CartItem> cartItems = cartItemRepository.findAllByCartId(id);
		
		return cartItems;
	}
	
	public void saveCartItem(CartItem cartItem) {
		cartItemRepository.save(cartItem);
	}
	
	public void createCartItem(CartItemRequest cartItemRequest, Long cartId) {
		Product product = findProductById(cartItemRequest.productId());
		
		Cart cart = findCartById(cartId);
		
		CartItem cartItem = new CartItem(product, cartItemRequest.quantity(), cart);
		
		saveCartItem(cartItem);
	}
	
	public CartItem findCartItemById(Long id) {
		Optional<CartItem> cartItem = cartItemRepository.findById(id);
		
		return cartItem.orElseThrow(() -> new IdNotFoundException("Cart Item"));
	}
	
	public void deleteCartItem(Long id, Long userId) {
		CartItem cartItem = findCartItemById(id);
		
		validateCartItemId(cartItem, userId);
		
		cartItemRepository.delete(cartItem);
	}
	
	public void deleteAllCartItem(List<CartItem> cartItems) {
		cartItemRepository.deleteAll(cartItems);
	}
	
	public void editCartItem(Long id, CartItemRequest cartItemDTO, Long userId) {
		CartItem cartItem = findCartItemById(id);
		
		validateCartItemId(cartItem, userId);
		
		Product product = findProductById(cartItemDTO.productId());
		
		cartItem.setProduct(product);
		cartItem.setQuantity(cartItemDTO.quantity());
		
		saveCartItem(cartItem);
	}
	
	private void validateCartItemId(CartItem cartItem, Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			
			if(user.getRoles().contains(Role.Value.ADMIN) || user.getCart().getId() == cartItem.getCart().getId()) {
				return;
			}
		}	
			throw new NotAuthorizedException();
		
	}
	
	private Product findProductById(Long productId) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		
		Product product = optionalProduct.orElseThrow(() -> new IdNotFoundException("Product"));
		
		return product;
	}
	
	private Cart findCartById(Long cartId) {
		Optional<Cart> optionalCart = cartRepository.findById(cartId);
		
		Cart cart = optionalCart.orElseThrow(() -> new IdNotFoundException("Cart"));
		
		return cart;
	}
}
