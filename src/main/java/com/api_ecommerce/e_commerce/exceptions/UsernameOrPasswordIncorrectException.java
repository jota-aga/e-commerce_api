package com.api_ecommerce.e_commerce.exceptions;

public class UsernameOrPasswordIncorrectException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameOrPasswordIncorrectException() {
		super("username or password is incorrect");
	}
}
