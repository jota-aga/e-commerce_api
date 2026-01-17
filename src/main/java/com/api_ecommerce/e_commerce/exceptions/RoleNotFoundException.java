package com.api_ecommerce.e_commerce.exceptions;

public class RoleNotFoundException extends RuntimeException{

	public RoleNotFoundException() {
		super("Role Not Found");
	}
	
}
