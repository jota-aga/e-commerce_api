package com.api_ecommerce.e_commerce.mapper;

import java.math.BigDecimal;

import java.util.List;

import com.api_ecommerce.e_commerce.dto.cart.CartResponse;
import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.dto.category.CategoryDTO;
import com.api_ecommerce.e_commerce.dto.order.OrderDTO;
import com.api_ecommerce.e_commerce.dto.order_item.OrderItemDTO;
import com.api_ecommerce.e_commerce.dto.product.ProductDTO;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Category;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class Mappers {
	
	public static ProductDTO toDTO(Product product) {
		ProductDTO dto = new ProductDTO(product.getName(), product.getDescricao(), 
										product.getPrice(), product.getCategory().getId(), product.getQuantity());
		
		return dto;
	}
	
	public static List<ProductDTO> toDTO(List<Product> products){
		List<ProductDTO> dto = products.stream()
									   .map(product -> Mappers.toDTO(product))
									   .toList();
		
		return dto;
	}
	
	public static Product toEntity(ProductDTO productDTO, Product product) {
		product.setName(productDTO.name());
		product.setDescricao(productDTO.description());
		product.setPrice(productDTO.price());
		product.setQuantity(productDTO.quantity());
		
		return product;
	}
	
	public static OrderItemDTO toDTO(OrderItem orderItem) {
		OrderItemDTO orderItemResponse = new OrderItemDTO(orderItem.getProductName(), orderItem.getProductDescription(), 
																	orderItem.getProductPrice(), orderItem.getQuantity());
		
		return orderItemResponse;
	}
	
	public static OrderDTO toDTO(Order order) {
		List<OrderItemDTO> orderItemDTO = order.getOrdersItem().stream()
																	.map(orderItem -> Mappers.toDTO(orderItem))
																	.toList();
		OrderDTO dto = new OrderDTO(orderItemDTO, order.getCreatedAt(), order.getTotalValue());
		
		return dto;
	}
	
	public static CartItemResponse toDTO(CartItem cartItem) {
		CartItemResponse dto = new CartItemResponse(Mappers.toDTO(cartItem.getProduct()), cartItem.getQuantity());
		
		return dto;
	}
	
	public static CartResponse toDTO(Cart cart) {
		List<CartItemResponse> cartItemDTO = cart.getCartItems().stream()
																.map(cartItem -> Mappers.toDTO(cartItem))
																.toList();
		
		BigDecimal totalValue = cart.getCartItems().stream()
												   .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
												   .reduce(BigDecimal.ZERO, BigDecimal::add);
		CartResponse dto = new CartResponse(cart.getUser(), cartItemDTO, totalValue);
		
		return dto;
	}
	
	public static List<OrderDTO> toListOrderDTO(List<Order> orders){
		List<OrderDTO> dto = orders.stream()
				 .map(order -> Mappers.toDTO(order))
				 .toList();
		return dto;
	}
	
	public static CategoryDTO toDTO(Category category) {
		CategoryDTO dto = new CategoryDTO(category.getName());
		
		return dto;
	}
}
