package com.api_ecommerce.e_commerce.dto.user;

import java.time.LocalDate;

public record RegisterBuyerRequest(String username, String password, String name, String cpf, LocalDate birthday, String adress) {

}
