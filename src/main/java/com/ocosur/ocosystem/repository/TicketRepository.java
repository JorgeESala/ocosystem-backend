package com.ocosur.ocosystem.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.model.Ticket;



public interface TicketRepository extends JpaRepository<Ticket,Integer>{
    
    List<Ticket> findByBranchIdAndDateBetween(
        Integer branchId,
        OffsetDateTime start,
        OffsetDateTime end
    );
}
