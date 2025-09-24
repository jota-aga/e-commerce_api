package com.api_ecommerce.e_commerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	public void saveCart(Cart cart) {
		cartRepository.save(cart);
	}
	
	public Cart findCartById(Long id) {
		Optional<Cart> cart = cartRepository.findById(id);
		
		return cart.get();
	}
	
	public Cart findCartByUserId(Long userId) {
		Optional<Cart> cart = cartRepository.findByUserId(userId);
		
		return cart.orElseThrow(() -> new IdNotFoundException("Cart By user id"));
	}
	
	public void checkout(User user, Cart cart) {
		
		List<OrderItem> orderItems = cart.getCartItems().stream()
														.map(cartItem -> new OrderItem(cartItem.getProduct().getName(), cartItem.getProduct().getDescricao(), 
																					   cartItem.getProduct().getPrice(), cartItem.getQuantity()))
														.toList();
		
		Order order = new Order(user, orderItems);
		orderItems.forEach(orderItem -> orderItem.setOrder(order));
		orderRepository.save(order);
		
		cart.getCartItems().clear();
		
		cartRepository.save(cart);
	}

	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}
}
