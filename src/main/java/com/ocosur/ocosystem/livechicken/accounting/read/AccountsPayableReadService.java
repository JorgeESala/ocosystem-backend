package com.ocosur.ocosystem.livechicken.accounting.read;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableAgingDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableHistoryDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableOpenDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.EntityAccountStatementDTO;

@Service
public class AccountsPayableReadService {

    private final AccountsPayableReadRepository repo;

    public AccountsPayableReadService(AccountsPayableReadRepository repo) {
        this.repo = repo;
    }

    public List<AccountsPayableOpenDTO> getOpen(Long debtorId, Long creditorId) {
        return repo.findOpen(debtorId, creditorId);
    }

    public List<AccountsPayableHistoryDTO> getHistory(
            Long accountsPayableId) {
        return repo.findHistory(accountsPayableId);
    }

    public List<AccountsPayableAgingDTO> getAging(Long debtorId, Long creditorId) {
        return repo.findAging(debtorId, creditorId);
    }

    public List<EntityAccountStatementDTO> getEntityAccountStatement(
            Long debtorEntityId,
            Long creditorEntityId,
            LocalDate from,
            LocalDate to) {
       
        return repo.findEntityAccountStatement(debtorEntityId, creditorEntityId, from, to);
    }

}
