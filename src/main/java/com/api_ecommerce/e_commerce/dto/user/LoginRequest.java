package com.api_ecommerce.e_commerce.dto.user;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String username, @NotNull String password) {

}
