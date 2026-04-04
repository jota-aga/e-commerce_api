package com.api_ecommerce.e_commerce.creator;

import java.util.ArrayList;
import java.util.List;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.Cart;
import com.api_ecommerce.e_commerce.entity.CartItem;
import com.api_ecommerce.e_commerce.entity.Product;

public class CartCreator {
	
	public static Cart cartWithBuyer() {
		Buyer buyer = BuyerCreator.simpleBuyer();
		return Cart.builder()
				   .buyer(buyer)
				   .build();
	}
	
	public static Cart completeCart() {
		List<CartItem> cartItems = new ArrayList<>();
		Buyer buyer = BuyerCreator.simpleBuyer();
		Cart cart = Cart.builder()
				   .id(1L)
				   .buyer(buyer)
				   .cartItems(cartItems)
				   .build();
		
		Product product1 = ProductCreator.productAvaliable();
		Product product2 = ProductCreator.productUnavaliable();
		CartItem cartItem1 = new CartItem(1L, product1, 10, cart);
		CartItem cartItem2 = new CartItem(2L, product2, 15, cart);
		cartItems.addAll(List.of(cartItem1, cartItem2));
		return cart;
	}
}
