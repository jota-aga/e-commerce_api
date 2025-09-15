package com.api_ecommerce.e_commerce.models.order;

import java.math.BigDecimal;

import com.api_ecommerce.e_commerce.models.cart.Cart;
import com.api_ecommerce.e_commerce.models.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany
	@JoinColumn(name="product_id", nullable = false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="cart_id", nullable = false)
	private Cart cart;
	
	@Column
	private int quantity;
	
	@Column
	private BigDecimal totalValue;

	public Order( Product product, Cart cart, int quantity, BigDecimal totalValue) {
		super();
		this.product = product;
		this.cart = cart;
		this.quantity = quantity;
		this.totalValue = totalValue;
	}

	public Order() {
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

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
}
