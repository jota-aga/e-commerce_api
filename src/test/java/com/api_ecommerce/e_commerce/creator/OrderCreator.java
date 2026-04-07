package com.api_ecommerce.e_commerce.creator;

import java.time.LocalDate;
import java.util.List;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Order;
import com.api_ecommerce.e_commerce.entity.OrderItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class OrderCreator {
	public static Order simpleOrder() {
		Buyer buyer = BuyerCreator.simpleBuyer();
		Order order = Order.builder()
						   .buyer(buyer)
						   .createdAt(LocalDate.now())
						   .build();
		
		return order;
	}
	
	public static Order orderWithItems() {
		Order order = simpleOrder();
		
		Product p1 = ProductCreator.productAvaliable();
		Product p2 = ProductCreator.productUnavaliable();
		
		OrderItem orderItem1 = new OrderItem(1l, p1.getName(), p1.getDescription(), 
				p1.getPrice(), p1.getQuantity(), p1, order);
		
		OrderItem orderItem2 = new OrderItem(2l, p2.getName(), p2.getDescription(), 
				p2.getPrice(), p2.getQuantity(), p2, order);
		
		order.setOrderItems(List.of(orderItem1, orderItem2));
		
		return order;
	}
}
