package com.api_ecommerce.e_commerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepository;
	
	public Role getRoleByName(String name) {
		return roleRepository.findRoleByName(name).get();
	}
}
