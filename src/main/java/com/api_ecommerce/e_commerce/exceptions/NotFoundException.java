package com.api_ecommerce.e_commerce.exceptions;

public class NotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(String object) {
		super(object+" not found.");
	}
}
