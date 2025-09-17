package com.api_ecommerce.e_commerce.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductAlreadyExistsException() {
		super("This name of product already exists.");
	}
}
