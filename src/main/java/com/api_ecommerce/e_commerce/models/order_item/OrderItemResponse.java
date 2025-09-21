package com.api_ecommerce.e_commerce.models.order_item;

import com.api_ecommerce.e_commerce.models.product.Product;

public class OrderItemResponse {
	private Product product;
	
	private int quantity;

	public OrderItemResponse() {
		super();
	}

	public OrderItemResponse(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
