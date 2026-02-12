package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.dto.AccountsPayableMovementResponseDTO;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsPayableMovementService {

    private final AccountsPayableMovementRepository repository;

    public List<AccountsPayableMovement> findByAccountsPayable(
            Long accountsPayableId) {
        return repository
                .findByAccountsPayableIdOrderByCreatedAtAsc(
                        accountsPayableId);
    }

    public List<AccountsPayableMovementResponseDTO> findByAccountPayable(
            Long accountsPayableId) {

        List<AccountsPayableMovement> movements = repository
                .findByAccountsPayable_IdOrderByMovementDateAscCreatedAtAsc(accountsPayableId);

        if (movements.isEmpty()) {
            return List.of();
        }

        BigDecimal runningBalance = movements.get(0).getAccountsPayable().getTotalAmount();

        List<AccountsPayableMovementResponseDTO> result = new ArrayList<>();

        for (AccountsPayableMovement m : movements) {

            BigDecimal balanceBefore = runningBalance;

            runningBalance = applyMovement(runningBalance, m);

            BigDecimal balanceAfter = runningBalance;

            result.add(
                    AccountsPayableMovementResponseDTO.builder()
                            .id(m.getId())
                            .movementType(m.getMovementType())
                            .amount(m.getAmount())
                            .balanceBefore(balanceBefore)
                            .balanceAfter(balanceAfter)
                            .notes(m.getNote())
                            .createdAt(m.getCreatedAt())
                            .movementDate(m.getMovementDate())
                            .build());
        }

        return result;
    }

    private BigDecimal applyMovement(BigDecimal currentBalance,
            AccountsPayableMovement m) {

        return switch (m.getMovementType()) {

            case PAYMENT, COMPENSATION ->
                currentBalance.subtract(m.getAmount());

            case ADJUSTMENT ->
                currentBalance.add(m.getAmount());

            case OTHER ->
                currentBalance;

        };
    }

}
