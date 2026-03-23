package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long>{
	Optional<Buyer> findByCpf(String cpf);
}
