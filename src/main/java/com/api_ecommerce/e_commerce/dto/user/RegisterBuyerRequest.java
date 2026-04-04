package com.api_ecommerce.e_commerce.dto.user;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record RegisterBuyerRequest(
		@NotEmpty
		String username,
		
		@NotEmpty
		String password,
		
		@NotNull
		String name, 
		
		@CPF
		@NotEmpty
		String cpf, 
		
		@NotNull
		@Past
		LocalDate birthday, 
		
		String adress
		) 
{}
