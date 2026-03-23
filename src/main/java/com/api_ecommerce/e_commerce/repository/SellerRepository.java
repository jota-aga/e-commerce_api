package com.api_ecommerce.e_commerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long>{
	Optional<Seller> findByCnpj(String cnpj);
}
