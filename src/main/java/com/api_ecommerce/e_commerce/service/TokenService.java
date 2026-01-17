package com.api_ecommerce.e_commerce.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.Role;
import com.api_ecommerce.e_commerce.entity.User;
import com.api_ecommerce.e_commerce.exceptions.IdNotFoundException;
import com.api_ecommerce.e_commerce.repository.UserRepository;

@Service
public class TokenService {
	
	@Autowired 
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	public Long getCurrentUserId() {
		String token = getTokenOfContext();
		
		Long userId = Long.valueOf(token);
		
		return userId;
	}
	
	public String generateToken(User user) {
		var scopes = user.getRoles()
						.stream()
						.map(Role::getName)
						.collect(Collectors.joining(" "));
		var claims = JwtClaimsSet.builder()
				 .issuer("mybackend")
				 .subject(user.getId().toString())
				 .issuedAt(Instant.now())
				 .expiresAt(this.tokenExpiration())
				 .claim("scope", scopes)
				 .build();
		
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		return jwtValue;
	}
	
	private Instant tokenExpiration() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
	private String getTokenOfContext() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) auth;
		
		String token = jwtAuthenticationToken.getToken().getSubject();
		
		return token;
	}
}
