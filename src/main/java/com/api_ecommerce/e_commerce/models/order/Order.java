package com.api_ecommerce.e_commerce.models.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.api_ecommerce.e_commerce.models.order_item.OrderItem;
import com.api_ecommerce.e_commerce.models.user.User;

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
	
	@Column
	private BigDecimal totalValue;
	
	public Order(User user, BigDecimal totalValue) {
		super();
		this.user = user;
		this.createdAt = LocalDate.now();
		this.totalValue = totalValue;
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

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	
	
}
