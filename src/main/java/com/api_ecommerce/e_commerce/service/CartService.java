package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.mapper.CartMapper;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void saveCart(Cart cart) {
		cartRepository.save(cart);
	}
	
	public CartAdminResponse findCartById(Long id) {
		Optional<Cart> optionalCart = cartRepository.findById(id);
		
		Cart cart = optionalCart.orElseThrow(() -> new IdNotFoundException("Cart"));
		
		CartAdminResponse cartAdminResponse = CartMapper.toAdminDTO(cart);
		
		return cartAdminResponse;
	}
	
	public void checkout(Long userId) {
		User user = findUserById(userId);
		
		Cart cart = findCartByUserId(userId);
		
		createOrderByCheckout(user, cart);
		
		cart.getCartItems().clear();
		
		cartRepository.save(cart);
	}

	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}
	
	private void createOrderByCheckout(User user, Cart cart) {
		List<OrderItem> orderItems = cart
				.getCartItems().stream().map(cartItem -> new OrderItem(cartItem.getProduct().getName(),
						cartItem.getProduct().getDescricao(), cartItem.getProduct().getPrice(), cartItem.getQuantity()))
				.toList();

		Order order = new Order(user, orderItems);
		orderItems.forEach(orderItem -> orderItem.setOrder(order));
		orderRepository.save(order);
	}
	
	private User findUserById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		
		User user = optionalUser.orElseThrow(() -> new IdNotFoundException("User"));
		
		return user;
	}
	
	public Cart findCartByUserId(Long userId) {
		Optional<Cart> cart = cartRepository.findByUserId(userId);
		
		return cart.orElseThrow(() -> new IdNotFoundException("Cart By user id"));
	}
}
