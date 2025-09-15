package com.api_ecommerce.e_commerce.enums;

public enum Role {
	ADMIN("admin"),
	CLIENT("client");
	
	private String role;
	
	Role(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
