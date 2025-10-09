package com.api_ecommerce.e_commerce.controller;

import java.time.LocalDate;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.mapper.OrderMapper;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findOrdersByUserId(@PathVariable Long id){
		List<Order> orders = orderService.findOrdersByUserId(id);
		
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping("/date")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findOrdersByDate(@RequestParam LocalDate date){
		List<Order> orders = orderService.findOrdersByDate(date);
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping()
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findAllOrders(){
		List<Order> orders = orderService.findAllOrders();
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.toListAdminDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@PostMapping("/{userId}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> createOrder(@PathVariable Long userId){
		User user = userService.findUserById(userId);
		Order order = new Order(user);
		orderService.saveOrder(order);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id){
		Order order = orderService.findOrderById(id);
		
		orderService.deleteOrderById(order);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
