package com.api_ecommerce.e_commerce.entity;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Transient;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="buyer_id", nullable = false)
	private Buyer buyer;
	
	@OneToMany(mappedBy = "cart_id", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<CartItem> cartItems;
	
	@Transient
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		if(cartItems == null || cartItems.isEmpty()) return BigDecimal.ZERO;
		
		return cartItems.stream()
		        		.map(cartItem -> cartItem.getProduct().getPrice()
		        		.multiply(BigDecimal.valueOf(cartItem.getQuantity())))
		        		.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
