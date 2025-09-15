package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.api_ecommerce.e_commerce.models.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<UserDetails> findByLogin(String login);

}
