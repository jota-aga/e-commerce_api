package com.api_ecommerce.e_commerce.creator;

import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class OrderItemCreator {
	
	public static OrderItem orderItem() {
		Product product = ProductCreator.productAvaliable();
		Order order = OrderCreator.simpleOrder();
		
		OrderItem orderItem = new OrderItem(1l, product.getName(), product.getDescription(), 
											product.getPrice(), product.getQuantity(), product, order);
		
		return orderItem;
	}
}
