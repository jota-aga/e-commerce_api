package com.api_ecommerce.e_commerce.models.order;

import com.api_ecommerce.e_commerce.models.product.Product;

public class OrderResponse {
	
	private Product product;
	private int quantity;
	
	public OrderResponse(Product product, int quantity) {
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
