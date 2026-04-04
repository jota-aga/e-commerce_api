
package com.api_ecommerce.e_commerce.entity;

import java.math.BigDecimal;

import com.api_ecommerce.e_commerce.enums.ProductStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	@Column
	private String description;
	
	@Column 
	private BigDecimal price;
	
	@Column
	private int quantity;
	
	@Enumerated(value = EnumType.STRING)
	private ProductStatus status;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
}
