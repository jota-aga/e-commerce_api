package com.api_ecommerce.e_commerce.dto.cart;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.dto.cart_item.CartItemResponse;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.User;

public class CartResponse {
	private User user;
	private List<CartItemResponse> cartItems;
	private BigDecimal totalValue;
	
	public CartResponse(User user, List<CartItem> cartItems) {
		this.user = user;
		this.cartItems = cartItems.stream()	
						.map(cartItem -> new CartItemResponse(cartItem.getProduct(), cartItem.getQuantity()))
						.toList();
		this.totalValue = cartItems.stream()
						.map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
						.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public CartResponse(User user, List<CartItemResponse> cartItems, BigDecimal totalValue) {
		super();
		this.user = user;
		this.cartItems = cartItems;
		this.totalValue = totalValue;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItemResponse> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemResponse> cartItems) {
		this.cartItems = cartItems;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
}
