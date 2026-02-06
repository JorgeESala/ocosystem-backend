package com.ocosur.ocosystem.livechicken.accounting.accounting_entity;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.dto.AccountingEntityResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounting/entities")
@RequiredArgsConstructor
public class AccountingEntityController {
    private final AccountingEntityService accountingEntityService;

    @GetMapping
    public List<AccountingEntityResponseDTO> getAll() {
        return accountingEntityService.getAll();
    }

}
