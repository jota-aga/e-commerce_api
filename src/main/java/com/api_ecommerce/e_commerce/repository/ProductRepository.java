package com.api_ecommerce.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
	List<Product> findAllByCategoryId(Long categoryId);
	List<Product> findAllByNameIgnoreCase(String name);
} 
