package com.api_ecommerce.e_commerce.controller;

import java.time.LocalDate;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.api_ecommerce.e_commerce.dto.order.OrderRequest;
import com.api_ecommerce.e_commerce.dto.order.OrderResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.Product;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.mapper.Mappers;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
	public ResponseEntity<List<OrderResponse>> findOrdersByUserId(@PathVariable Long id){
		List<Order> orders = orderService.findOrdersByUserId(id);
		
		List<OrderResponse> orderResponse = Mappers.toListOrderDTO(orders);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
	}
	
	@GetMapping("/date")
	public ResponseEntity<List<OrderResponse>> findOrdersByDate(@RequestParam LocalDate date){
		List<Order> orders = orderService.findOrdersByDate(date);
		List<OrderResponse> orderResponse = Mappers.toListOrderDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
	}
	
	@GetMapping()
	public ResponseEntity<List<OrderResponse>> findOrdersByDate(){
		List<Order> orders = orderService.findAllOrders();
		List<OrderResponse> orderResponse = Mappers.toListOrderDTO(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
	}
	
	@PostMapping()
	public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderDTO){
		User user = userService.findUserById(orderDTO.userId());
		Order order = new Order(user);
		orderService.saveOrder(order);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}
