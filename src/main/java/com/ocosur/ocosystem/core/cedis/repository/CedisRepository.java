package com.ocosur.ocosystem.core.cedis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.core.cedis.model.Cedis;

public interface CedisRepository extends JpaRepository<Cedis, Long> {
    
}
