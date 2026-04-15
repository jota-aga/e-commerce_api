package com.api_ecommerce.e_commerce.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.mapper.OrderMapper;
import com.api_ecommerce.e_commerce.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOrdersByBuyerId(@PathVariable Long id, Pageable pageable){
		Page<Order> orders = orderService.findOrdersByBuyerId(id, pageable);
		
		Page<OrderAdminResponse> orderAdminResponse = orders.map(order -> OrderMapper.INSTANCE.orderToOrderAdminResponse(order));
	
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping("/date")
	public ResponseEntity<?> findOrdersByPeriod(@RequestParam LocalDate start, @RequestParam LocalDate end, Pageable pageable){
		Page<Order> orders = orderService.findOrdersByPeriod(start, end, pageable);
		
		Page<OrderAdminResponse> orderAdminResponse = orders.map(order -> OrderMapper.INSTANCE.orderToOrderAdminResponse(order));
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping
	public ResponseEntity<List<OrderAdminResponse>> findAllOrders(){
		List<Order> orders = orderService.findAllOrders();
		
		List<OrderAdminResponse> orderAdminResponse = OrderMapper.INSTANCE.listOrderToListOrderAdminResponse(orders);
		
		return ResponseEntity.status(HttpStatus.OK).body(orderAdminResponse);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<?> findOrdersByProduct(@PathVariable Long productId, Pageable pageable){
		Page<Order> orders = orderService.findOrdersByProduct(productId, pageable);
		
		Page<OrderAdminResponse> ordersResponse = orders.map(order -> OrderMapper.INSTANCE.orderToOrderAdminResponse(order));
		
		return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
	}
}
