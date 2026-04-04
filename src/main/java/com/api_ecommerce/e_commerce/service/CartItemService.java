package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.enums.ProductStatus;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotAuthorizedException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartItemRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class CartItemService {
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private SecurityService securityService;
	
	public List<CartItem> findCartItemsByCartId(Long id){
		List<CartItem> cartItems = cartItemRepository.findAllByCartId(id);
		
		return cartItems;
	}
	
	public CartItem findCartItemById(Long id) {
		Optional<CartItem> cartItem = cartItemRepository.findById(id);
		
		return cartItem.orElseThrow(() -> new NotFoundException("Cart Item id"));
	}
	
	@Transactional
	public void deleteCartItem(Long id) {
		CartItem cartItem = findCartItemById(id);
		
		validateCartItemId(cartItem);
		
		cartItemRepository.delete(cartItem);
	}
	
	@Transactional
	public void deleteAllCartItem(List<CartItem> cartItems) {
		cartItemRepository.deleteAll(cartItems);
	}
	
	@Transactional
	public void addItemToCartToCurrentUser(CartItemRequest dto) {
		Cart cart = findCartByUserAuthenticated();
		
		createCartItem(dto, cart);
	}
	
	@Transactional
	public void addItemToCart(CartItemRequest dto, Long cartId) {
		Cart cart = cartRepository.findById(cartId)
				    .orElseThrow(() -> new NotFoundException("Cart"));
		
		createCartItem(dto, cart);
	}
	
	@Transactional
	public void editCartItem(Long id, CartItemRequest cartItemDTO) {
		CartItem cartItem = findCartItemById(id);
		
		validateCartItemId(cartItem);
		
		Product product = findProductById(cartItemDTO.productId());
		
		cartItem.setProduct(product);
		cartItem.setQuantity(cartItemDTO.quantity());
		
		cartItemRepository.save(cartItem);
	}
	
	@Transactional
	private void createCartItem(CartItemRequest dto, Cart cart) {
		Product product = findProductById(dto.productId());
		
		if(product.getStatus() != ProductStatus.AVAILABLE) {
			throw new ConflictException("This Product is unavailable");
		}
		
		CartItem cartItem = CartItem.builder()
									.product(product)
									.quantity(dto.quantity())
									.cart(cart)
									.build();

		cartItemRepository.save(cartItem);
	}
	
	private void validateCartItemId(CartItem cartItem) {
		User user = securityService.getCurrentUser();
		
		if(user.getRole().getName().equals(Role.Value.ADMIN.name())) return;
				
		Buyer buyer = cartItem.getCart().getBuyer();
		
		if(!user.getId().equals(buyer.getUser().getId())) throw new NotAuthorizedException("You cant manage this cart item");	
	}
	
	private Product findProductById(Long productId) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		
		Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product id"));
		
		return product;
	}
	
	private Cart findCartByUserAuthenticated() {
		User user = securityService.getCurrentUser();
		
		Optional<Buyer> optionalBuyer = buyerRepository.findByUser(user);
		Buyer buyer = optionalBuyer.orElseThrow(() -> new NotFoundException("Buyer by User"));
		
		Optional<Cart> optionalCart = cartRepository.findByBuyerId(buyer.getId());
		
		Cart cart = optionalCart.orElseThrow(() -> new NotFoundException("Cart id"));
		
		return cart;
	}
}
