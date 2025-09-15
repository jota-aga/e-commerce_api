package com.api_ecommerce.e_commerce.models.user;

import com.api_ecommerce.e_commerce.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	@NotBlank
	private String username;
	@NotBlank
	@Size(min = 8, max = 32)
	private String password;
	@NotBlank
	private Role role;

	public RegisterRequest(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;

		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
