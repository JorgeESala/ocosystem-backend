package com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayable;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountsPayableSourceType;

@Getter
@Builder
public class AccountsPayableResponseDTO {

    private Long id;

    private Long creditorEntityId;
    private Long debtorEntityId;

    private BigDecimal totalAmount;
    private BigDecimal balance;

    private AccountsPayableSourceType sourceType;
    private Long sourceId;

    private LocalDateTime createdAt;

    public static AccountsPayableResponseDTO from(AccountsPayable ap) {
        return AccountsPayableResponseDTO.builder()
                .id(ap.getId())
                .creditorEntityId(ap.getCreditor().getId())
                .debtorEntityId(ap.getDebtor().getId())
                .totalAmount(ap.getTotalAmount())
                .balance(ap.getBalance())
                .sourceType(ap.getSourceType())
                .sourceId(ap.getSourceId())
                .createdAt(ap.getCreatedAt())
                .build();
    }
}
