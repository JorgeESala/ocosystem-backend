package com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto;


import lombok.Getter;

import java.math.BigDecimal;

import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountsPayableSourceType;

@Getter
public class CreateAccountsPayableRequestDTO {

    // creditor
    private AccountingEntityType creditorType;
    private Long creditorEntityId;

    // debtor
    private AccountingEntityType debtorType;
    private Long debtorEntityId;

    // amount
    private BigDecimal amount;

    // optional source
    private AccountsPayableSourceType sourceType;
    private Long sourceId;

    private String notes;
}