package com.api_ecommerce.e_commerce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.NotFoundException;
import com.api_ecommerce.e_commerce.repository.BuyerRepository;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.ProductRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private BuyerRepository buyerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SecurityService securityService;
	
	public Page<Order> findOrdersByBuyerId(Long id, Pageable pageable) {
		Page<Order> orders = orderRepository.findAllOrderByBuyerId(id, pageable);
				
		return orders;
	}
	
	public Page<Order> findOrdersByUserAuthenticated(Pageable pageable){
		Buyer buyer = findBuyerByUserAuthenticated();
		
		return findOrdersByBuyerId(buyer.getId(), pageable);
	}
	
	public Order findOrderById(Long id) {
		Optional<Order> order = orderRepository.findById(id);
	
		return order.orElseThrow(() -> new NotFoundException("Order's id"));
	}
	
	public Page<Order> findOrdersByPeriod(LocalDate start, LocalDate end, Pageable pageable){
		Page<Order> orders = orderRepository.findAllByCreatedAtBetween(start, end, pageable);
		
		return orders;
	}
	
	public List<Order> findAllOrders(){
		List<Order> orders= orderRepository.findAll();
				
		return orders;
	}
	
	public Page<Order> findOrdersByProduct(Long productId, Pageable pageable){
		Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Proudct id"));
		
		Page<Order> orders = orderRepository.findDistinctByOrderItemsProduct(product, pageable);
		
		return orders;
	}
	
	private Buyer findBuyerByUserAuthenticated() {
		User user = securityService.getCurrentUser();
		
		Optional<Buyer> optionalBuyer = buyerRepository.findByUser(user);
		
		return optionalBuyer.orElseThrow(() -> new NotFoundException("Buyer's id"));
	}
}
