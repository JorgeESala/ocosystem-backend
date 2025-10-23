package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.DailyBatchSale;

public interface DailyBatchSaleRepository extends JpaRepository<DailyBatchSale, Integer> {
    
}
