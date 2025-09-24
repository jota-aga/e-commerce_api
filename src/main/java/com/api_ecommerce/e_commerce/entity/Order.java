package com.api_ecommerce.e_commerce.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column
	private LocalDate createdAt;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> ordersItem;

	public Order(User user, List<OrderItem> ordersItem) {
		super();
		this.user = user;
		this.createdAt = LocalDate.now();
		this.ordersItem = ordersItem;
	}
	
	public Order(User user) {
		super();
		this.user = user;
		this.createdAt = LocalDate.now();
	}

	public Order() {
		this.createdAt = LocalDate.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderItem> getOrdersItem() {
		return ordersItem;
	}

	public void setOrdersItem(List<OrderItem> ordersItem) {
		this.ordersItem = ordersItem;
	}
}
