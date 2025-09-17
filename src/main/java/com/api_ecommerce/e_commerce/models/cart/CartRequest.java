package com.api_ecommerce.e_commerce.models.cart;

import java.util.List;

import com.api_ecommerce.e_commerce.models.cart_item.CartItem;
import com.api_ecommerce.e_commerce.models.user.User;

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
