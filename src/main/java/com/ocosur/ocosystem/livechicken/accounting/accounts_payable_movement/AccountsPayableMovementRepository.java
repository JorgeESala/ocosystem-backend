package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountsPayableMovementRepository
        extends JpaRepository<AccountsPayableMovement, Long> {

    @Query("""
                SELECT m
                FROM AccountsPayableMovement m
                WHERE m.accountsPayable.id = :accountsPayableId
                ORDER BY m.createdAt ASC
            """)
    List<AccountsPayableMovement> findByAccountsPayableIdOrderByCreatedAtAsc(
            Long accountsPayableId);

    List<AccountsPayableMovement> findByAccountsPayable_IdOrderByMovementDateAscCreatedAtAsc(Long id);

}
