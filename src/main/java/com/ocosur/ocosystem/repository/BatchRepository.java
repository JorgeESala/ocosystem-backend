package com.ocosur.ocosystem.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findByBranchOrderByDateDesc(Branch branch, Pageable pageable);

}
