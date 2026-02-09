package com.ocosur.ocosystem.livechicken.accounting.accounts_payable;

import org.springframework.web.bind.annotation.*;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityService;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto.AccountsPayableResponseDTO;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.dto.CreateAccountsPayableRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.common.AccountingEntityType;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/accounts-payable")
@RequiredArgsConstructor
public class AccountsPayableController {

        private final AccountsPayableService accountsPayableService;
        private final AccountingEntityService accountingEntityService;

        // --------------------------------------------------
        // Get open debts between two entities
        // --------------------------------------------------

        @GetMapping("/open")
        public List<AccountsPayableResponseDTO> getOpenDebts(
                        @RequestParam AccountingEntityType debtorType,
                        @RequestParam Long debtorId,
                        @RequestParam AccountingEntityType creditorType,
                        @RequestParam Long creditorId) {
                AccountingEntity debtor = accountingEntityService.getByTypeAndEntityId(
                                debtorType,
                                debtorId);

                AccountingEntity creditor = accountingEntityService.getByTypeAndEntityId(
                                creditorType,
                                creditorId);

                return accountsPayableService
                                .findOpenByDebtorAndCreditor(debtor, creditor)
                                .stream()
                                .map(AccountsPayableResponseDTO::from)
                                .toList();
        }

        // --------------------------------------------------
        // Get single accounts payable detail
        // --------------------------------------------------

        @GetMapping("/{id}")
        public AccountsPayableResponseDTO getById(@PathVariable Long id) {
                return AccountsPayableResponseDTO.from(
                                accountsPayableService.getById(id));
        }

        @PostMapping
        public AccountsPayableResponseDTO create(
                        @RequestBody CreateAccountsPayableRequestDTO request) {


                return AccountsPayableResponseDTO.from(
                                accountsPayableService.createDebt(request));
        }
}
