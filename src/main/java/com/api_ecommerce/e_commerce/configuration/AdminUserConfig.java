package com.api_ecommerce.e_commerce.configuration;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.repository.RoleRepository;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		String username = "ecommerce_admin";
		String password = "joao789";
		
		Optional<User> adminRepeat =  userRepository.findByLogin(username);
		
		adminRepeat.ifPresentOrElse(
									u -> System.out.println(),
									() -> {
											Role role = roleRepository.findRoleByName(Role.Value.ADMIN.name()).get();
											User user = new User(username, passwordEncoder.encode(password), Set.of(role));
											userRepository.save(user);
										  }
								);
	}
}
