package com.api_ecommerce.e_commerce.models.cart;

import java.util.List;

import com.api_ecommerce.e_commerce.models.cart_item.CartItem;
import com.api_ecommerce.e_commerce.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@OneToMany(mappedBy = "item", orphanRemoval = false)
	private List<CartItem> orders;
	

	public Cart(User user) {
		super();
		this.user = user;
	}

	public Cart() {
		super();
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

	public List<CartItem> getOrders() {
		return orders;
	}

	public void setOrders(List<CartItem> orders) {
		this.orders = orders;
	}
}
