package com.api_ecommerce.e_commerce.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.product.Product;
import com.api_ecommerce.e_commerce.models.user.User;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.api_ecommerce.e_commerce.service.UserService;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> findOrdersByUserId(@PathVariable Long id){
		List<Order> orders = orderService.findOrdersByUserId(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
