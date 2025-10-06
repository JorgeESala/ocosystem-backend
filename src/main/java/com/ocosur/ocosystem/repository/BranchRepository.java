package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Branch;


public interface BranchRepository extends JpaRepository<Branch, Integer> {
    
}
