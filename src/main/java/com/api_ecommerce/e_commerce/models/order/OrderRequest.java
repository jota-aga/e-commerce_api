package com.api_ecommerce.e_commerce.models.order;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {
	
	@NotNull
	private Long CartId;
	
	@NotNull
	private Long productId;
	
	@Positive
	private int quantity;

	public OrderRequest(Long cartId, Long productId, int quantity) {
		super();
		CartId = cartId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getCartId() {
		return CartId;
	}

	public void setCartId(Long cartId) {
		CartId = cartId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
