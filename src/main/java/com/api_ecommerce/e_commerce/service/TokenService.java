package com.api_ecommerce.e_commerce.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.User;

@Service
public class TokenService {
	
	@Autowired 
	private JwtEncoder jwtEncoder;
	
	public static User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();

		return user;
	}
	
	public String generateToken(User user) {
		var scopes = user.getRole();
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
}
