package com.ocosur.ocosystem.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocosur.ocosystem.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    List<Expense> findByBranchIdAndDateBetween(
            Integer branchId,
            OffsetDateTime start,
            OffsetDateTime end);

    Page<Expense> findByBranchIdInOrderByDateDesc(List<Integer> branchIds, Pageable pageable);

    List<Expense> findByBranchIdAndDateGreaterThanEqualAndDateLessThan(
            Integer branchId,
            OffsetDateTime start,
            OffsetDateTime end);

    @Query("""
                SELECT COALESCE(SUM(e.amount), 0)
                FROM Expense e
                WHERE e.date BETWEEN :start AND :end
                AND e.branch.id = :branchId
            """)
    BigDecimal sumBetweenByBranch(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("branchId") Integer branchId);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.date BETWEEN :start AND :end
            AND e.branch.id IN :branchIds
            """)
    BigDecimal sumBetweenByBranches(LocalDate start, LocalDate end, List<Integer> branchIds);

    List<Expense> findByBranchIdInAndDateBetweenOrderByDateDesc(
            List<Integer> branchIds,
            LocalDate start,
            LocalDate end);

    Page<Expense> findAllByOrderByDateDesc(Pageable pageable);

}
