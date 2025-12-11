package com.ocosur.ocosystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocosur.ocosystem.model.BatchSale;

public interface BatchSaleRepository extends JpaRepository<BatchSale, Long> {
    public List<BatchSale> findByBatchId(Long batchId);

    @Query("""
                SELECT bs FROM BatchSale bs
                JOIN FETCH bs.batch b
                JOIN b.branch br
                WHERE bs.date BETWEEN :start AND :end
                AND br.id = :branchId
            """)
    List<BatchSale> findWithBatchBetweenDatesAndBranch(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("branchId") Long branchId);

    @Query("""
            SELECT bs
            FROM BatchSale bs
            WHERE bs.date BETWEEN :start AND :end
            AND bs.batch.branch.id IN :branchIds
            """)
    List<BatchSale> findWithBatchBetweenDatesAndBranchIds(
            LocalDate start,
            LocalDate end,
            List<Long> branchIds);

}
