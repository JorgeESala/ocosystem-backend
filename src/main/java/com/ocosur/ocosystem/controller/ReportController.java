package com.ocosur.ocosystem.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

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
import com.ocosur.ocosystem.dto.ReportEntryDTO;
import com.ocosur.ocosystem.dto.TimeSeriesPointDTO;
import com.ocosur.ocosystem.dto.WeeklyReportDTO;
import com.ocosur.ocosystem.security.RequireBusiness;
import com.ocosur.ocosystem.service.ReportService;

@CrossOrigin
@RestController
@RequestMapping("/api/reports")
@RequireBusiness("BRANCHES")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/weekly")
    public WeeklyReportDTO getWeeklyCalendarReport(
            @RequestParam Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date) {
        return reportService.getWeeklyReport(branchId, date, true);
    }

    @GetMapping("/weekly-category")
    public WeeklyReportDTO getWeeklyCategoryReport(
            @RequestParam Long branchId,
            @RequestParam Integer categoryId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date) {

        return reportService.getWeeklyReportByCategory(branchId, categoryId, date, true);
    }

    @GetMapping("/monthly")
    public MonthlyReportDTO getMonthlyReport(
            @RequestParam Long branchId,
            @RequestParam int year,
            @RequestParam int month) {
        return reportService.getMonthlyReport(branchId, year, month);
    }

    @GetMapping("/monthly-category")
    public MonthlyCategoryReportDTO getMonthlyCategoryReport(
            @RequestParam Long branchId,
            @RequestParam Integer categoryId,
            @RequestParam Integer year,
            @RequestParam Integer month) {

        return reportService.getMonthlyReportByCategoryWithWeeks(branchId, categoryId, year, month);
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyReportDTO> getDailyReport(
            @RequestParam Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Convertir LocalDate a OffsetDateTime
        OffsetDateTime dateTime = date.atStartOfDay().atOffset(ZoneOffset.UTC);

        DailyReportDTO report = reportService.getDailyReport(branchId, dateTime);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<?> getReports(
            @RequestParam List<Long> branchId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String metric,
            @RequestParam Boolean includeCategories,
            @RequestParam(required = false) List<String> categories,
            @RequestParam String frequency,
            @RequestParam Boolean compareSelf) {
        try {
            // Parsear fechas
            OffsetDateTime start = OffsetDateTime.parse(startDate);
            OffsetDateTime end = OffsetDateTime.parse(endDate);

            List<ReportEntryDTO> reports;
            // 🔹 Si el frequency es "weekly_custom", usa la nueva función
            if (frequency.equalsIgnoreCase("weekly_custom") || frequency.equalsIgnoreCase("daily_custom")) {
                reports = reportService.getReportsCustom(branchId, start, end, frequency, metric, true, categories,
                        compareSelf);

            } else {
                reports = reportService.getReportsCustom(branchId, start, end, frequency, metric, false, categories,
                        compareSelf);

            }
            List<Map<String, Object>> formatted = reportService.formatForChart(reports, frequency, metric,
                    includeCategories, categories, compareSelf);

            return ResponseEntity.ok(formatted);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/time-series")
    public ResponseEntity<?> getReportsTimeSeries(
            @RequestParam List<Long> branchId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String metric,
            @RequestParam String frequency,
            @RequestParam(required = false) List<String> categories,
            @RequestParam Boolean compareSelf) {
        try {
            OffsetDateTime start = OffsetDateTime.parse(startDate);
            OffsetDateTime end = OffsetDateTime.parse(endDate);
            Boolean comparePrevious = false;

            if (frequency.equalsIgnoreCase("weekly_custom") || frequency.equalsIgnoreCase("daily_custom")) {
                comparePrevious = true;
            }
            
            List<ReportEntryDTO> reports = reportService.getReportsCustom(
                    branchId,
                    start,
                    end,
                    frequency,
                    metric,
                    comparePrevious,
                    categories,
                    compareSelf);

            List<TimeSeriesPointDTO> response = reportService.formatForTimeSeries(reports, metric);

            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
