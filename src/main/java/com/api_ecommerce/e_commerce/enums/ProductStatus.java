package com.api_ecommerce.e_commerce.enums;

public enum ProductStatus {
	DISPONIVEL("Disponível"),
	INDISPONIVEL("Indisponível");
	
	private String legenda;

	private ProductStatus(String legenda) {
		this.legenda = legenda;
	}

	public String getLegenda() {
		return legenda;
	}
}
