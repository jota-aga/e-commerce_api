package com.api_ecommerce.e_commerce.models.cart_item;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="item")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	@Column
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="cart_id", nullable = true)
	private Cart cart;

	public CartItem(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	public CartItem() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
