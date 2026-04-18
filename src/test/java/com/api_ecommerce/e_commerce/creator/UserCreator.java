package com.api_ecommerce.e_commerce.creator;

import java.time.LocalDate;

import com.api_ecommerce.e_commerce.dto.user.RegisterBuyerRequest;
import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;

public class UserCreator {
	
	public static User userAdmin() {
		Role role = RoleCreator.roleAdmin();
		return new User(1L, "username", "password", role);
	}
	
	public static User userBuyer() {
		Role role = RoleCreator.roleBuyer();
		return new User(2L, "username", "password", role);
	}
	
	public static RegisterBuyerRequest registerBuyerRequest() {
		return new RegisterBuyerRequest("username", "senha", "nome", "11237419484", LocalDate.now().minusYears(20), "endereço");
	}
}
