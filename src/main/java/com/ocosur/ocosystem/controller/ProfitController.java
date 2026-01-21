package com.ocosur.ocosystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.dto.ProfitReport;
import com.ocosur.ocosystem.security.RequireBusiness;
import com.ocosur.ocosystem.service.ProfitService;
@RestController
@CrossOrigin
@RequestMapping("/api/reports")
@RequireBusiness("BRANCHES")
public class ProfitController {

    private final ProfitService profitService;

    public ProfitController(ProfitService profitService) {
        this.profitService = profitService;
    }

    @GetMapping("/profit")
    public ResponseEntity<ProfitReport> getProfit(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam("branchIds") List<Long> branchIds
    ) {
        return ResponseEntity.ok(
                profitService.calculateProfit(start, end, branchIds)
        );
    }
}
