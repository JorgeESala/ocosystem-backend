package com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement;

public enum AccountsPayableMovementType {
    PAYMENT, // decreases debt
    COMPENSATION, 
    ADJUSTMENT,
    OTHER // manual correction
}