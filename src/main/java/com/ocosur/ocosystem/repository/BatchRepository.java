package com.ocosur.ocosystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByBranchOrderByDateDesc(Branch branch, Pageable pageable);

    List<Batch> findByBranchIdInAndDateBetweenOrderByDateDesc(
            List<Long> branchIds,
            LocalDate start,
            LocalDate end);

    Page<Batch> findAllByOrderByDateDesc(Pageable pageable);

    @Query("""
                select b
                from Batch b
                join fetch b.branch br
                where b.date between :startDate and :endDate
                  and br.id in :branchIds
                  ORDER BY b.date DESC
            """)
    List<Batch> findBetweenDatesAndBranchIds(
            LocalDate startDate,
            LocalDate endDate,
            List<Long> branchIds);

}
