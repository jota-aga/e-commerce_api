package com.api_ecommerce.e_commerce.service;

import java.time.Instant;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api_ecommerce.e_commerce.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
							  .withIssuer("auth-api")
							  .withSubject(user.getUsername())
							  .withExpiresAt(this.tokenExpiration())
							  .sign(algorithm);
			return token;
							  
							  
		}
		catch(JWTCreationException e) {
			throw new RuntimeException("Error while generating token");
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					  .withIssuer("auth-api")
					  .build()
					  .verify(token)
					  .getSubject();
		}catch(JWTVerificationException exception) {
			throw new RuntimeException("token is not valid");
		}
	}
	
	public Instant tokenExpiration() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
	public String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if(authHeader == null) return null;
		return authHeader.replace("Bearer ", "");
	}
}
