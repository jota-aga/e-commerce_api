package com.api_ecommerce.e_commerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.ConflictException;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.mapper.OrderItemMapper;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.CartRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private SecurityService securityService;
	
	public Cart findCartById(Long id) {
		Optional<Cart> optionalCart = cartRepository.findById(id);
		
		Cart cart = optionalCart
				    .orElseThrow(() -> new NotFoundException("Cart's id"));
				
		return cart;
	}
	
	public Cart findCartByBuyerId(Long buyerId) {
		Optional<Cart> cart = cartRepository.findByBuyerId(buyerId);
		
		return cart
			   .orElseThrow(() -> new NotFoundException("Cart by buyer id"));
	}
	
	public Cart getCartOfUserAuthenticated() {
		Buyer buyer = findBuyerByUserAuthenticated();
				
		return findCartByBuyerId(buyer.getId());
	}
	
	@Transactional
	public void checkoutForUserAuthenticated() {
		Buyer buyer = findBuyerByUserAuthenticated();
		
		checkout(buyer);
	}
	
	@Transactional
	public void checkoutByBuyerId(Long buyerId) {
		Buyer buyer = buyerRepository.findById(buyerId)
					  .orElseThrow(() -> new NotFoundException("Buyer's id"));
		
		checkout(buyer);
	}
	
	@Transactional
	private void checkout(Buyer buyer) {
		Cart cart = findCartByBuyerId(buyer.getId());
		
		if(cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
			throw new ConflictException("The cart is empty");
		}
		
		createOrderByCart(buyer, cart);
		
		cart.getCartItems().clear();
		
		cartRepository.save(cart);
	}
	
	@Transactional
	private void createOrderByCart(Buyer buyer, Cart cart) {
		List<OrderItem> orderItems = cart
				.getCartItems().stream().map(cartItem -> OrderItemMapper.INSTANCE.cartItemToOrderItem(cartItem))
				.toList();
		
		if(orderItems == null || orderItems.isEmpty()) {
			throw new ConflictException("Order items is empty!");
			
		}
		else {
			Order order = Order.builder()
			           .buyer(buyer)
			           .orderItems(orderItems)
			           .build();
	
			orderItems.forEach(orderItem -> orderItem.setOrder(order));
			order.setOrderItems(orderItems);
			
			orderRepository.save(order);
		}
	}
	
	private Buyer findBuyerByUserAuthenticated() {
		User user = securityService.getCurrentUser();
		
		Optional<Buyer> optionalBuyer = buyerRepository.findByUser(user);
		
		return optionalBuyer.orElseThrow(() -> new NotFoundException("Buyer by user"));
	}
}
