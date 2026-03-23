package com.api_ecommerce.e_commerce.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Buyer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	@Past
	private LocalDate birthday;
	
	@CPF
	@NotEmpty
	private String cpf;
	
	private String adress;
	
	@OneToOne
	private User user;

	public Buyer(String name, LocalDate birthday, String cpf, String adress,
			User user) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.cpf = cpf;
		this.adress = adress;
		this.user = user;
	}
	
	
}
