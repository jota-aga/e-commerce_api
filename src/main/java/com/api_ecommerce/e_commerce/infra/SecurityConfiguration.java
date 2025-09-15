package com.api_ecommerce.e_commerce.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Bean
	public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager) {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sessionManagement -> session.sessionCreationPolicy(sessionCreationPolicy.STATELESS))
				.authorize()
	}
	
	
	

}
