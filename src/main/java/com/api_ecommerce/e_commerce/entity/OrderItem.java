package com.api_ecommerce.e_commerce.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
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
}
