package com.api_ecommerce.e_commerce.entity;

import java.math.BigDecimal;
import java.util.List;

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
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String productName;
	
	@Column
	private String productDescription;
	
	@Column
	private BigDecimal productPrice;
	
	@Column
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private Order order;

	public OrderItem(String productName, String productDescription, BigDecimal productPrice, int quantity) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	public OrderItem(String productName, String productDescription, BigDecimal productPrice, int quantity,
			Order order) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.quantity = quantity;
		this.order = order;
	}
}
