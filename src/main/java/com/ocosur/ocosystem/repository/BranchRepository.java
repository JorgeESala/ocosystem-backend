package com.ocosur.ocosystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ocosur.ocosystem.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    @Query("SELECT b.id FROM Branch b")
    List<Integer> findAllIds();

}
