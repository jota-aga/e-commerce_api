package com.api_ecommerce.e_commerce.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.OrderItemRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private SecurityService securityService;
	
	public List<Order> findOrdersByBuyerId(Long id) {
		List<Order> orders = orderRepository.findAllOrderByBuyerId(id);
				
		return orders;
	}
	
	public List<Order> findOrdersByUserAuthenticated(){
		Buyer buyer = findBuyerByUserAuthenticated();
		
		return findOrdersByBuyerId(buyer.getId());
	}
	
	public Order findOrderById(Long id) {
		Optional<Order> order = orderRepository.findById(id);
	
		return order.orElseThrow(() -> new NotFoundException("Order's id"));
	}
	
	public List<Order> findOrdersByPeriod(LocalDate start, LocalDate end){
		List<Order> orders = orderRepository.findAllByCreatedAtBetween(start, end);
		
		return orders;
	}
	
	public List<Order> findAllOrders(){
		List<Order> orders= orderRepository.findAll();
				
		return orders;
	}
	
	public List<Order> findOrdersByProduct(Long productId){
		List<OrderItem> orderItems = orderItemRepository.findAllByProductId(productId);
		
		List<Order> orders = new ArrayList<>();
		orderItems.forEach(orderItem -> orders.add(orderItem.getOrder()));
		
		return orders;
	}
	
	private Buyer findBuyerByUserAuthenticated() {
		User user = securityService.getCurrentUser();
		
		Optional<Buyer> optionalBuyer = buyerRepository.findByUser(user);
		
		return optionalBuyer.orElseThrow(() -> new NotFoundException("Buyer's id"));
	}
}
