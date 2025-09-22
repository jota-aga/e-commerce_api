package com.api_ecommerce.e_commerce.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ProductRequest {
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@Positive
	private BigDecimal price;
	
	private Long categoryId;
	
	@Min(value=0)
	private int quantity;

	public ProductRequest(String name, String description, BigDecimal price, Long categoryId, int quantity) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryId = categoryId;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
