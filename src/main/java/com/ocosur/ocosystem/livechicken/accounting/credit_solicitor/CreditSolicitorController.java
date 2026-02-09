package com.ocosur.ocosystem.livechicken.accounting.credit_solicitor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.dto.CreditSolicitorResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/credit-solicitors")
@RequiredArgsConstructor
public class CreditSolicitorController {

    private final CreditSolicitorService service;

    @GetMapping
    public List<CreditSolicitorResponseDTO> getAllActive() {
        return service.getAll();
    }
}
