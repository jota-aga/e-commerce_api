package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.order.OrderRequest;
import com.api_ecommerce.e_commerce.models.product.Product;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartService cartService;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveOrder(@RequestBody OrderRequest orderRequest){
		Product product = productService.findProductById(orderRequest.getProductId());
		
		Cart cart = cartService.findCartById(orderRequest.getCartId());
		
		Order order = new Order(product, cart, orderRequest.getQuantity());
		
		orderService.saveOrder(order);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable Long id){
		Order order = orderService.findOrderById(id);
		orderService.deleteOrderById(order);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("edit/{id}")
	public ResponseEntity<String> editOrder(@RequestBody OrderRequest orderRequest, @PathVariable Long id){
		Order order = orderService.findOrderById(id);
		Product product = productService.findProductById(orderRequest.getProductId());
		order.setProduct(product);
		order.setQuantity(orderRequest.getQuantity());
		
		orderService.saveOrder(order);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
