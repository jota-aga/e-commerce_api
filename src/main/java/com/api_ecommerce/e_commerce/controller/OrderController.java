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
import com.api_ecommerce.e_commerce.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findOrdersByUserId(@PathVariable Long id){
		List<OrderAdminResponse> orderAdminResponse = orderService.findOrdersByUserId(id);
	
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping("/date")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findOrdersByDate(@RequestParam LocalDate date){
		List<OrderAdminResponse> orderAdminResponse = orderService.findOrdersByDate(date);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping()
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<List<OrderAdminResponse>> findAllOrders(){
		List<OrderAdminResponse> orderAdminResponse = orderService.findAllOrders();
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@PostMapping("/{userId}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> createOrder(@PathVariable Long userId){
		orderService.createOrder(userId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id){
		orderService.deleteOrderById(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
