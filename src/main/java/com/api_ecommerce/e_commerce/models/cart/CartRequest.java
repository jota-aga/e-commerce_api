package com.api_ecommerce.e_commerce.models.cart;

import java.util.List;

import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.user.User;

public class CartRequest {
	private User user;
	private List<Order> orders;
	
	public CartRequest(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
