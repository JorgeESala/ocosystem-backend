package com.ocosur.ocosystem.livechicken.accounting.accounts_payable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsPayableRepository
        extends JpaRepository<AccountsPayable, Long> {

    @Query("""
                SELECT ap
                FROM AccountsPayable ap
                WHERE ap.debtor = :debtor
                  AND ap.creditor = :creditor
                  AND ap.balance > 0
                ORDER BY ap.createdAt ASC
            """)
    List<AccountsPayable> findOpenByDebtorAndCreditorOrderByCreatedAtAsc(
            AccountingEntity debtor,
            AccountingEntity creditor);

    List<AccountsPayable> findByDebtorAndCreditorAndBalanceGreaterThanOrderByCreatedAtAsc(
            AccountingEntity debtor,
            AccountingEntity creditor,
            BigDecimal balance);
}
