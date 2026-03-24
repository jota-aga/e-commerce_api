package com.api_ecommerce.e_commerce.entity;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="buyer_id", nullable = false)
	private Buyer buyer;
	
	@OneToMany(mappedBy = "cart", cascade= CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItems;

	public Cart(Buyer buyer, List<CartItem> cartItems) {
		super();
		this.buyer = buyer;
		this.cartItems = cartItems;
	}

	public Cart(Buyer buyer) {
		super();
		this.buyer = buyer;
	}
}
