package com.ocosur.ocosystem.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

        List<Expense> findByBranchIdAndDateBetween(
                        Integer branchId,
                        OffsetDateTime start,
                        OffsetDateTime end);

        List<Batch> findByBranchOrderByDateDesc(Branch branch, Pageable pageable);

        List<Expense> findByBranchIdAndDateGreaterThanEqualAndDateLessThan(
                        Integer branchId,
                        OffsetDateTime start,
                        OffsetDateTime end);

}
