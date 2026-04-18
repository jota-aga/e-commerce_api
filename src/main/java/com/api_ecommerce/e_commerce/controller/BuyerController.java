package com.api_ecommerce.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.api_ecommerce.e_commerce.dto.cart.CartClientResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemRequest;
import com.api_ecommerce.e_commerce.dto.order.OrderBuyerResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.mapper.CartMapper;
import com.api_ecommerce.e_commerce.mapper.OrderMapper;
import com.api_ecommerce.e_commerce.service.AuthService;
import com.api_ecommerce.e_commerce.service.CartItemService;
import com.api_ecommerce.e_commerce.service.CartService;
import com.api_ecommerce.e_commerce.service.OrderService;
import com.api_ecommerce.e_commerce.service.ProductService;
import com.api_ecommerce.e_commerce.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
	
	@Autowired
	AuthService authService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartItemService cartItemService;
	
	@Autowired
	TokenService tokenService;
	
	@GetMapping("/orders")
	public ResponseEntity<?> getAllOrderOfBuyer(Pageable pageable){		
		Page<Order> orders = orderService.findOrdersByUserAuthenticated(pageable);
		
		Page<OrderBuyerResponse> ordersResponse = orders.map(order -> OrderMapper.INSTANCE.orderToOrderBuyerResponse(order));
		
		return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
	}
	
	@GetMapping("/cart")
	public ResponseEntity<CartClientResponse> getCart(){		
		Cart cart = cartService.getCartOfUserAuthenticated();
		
		CartClientResponse cartClientResponse = CartMapper.INSTANCE.cartToCartClienteResponse(cart);
		
		return ResponseEntity.status(HttpStatus.OK).body(cartClientResponse);
	}
	
	@PostMapping("/cart-item")
	public ResponseEntity<?> saveCartItem(@Valid @RequestBody CartItemRequest request){
				
		cartItemService.addItemToCartToCurrentUser(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/cart-item/{cartItemId}")
	public ResponseEntity<?> editCartItem(@PathVariable Long cartItemId, @Valid @RequestBody CartItemRequest request){		
		cartItemService.editCartItem(cartItemId, request);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/cart-item/{cartItemId}")
	public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId){		
		cartItemService.deleteCartItem(cartItemId);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<?> checkoutCart(){		
		cartService.checkoutForUserAuthenticated();
		
		return ResponseEntity.status(HttpStatus.OK).build();		
	}
}
