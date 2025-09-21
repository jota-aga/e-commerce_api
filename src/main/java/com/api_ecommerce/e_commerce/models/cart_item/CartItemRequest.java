package com.api_ecommerce.e_commerce.models.cart_item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CartItemRequest {
	
	@NotNull
	private Long productId;
	
	@NotNull
	private Long cartId;
	
	@Positive
	private int quantity;

	public CartItemRequest(@NotNull Long productId, @NotNull Long cartId, @Positive int quantity) {
		super();
		this.productId = productId;
		this.cartId = cartId;
		this.quantity = quantity;
	}

	public CartItemRequest() {
		super();
	}



	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
