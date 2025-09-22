package com.api_ecommerce.e_commerce.dto.cart;

import java.util.List;

import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.User;

public class CartRequest {
	private User user;
	private List<CartItem> orders;
	
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

	public List<CartItem> getOrders() {
		return orders;
	}

	public void setOrders(List<CartItem> orders) {
		this.orders = orders;
	}
}
