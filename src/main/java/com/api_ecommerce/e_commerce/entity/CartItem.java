package com.api_ecommerce.e_commerce.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

	public CartItem(Product product, int quantity, Cart cart) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.cart = cart;
	}
	
	
}
