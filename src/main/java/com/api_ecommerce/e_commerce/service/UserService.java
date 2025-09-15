package com.api_ecommerce.e_commerce.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.api_ecommerce.e_commerce.models.user.User;
import com.api_ecommerce.e_commerce.repository.UserRepository;

public class UserService {
	@Autowired
	private UserRepository repo;
	
	public void saveUser(User user) {
		repo.save(user);
	}
	
}
