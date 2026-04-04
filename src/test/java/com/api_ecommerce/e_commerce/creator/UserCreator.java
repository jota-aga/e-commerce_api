package com.api_ecommerce.e_commerce.creator;

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
}
