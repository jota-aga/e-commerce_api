package com.api_ecommerce.e_commerce.dto.user;

import com.api_ecommerce.e_commerce.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@NotBlank
	private String login;
	
	@NotBlank
	@Size(min = 8, max = 32)
	private String password;
	
	@NotBlank
	private UserRole role;

	public RegisterRequest(String login, String password, UserRole role) {
		super();
		this.login = login;
		this.password = password;
		this.role = role;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
}
