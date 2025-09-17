package com.api_ecommerce.e_commerce.exceptions;

public class IdNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdNotFoundException(String object) {
		super(object+"'s id not exists.");
	}
}
