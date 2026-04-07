package com.api_ecommerce.e_commerce.creator;

import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class CartItemCreator {
	public static CartItem cartItemWithProductAvailable() {
		Cart cart = CartCreator.cartWithBuyer();
		Product product = ProductCreator.productAvaliable();
		
		return new CartItem(1L, product, 2, cart);
	}
}
