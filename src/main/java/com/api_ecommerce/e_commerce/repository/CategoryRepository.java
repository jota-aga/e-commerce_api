package com.api_ecommerce.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_ecommerce.e_commerce.models.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
