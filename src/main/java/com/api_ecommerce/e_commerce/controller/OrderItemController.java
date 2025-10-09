package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.dto.order_item.OrderItemRequest;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.service.OrderItemService;
import com.api_ecommerce.e_commerce.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("order-item")
public class OrderItemController {
	@Autowired
	OrderItemService orderItemService;
	
	@Autowired
	OrderService orderService;
	
	@PostMapping("/{orderId}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> createOrderItem(@Valid @RequestBody OrderItemRequest orderItemRequest, @PathVariable Long orderId){
		Order order = orderService.findOrderById(orderId);
		OrderItem orderItem = new OrderItem(orderItemRequest.productName(), orderItemRequest.productDescription(), orderItemRequest.productPrice(), orderItemRequest.quantity(), order);
		orderItemService.save(orderItem);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> deleteOrderItem(@PathVariable Long id){
		OrderItem orderItem = orderItemService.findOrderItemById(id);
		orderItemService.deleteOrderItem(orderItem);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	public ResponseEntity<String> editOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemRequest orderItemRequest){
		OrderItem orderItem = orderItemService.findOrderItemById(id);
		orderItemService.editOrderItem(orderItem, orderItemRequest);
		orderItemService.save(orderItem);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	} 
}
