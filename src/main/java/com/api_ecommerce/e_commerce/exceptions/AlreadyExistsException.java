package com.api_ecommerce.e_commerce.exceptions;

public class AlreadyExistsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(String object) {
		super("This "+ object +" already exists.");	
	}
}
