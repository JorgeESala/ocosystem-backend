package com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    private Long creditSolicitorid;
    private String creditSolicitorName;

    private LocalDate date;

    public static AccountsPayableResponseDTO from(AccountsPayable ap) {
        return AccountsPayableResponseDTO.builder()
                .id(ap.getId())
                .creditorEntityId(ap.getCreditor().getId())
                .debtorEntityId(ap.getDebtor().getId())
                .totalAmount(ap.getTotalAmount())
                .balance(ap.getBalance())
                .sourceType(ap.getSourceType())
                .sourceId(ap.getSourceId())
                .creditSolicitorid(
                        ap.getCreditSolicitor() != null ? ap.getCreditSolicitor().getId() : null)
                .creditSolicitorName(
                        ap.getCreditSolicitor() != null ? ap.getCreditSolicitor().getName() : null)
                .date(ap.getDate())
                .build();
    }
}
