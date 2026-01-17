package com.api_ecommerce.e_commerce.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.mapper.OrderMapper;
import com.api_ecommerce.e_commerce.repository.OrderRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void saveOrder(Order order) {
		
		orderRepository.save(order);
	}
	
	public List<OrderAdminResponse> findOrdersByUserId(Long id) {
		List<Order> orders = orderRepository.findAllOrderByUserId(id);
		
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		return orderAdminResponse;
	}
	
	public Order findOrderById(Long id) {
		Optional<Order> order = orderRepository.findById(id);
	
		return order.orElseThrow(() -> new IdNotFoundException("Order"));
	}
	
	public void deleteOrderById(Long id) {
		Order order = findOrderById(id);
		orderRepository.delete(order);	
	}
	
	public List<OrderAdminResponse> findOrdersByDate(LocalDate date){
		List<Order> orders = orderRepository.findAllByCreatedAt(date);
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		return orderAdminResponse;
	}
	
	public List<OrderAdminResponse> findAllOrders(){
		List<Order> orders= orderRepository.findAll();
		
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		return orderAdminResponse;
	}

	public void createOrder(Long userId) {
		User user = findUserById(userId);
		
		Order order = new Order(user);
		
		saveOrder(order);
	}
	
	private User findUserById(Long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		
		User user = optionalUser.orElseThrow(() -> new IdNotFoundException("User"));
		
		return user;
	}
}
