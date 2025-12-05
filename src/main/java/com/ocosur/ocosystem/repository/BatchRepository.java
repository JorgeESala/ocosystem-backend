package com.ocosur.ocosystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findByBranchOrderByDateDesc(Branch branch, Pageable pageable);

    List<Batch> findByBranchIdInAndDateBetweenOrderByDateDesc(
            List<Integer> branchIds,
            LocalDate start,
            LocalDate end);

    Page<Batch> findAllByOrderByDateDesc(Pageable pageable);

}
