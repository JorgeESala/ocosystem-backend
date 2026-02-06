package com.ocosur.ocosystem.livechicken.accounting.common;

public enum AccountsPayableSourceType {
    BATCH,      // Supplier -> CEDIS
    DELIVERY,   // CEDIS -> Branch
    ADJUSTMENT,  // Manual / correction
    OTHER
}