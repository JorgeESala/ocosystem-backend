package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Product;

public interface ProductRepository extends JpaRepository<Product,String> {
    
    
}
