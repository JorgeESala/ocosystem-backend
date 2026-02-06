package com.ocosur.ocosystem.livechicken.accounting.read;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableAgingDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableHistoryDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.AccountsPayableOpenDTO;
import com.ocosur.ocosystem.livechicken.accounting.read.dto.EntityAccountStatementDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/read/accounts-payable")
@RequiredArgsConstructor
public class AccountsPayableReadController {

    private final AccountsPayableReadService service;

    @GetMapping("/open")
    public List<AccountsPayableOpenDTO> open(
            @RequestParam(required = false) Long debtorId,
            @RequestParam(required = false) Long creditorId) {
        return service.getOpen(debtorId, creditorId);
    }

    @GetMapping("/{id}/history")
    public List<AccountsPayableHistoryDTO> history(
            @PathVariable Long id,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getHistory(id);
    }

    @GetMapping("/aging")
    public List<AccountsPayableAgingDTO> aging(
            @RequestParam(required = false) Long debtorId,
            @RequestParam(required = false) Long creditorId) {
        return service.getAging(debtorId, creditorId);
    }

    @GetMapping("/entity-statement")
    public List<EntityAccountStatementDTO> entityAccountStatement(
            @RequestParam(required = false) Long debtorId,
            @RequestParam(required = false) Long creditorId,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getEntityAccountStatement(debtorId, creditorId, from, to);
    }

}
