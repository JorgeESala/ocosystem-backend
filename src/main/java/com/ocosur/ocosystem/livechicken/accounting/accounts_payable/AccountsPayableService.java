package com.ocosur.ocosystem.livechicken.accounting.accounts_payable;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityRepository;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityService;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto.CreateAccountsPayableRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountsPayableSourceType;
import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.CreditSolicitor;
import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.CreditSolicitorRepository;
import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.CreditSolicitorService;
import com.ocosur.ocosystem.livechicken.accounting.payment.Payment;
import com.ocosur.ocosystem.livechicken.accounting.payment_application.PaymentApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsPayableService {

        private final AccountsPayableRepository accountsPayableRepository;
        private final PaymentApplicationService paymentApplicationService;
        private final AccountingEntityService accountingEntityService;
        private final CreditSolicitorService creditSolicitorService;

        // ------------------------------------------------------------------
        // Creation
        // ------------------------------------------------------------------

        @Transactional
        public AccountsPayable createDebt(CreateAccountsPayableRequestDTO request) {
                AccountingEntity creditor = accountingEntityService.getById
                (request.getCreditorEntityId());
                AccountingEntity debtor = accountingEntityService.getById
                (request.getDebtorEntityId());

                CreditSolicitor solicitor = creditSolicitorService.getById(request.getSolicitorId());

                AccountsPayable ap = AccountsPayable.create(
                        creditor,
                        debtor,
                        request.getAmount(),
                        request.getSourceType(),
                        request.getSourceId(),
                        solicitor,
                        request.getNotes(),
                        request.getDate()
                        
                );

                return accountsPayableRepository.save(ap);
        }

        // ------------------------------------------------------------------
        // Queries
        // ------------------------------------------------------------------

        public List<AccountsPayable> findOpenByDebtorAndCreditor(
                        AccountingEntity debtor,
                        AccountingEntity creditor) {
                return accountsPayableRepository
                                .findOpenByDebtorAndCreditorOrderByCreatedAtAsc(
                                                debtor,
                                                creditor);
        }

        public AccountsPayable getById(Long id) {
                return accountsPayableRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "AccountsPayable not found with id: " + id));
        }

        // ------------------------------------------------------------------
        // FIFO orchestration
        // ------------------------------------------------------------------

        /**
         * Apply a payment to all open debts using FIFO (oldest first).
         * This method DOES NOT create the payment.
         */
        @Transactional
        public void applyPaymentFifo(
                        Payment payment,
                        AccountingEntity debtor,
                        AccountingEntity creditor,
                        String notes) {
                BigDecimal remaining = payment.getAmount();

                List<AccountsPayable> openDebts = findOpenByDebtorAndCreditor(debtor, creditor);

                for (AccountsPayable ap : openDebts) {

                        if (remaining.signum() <= 0) {
                                break;
                        }

                        BigDecimal toApply = remaining.min(ap.getBalance());

                        paymentApplicationService.applyPayment(
                                        payment,
                                        ap,
                                        toApply,
                                        notes);

                        remaining = remaining.subtract(toApply);
                }

                if (remaining.signum() > 0) {
                        throw new IllegalStateException(
                                        "Payment amount exceeds total open debt");
                }
        }
}
