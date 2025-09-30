package com.api_ecommerce.e_commerce.exceptions;

public class NotAuthorizedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAuthorizedException() {
		super("you dont have authorization");
	}
}
