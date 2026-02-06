package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

import org.springframework.web.bind.annotation.*;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.dto.AccountsPayableMovementResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/accounts-payable-movements")
public class AccountsPayableMovementController {

    private final AccountsPayableMovementService movementService;

    public AccountsPayableMovementController(
            AccountsPayableMovementService movementService) {
        this.movementService = movementService;
    }

    // --------------------------------------------------
    // Get movements for a specific accounts payable
    // --------------------------------------------------

    @GetMapping("/by-accounts-payable/{accountsPayableId}")
    public List<AccountsPayableMovementResponseDTO> getByAccountsPayable(
            @PathVariable Long accountsPayableId) {
        return movementService
                .findByAccountsPayable(accountsPayableId)
                .stream()
                .map(AccountsPayableMovementResponseDTO::from)
                .toList();
    }
}
