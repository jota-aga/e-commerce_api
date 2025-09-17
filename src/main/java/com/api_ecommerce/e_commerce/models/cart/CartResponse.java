package com.api_ecommerce.e_commerce.models.cart;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.models.cart_item.CartItem;
import com.api_ecommerce.e_commerce.models.user.User;

public class CartResponse {
	private User user;
	private List<CartItem> orders;
	private BigDecimal totalValue;
	
	public CartResponse(User user, List<CartItem> orders) {
		super();
		this.user = user;
		this.totalValue = BigDecimal.ZERO;
		this.orders = new ArrayList<>();
		for(CartItem order : orders) {
			BigDecimal quantity = BigDecimal.valueOf(order.getQuantity());
			BigDecimal value = quantity.multiply(order.getProduct().getPrice());
			this.totalValue = this.totalValue.add(value);
			

		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItem> getOrder() {
		return this.orders;
	}

	public void setOrders(List<CartItem> orders) {
		this.orders = orders;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
}
