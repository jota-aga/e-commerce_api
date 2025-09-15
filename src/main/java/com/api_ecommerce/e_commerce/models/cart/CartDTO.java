package com.api_ecommerce.e_commerce.models.cart;

import java.math.BigDecimal;
import java.util.List;

import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.user.User;

public class CartDTO {
	private User user;
	private BigDecimal totalValue;
	private List<Order> orders;
	
	public CartDTO(User user) {
		super();
		this.totalValue = BigDecimal.ZERO;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
