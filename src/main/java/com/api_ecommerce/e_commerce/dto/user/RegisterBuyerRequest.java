package com.api_ecommerce.e_commerce.dto.user;

public record RegisterBuyerRequest(String username, String password, String name, String cpf, String birthday, String adress) {

}
