package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Losses;

public interface LossesRepository extends JpaRepository<Losses, Integer> {
    
}
