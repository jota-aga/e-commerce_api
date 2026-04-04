package com.api_ecommerce.e_commerce.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.repository.RoleRepository;

@Configuration
@Order(0)
public class RoleConfig implements CommandLineRunner {
	@Autowired
	RoleRepository roleRepository;
		
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Optional<Role> roleRepeat = roleRepository.findRoleByName(Role.Value.ADMIN.name());
		
		roleRepeat.ifPresentOrElse(
								   u -> System.out.println("The roles are already in db"),
								   () ->{
									   		Role roleAdmin = Role.builder()
									   							 .name(Role.Value.ADMIN.name())
									   							 .build();
									   		Role roleBuyer = Role.builder()
									   							 .name(Role.Value.BUYER.name())
									   							 .build();
									   		roleRepository.save(roleAdmin);
									   		roleRepository.save(roleBuyer);
								   }
								 );
		
	}

}
