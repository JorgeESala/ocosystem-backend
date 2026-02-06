package com.ocosur.ocosystem.livechicken.accounting.accounting_entity.dto;

import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;

import lombok.Data;

@Data
public class AccountingEntityResponseDTO {
    private Long id;
    private String name;
    private AccountingEntityType entityType;
    private Long entityId;
}
