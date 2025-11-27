package com.ocosur.ocosystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.BatchSale;

public interface BatchSaleRepository extends JpaRepository<BatchSale, Integer> {
    public List<BatchSale> findByBatchId(Integer batchId);
}
