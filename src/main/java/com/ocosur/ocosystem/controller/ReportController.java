package com.ocosur.ocosystem.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.dto.DailyReportDTO;
import com.ocosur.ocosystem.dto.MonthlyCategoryReportDTO;
import com.ocosur.ocosystem.dto.MonthlyReportDTO;
import com.ocosur.ocosystem.dto.WeeklyCategoryReportDTO;
import com.ocosur.ocosystem.dto.WeeklyChickenReportDTO;
import com.ocosur.ocosystem.dto.WeeklyReportDTO;
import com.ocosur.ocosystem.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;



    @GetMapping("/weekly")
    public WeeklyReportDTO getWeeklyReport(
        @RequestParam Integer branchId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start
    ) {
        return reportService.getWeeklyReport(branchId, start);
    }

    @GetMapping("/weekly/chicken")
    public WeeklyReportDTO getWeeklyChickenReport(
        @RequestParam Integer branchId,
        @RequestParam Integer categoryId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start
    ) {
        return reportService.getWeeklyReportByCategory(branchId, categoryId, start);
    }

    @GetMapping("/monthly")
    public MonthlyReportDTO getMonthlyReport(
            @RequestParam Integer branchId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return reportService.getMonthlyReport(branchId, year, month);
    }


    @GetMapping("/monthly-category")
    public MonthlyCategoryReportDTO getMonthlyCategoryReport(
            @RequestParam Integer branchId,
            @RequestParam Integer categoryId,
            @RequestParam Integer year,
            @RequestParam Integer month) {

    
        return reportService.getMonthlyReportByCategoryWithWeeks(branchId, categoryId, year, month);
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyReportDTO> getDailyReport(
            @RequestParam Integer branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        // Convertir LocalDate a OffsetDateTime
        OffsetDateTime dateTime = date.atStartOfDay().atOffset(ZoneOffset.UTC);

        DailyReportDTO report = reportService.getDailyReport(branchId, dateTime);
        return ResponseEntity.ok(report);
    }

}
