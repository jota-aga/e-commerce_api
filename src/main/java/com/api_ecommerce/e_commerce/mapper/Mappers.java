package com.api_ecommerce.e_commerce.mapper;

import java.math.BigDecimal;

import java.util.List;

import com.api_ecommerce.e_commerce.dto.cart.CartAdminResponse;
import com.api_ecommerce.e_commerce.dto.cart.CartClientResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.dto.order.OrderAdminResponse;
import com.api_ecommerce.e_commerce.dto.order.OrderClientResponse;
import com.api_ecommerce.e_commerce.dto.order_item.OrderItemResponse;
import com.api_ecommerce.e_commerce.dto.product.ProductRequest;
import com.api_ecommerce.e_commerce.dto.product.ProductResponse;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class Mappers {
	
	public static ProductResponse toDTO(Product product) {
		ProductResponse dto = new ProductResponse(product.getName(), product.getDescricao(), 
										product.getPrice(), product.getCategory().getName(), product.getQuantity());
		
		return dto;
	}
	
	public static List<ProductResponse> toProductDTOList(List<Product> products){
		List<ProductResponse> dto = products.stream()
									   .map(product -> Mappers.toDTO(product))
									   .toList();
		
		return dto;
	}
	
	public static Product toEntity(ProductRequest productRequest, Product product) {
		product.setName(productRequest.name());
		product.setDescricao(productRequest.description());
		product.setPrice(productRequest.price());
		product.setQuantity(productRequest.quantity());
		
		return product;
	}
	
	public static OrderItemResponse toDTO(OrderItem orderItem) {
		OrderItemResponse orderItemResponse = new OrderItemResponse(orderItem.getProductName(), orderItem.getProductDescription(), 
																	orderItem.getProductPrice(), orderItem.getQuantity());
		
		return orderItemResponse;
	}
	
	public static OrderAdminResponse toAdminDTO(Order order) {
		List<OrderItemResponse> orderItemDTO = order.getOrdersItem().stream()
																	.map(orderItem -> Mappers.toDTO(orderItem))
																	.toList();
		BigDecimal totalValue = order.getOrdersItem().stream()
													 .map(orderItem -> orderItem.getProductPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
													 .reduce(BigDecimal.ZERO, BigDecimal::add);
		OrderAdminResponse dto = new OrderAdminResponse(order.getUser(), orderItemDTO, order.getCreatedAt(), totalValue);
		
		return dto;
	}
	
	public static CartItemResponse toDTO(CartItem cartItem) {
		CartItemResponse dto = new CartItemResponse(Mappers.toDTO(cartItem.getProduct()), cartItem.getQuantity());
		
		return dto;
	}
	
	public static CartAdminResponse toAdminDTO(Cart cart) {
		List<CartItemResponse> cartItemDTO = cart.getCartItems().stream()
																.map(cartItem -> Mappers.toDTO(cartItem))
																.toList();
		
		BigDecimal totalValue = cart.getCartItems().stream()
												   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
												   .reduce(BigDecimal.ZERO, BigDecimal::add);
		CartAdminResponse dto = new CartAdminResponse(cart.getUser(), cartItemDTO, totalValue);
		
		return dto;
	}
	
	public static CartClientResponse toClientDTO(Cart cart) {
		List<CartItemResponse> cartItemDTO = cart.getCartItems().stream()
																.map(cartItem -> Mappers.toDTO(cartItem))
																.toList();
		
		BigDecimal totalValue = cart.getCartItems().stream()
												   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
												   .reduce(BigDecimal.ZERO, BigDecimal::add);
		CartClientResponse dto = new CartClientResponse(cartItemDTO, totalValue);
		
		return dto;
	}
	
	public static List<OrderAdminResponse> toListOrderAdminDTO(List<Order> orders){
		List<OrderAdminResponse> dto = orders.stream()
				 .map(order -> Mappers.toAdminDTO(order))
				 .toList();
		return dto;
	}
	
	public static List<OrderClientResponse> toListOrderClientDTO(List<Order> orders){
		List<OrderClientResponse> dto = orders.stream()
				 .map(order -> Mappers.toClientDTO(order))
				 .toList();
		return dto;
	}
	
	public static CategoryDTO toDTO(Category category) {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		return dto;
	}
	
	public static OrderClientResponse toClientDTO(Order order) {
		List<OrderItemResponse> ordersItemsDTO = order.getOrdersItem().stream()
																	  .map(orderItem -> Mappers.toDTO(orderItem))
																	  .toList();
		BigDecimal totalValue = order.getOrdersItem().stream()
				 .map(orderItem -> orderItem.getProductPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
				 .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		OrderClientResponse dto = new OrderClientResponse(ordersItemsDTO, order.getCreatedAt(), totalValue);
		return dto;
	}
}
