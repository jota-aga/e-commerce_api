package com.api_ecommerce.e_commerce.enums;

public enum ProductStatus {
	AVAILABLE("Available"),
	UNAVAILABLE("Unavailable");
	
	private String description;

	private ProductStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
