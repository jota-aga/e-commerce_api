
package com.api_ecommerce.e_commerce.entity;

import java.math.BigDecimal;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	@Column
	private String descricao;
	
	@Column 
	private BigDecimal price;
	
	@Column
	private int quantity;
	
	@OneToMany(mappedBy="product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItem;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;

	public Product(String name, String descricao, int quantity, BigDecimal price, Category category) {
		super();
		this.name = name;
		this.descricao = descricao;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}
	
	
}
