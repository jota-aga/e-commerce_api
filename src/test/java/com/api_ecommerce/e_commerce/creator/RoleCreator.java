package com.api_ecommerce.e_commerce.creator;

import com.api_ecommerce.e_commerce.entity.Role;

public class RoleCreator {
	
	public static Role roleBuyer() {
		return new Role(1L, Role.Value.BUYER.name());
	}
	
	public static Role roleAdmin() {
		return new Role(1L, Role.Value.ADMIN.name());
	}
}
