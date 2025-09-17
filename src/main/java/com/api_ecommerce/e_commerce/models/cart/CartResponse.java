package com.api_ecommerce.e_commerce.models.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.models.order.Order;
import com.api_ecommerce.e_commerce.models.order.OrderResponse;
import com.api_ecommerce.e_commerce.models.user.User;

public class CartResponse {
	private User user;
	private List<OrderResponse> ordersResponse;
	private BigDecimal totalValue;
	
	public CartResponse(User user, List<Order> orders) {
		super();
		this.user = user;
		this.totalValue = BigDecimal.ZERO;
		this.ordersResponse = new ArrayList<>();
		for(Order order : orders) {
			BigDecimal quantity = BigDecimal.valueOf(order.getQuantity());
			BigDecimal value = quantity.multiply(order.getProduct().getPrice());
			this.totalValue = this.totalValue.add(value);
			
			ordersResponse.add(new OrderResponse(order.getProduct(), order.getQuantity()));
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderResponse> getOrdersReponse() {
		return this.ordersResponse;
	}

	public void setOrders(List<OrderResponse> ordersResponse) {
		this.ordersResponse = ordersResponse;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
}
