package com.api_ecommerce.e_commerce.entity;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Seller {
	@NotEmpty
	private String name;
	
	@CNPJ
	@NotNull
	private String cnpj;
	
	@OneToOne
	private User user;
}
