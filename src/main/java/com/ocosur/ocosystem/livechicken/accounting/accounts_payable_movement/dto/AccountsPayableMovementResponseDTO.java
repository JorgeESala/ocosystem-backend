package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovement;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovementType;

@Getter
@Builder
public class AccountsPayableMovementResponseDTO {

    private Long id;

    private AccountsPayableMovementType movementType;

    private BigDecimal amount;

    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    private String notes;

    private OffsetDateTime createdAt;
    private LocalDate movementDate;

    public static AccountsPayableMovementResponseDTO from(
            AccountsPayableMovement m) {
        return AccountsPayableMovementResponseDTO.builder()
                .id(m.getId())
                .movementType(m.getMovementType())
                .amount(m.getAmount())
                // .balanceBefore(m.getBalanceBefore())
                // .balanceAfter(m.getBalanceAfter())
                .notes(m.getNote())
                .movementDate(m.getMovementDate())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
