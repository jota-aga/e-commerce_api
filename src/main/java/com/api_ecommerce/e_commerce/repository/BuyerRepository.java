package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Buyer;
import com.api_ecommerce.e_commerce.entity.User;

public interface BuyerRepository extends JpaRepository<Buyer, Long>{
	Optional<Buyer> findByCpf(String cpf);
	Optional<Buyer> findByUser(User user);
}
