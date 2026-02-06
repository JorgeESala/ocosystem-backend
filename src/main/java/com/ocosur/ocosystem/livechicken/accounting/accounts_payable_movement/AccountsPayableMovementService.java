package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsPayableMovementService {

    private final AccountsPayableMovementRepository repository;

    public AccountsPayableMovementService(
            AccountsPayableMovementRepository repository) {
        this.repository = repository;
    }

    public List<AccountsPayableMovement> findByAccountsPayable(
            Long accountsPayableId) {
        return repository
                .findByAccountsPayableIdOrderByCreatedAtAsc(
                        accountsPayableId);
    }
}
