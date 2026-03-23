package com.api_ecommerce.e_commerce.entity;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seller {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String name;
	
	@CNPJ
	@NotNull
	private String cnpj;
	
	@OneToOne
	private User user;

	public Seller(@NotEmpty String name, @CNPJ @NotNull String cnpj, User user) {
		super();
		this.name = name;
		this.cnpj = cnpj;
		this.user = user;
	}
}
