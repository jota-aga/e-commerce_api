package com.api_ecommerce.e_commerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.order_item.OrderItem;
import com.api_ecommerce.e_commerce.models.user.User;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
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
		BigDecimal total = cart.getCartItems().stream()
				  			   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
				  			   .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		Order order = new Order(user, total);
		orderRepository.save(order);
		
		List<OrderItem> orderItems = cart.getCartItems().stream()
														.map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity(), order))
														.toList();
		orderItemRepository.saveAll(orderItems);
		
		cart.getCartItems().clear();
		
		cartRepository.save(cart);
	}
}
