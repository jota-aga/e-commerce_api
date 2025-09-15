package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.models.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
