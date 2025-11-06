package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
}
