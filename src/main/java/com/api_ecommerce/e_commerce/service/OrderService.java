package com.api_ecommerce.e_commerce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
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
	
		return order.orElseThrow(() -> new IdNotFoundException("Order"));
	}
	
	public List<Order> findOrdersByDate(LocalDate date){
		List<Order> orders = orderRepository.findAllByCreatedAt(date);
		
		return orders;
	}
	
	public List<Order> findAllOrders(){
		List<Order> orders= orderRepository.findAll();
				
		return orders;
	}
	
	private Buyer findBuyerByUserAuthenticated() {
		User user = TokenService.getCurrentUser();
		
		Optional<Buyer> optionalBuyer = buyerRepository.findByUser(user);
		
		return optionalBuyer.orElseThrow(() -> new IdNotFoundException("User"));
	}
}
