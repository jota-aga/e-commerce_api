package com.api_ecommerce.e_commerce.dto.cart_item;

import com.api_ecommerce.e_commerce.dto.product.ProductDTO;

public class CartItemResponse {
	private ProductDTO product;
	private int quantity;
	public CartItemResponse(ProductDTO product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}
	public ProductDTO getProduct() {
		return product;
	}
	public void setProduct(ProductDTO product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
