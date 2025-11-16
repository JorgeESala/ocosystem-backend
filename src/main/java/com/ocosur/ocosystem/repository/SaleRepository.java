package com.ocosur.ocosystem.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer>{
    List<Sale> findByTicketBranchIdAndTicketDateBetween(Integer branchId,
            OffsetDateTime start,
            OffsetDateTime end);
}
