package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findRoleByName(String name);
}
