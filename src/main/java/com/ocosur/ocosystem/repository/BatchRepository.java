package com.ocosur.ocosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Batch;

public interface BatchRepository extends JpaRepository<Batch, Integer> {

}
