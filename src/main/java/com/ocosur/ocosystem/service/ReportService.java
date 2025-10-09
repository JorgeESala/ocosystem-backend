package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.DailyReportDTO;
import com.ocosur.ocosystem.dto.MonthlyCategoryReportDTO;
import com.ocosur.ocosystem.dto.MonthlyReportDTO;
import com.ocosur.ocosystem.dto.ProductReportDTO;
import com.ocosur.ocosystem.dto.WeeklyReportDTO;
import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.model.Product;
import com.ocosur.ocosystem.model.Sale;
import com.ocosur.ocosystem.model.Ticket;
import com.ocosur.ocosystem.repository.ExpenseRepository;
import com.ocosur.ocosystem.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
        private final TicketRepository ticketRepository;
        private final ExpenseRepository expenseRepository;

        private final Integer GUT_CATEGORY = 5;
        private final Integer SLAUGHTERED_CHICKEN = 6;
        private final Integer EGG_CATEGORY = 7;
        private final Integer EGG_CARTON_CATEGORY = 8;
        private final Integer ALL_EGGS_CATEGORY = 9;

        private OffsetDateTime getEndOfWeek(OffsetDateTime start) {
                DayOfWeek dayOfWeek = start.getDayOfWeek();
                int daysUntilSaturday = DayOfWeek.SATURDAY.getValue() - dayOfWeek.getValue();
                if (daysUntilSaturday < 0) {
                        daysUntilSaturday += 7;
                }

                return start.plusDays(daysUntilSaturday)
                                .withHour(23)
                                .withMinute(59)
                                .withSecond(59)
                                .withNano(999999999);
        }

        private OffsetDateTime getWeekStart(OffsetDateTime dateTime) {
                LocalDate weekStartDate = dateTime.toLocalDate()
                                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                return weekStartDate.atStartOfDay().atOffset(dateTime.getOffset());
        }

        public List<LocalDate> getSaturdays(int year, int month) {
                YearMonth ym = YearMonth.of(year, month);
                List<LocalDate> saturdays = new ArrayList<>();

                for (int day = 1; day <= ym.lengthOfMonth(); day++) {
                        LocalDate date = ym.atDay(day);
                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                                saturdays.add(date);
                        }
                }

                return saturdays;
        }

        public List<ProductReportDTO> getProductsReport(List<Sale> sales) {
                // Agrupar ventas por producto
                Map<Product, List<Sale>> salesByProduct = sales.stream()
                                .collect(Collectors.groupingBy(Sale::getProduct));

                // Crear lista de ProductReportDTO
                List<ProductReportDTO> productReports = salesByProduct.entrySet().stream()
                                .map(entry -> {
                                        Product product = entry.getKey();
                                        List<Sale> productSales = entry.getValue();

                                        BigDecimal totalQuantity = productSales.stream()
                                                        .map(Sale::getQuantity)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        BigDecimal totalProductSales = productSales.stream()
                                                        .map(Sale::getSubtotal)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        ProductReportDTO dto = new ProductReportDTO();
                                        dto.setProduct(product);
                                        dto.setQuantitySold(totalQuantity);
                                        dto.setTotalSales(totalProductSales);
                                        return dto;
                                })
                                .toList();
                return productReports;

        }

        public DailyReportDTO getDailyReport(Integer branchId, OffsetDateTime date) {

                ZoneId zone = ZoneId.of("America/Mexico_City");
                LocalDate localDate = date.toLocalDate();
                OffsetDateTime start = localDate.atStartOfDay(zone).toOffsetDateTime();
                OffsetDateTime end = localDate.plusDays(1).atStartOfDay(zone).minusNanos(1).toOffsetDateTime();

                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateBetween(branchId, start, end);
                List<Sale> sales = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .toList();
                // Gastos del día
                List<Expense> expenses = expenseRepository.findByBranchIdAndDateBetween(branchId, start, end);

                // Totales
                BigDecimal totalSales = tickets.stream()
                                .map(Ticket::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpenses = expenses.stream()
                                .map(Expense::getPaid)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalProfit = totalSales.subtract(totalExpenses);

                BigDecimal totalSold = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalBought = expenses.stream()
                                .map(e -> e.getQuantity() == null ? BigDecimal.ZERO : e.getQuantity())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Solo si aplica (ej. categoría pollo)
                BigDecimal gut = BigDecimal.ZERO;
                BigDecimal slaughtered = BigDecimal.ZERO;
                BigDecimal waste = BigDecimal.ZERO;

                List<Sale> gutSales = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .filter(s -> s.getProduct().getCategory().getId().equals(GUT_CATEGORY))
                                .toList();
                List<Sale> slaughteredSales = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .filter(s -> s.getProduct().getCategory().getId().equals(SLAUGHTERED_CHICKEN))
                                .toList();
                if (!gutSales.isEmpty()) {
                        gut = gutSales.stream()
                                        .map(Sale::getQuantity)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        waste = totalBought.subtract(gut).subtract(totalSold);
                }
                if (!slaughteredSales.isEmpty()) {
                        slaughtered = slaughteredSales.stream()
                                        .map(Sale::getQuantity)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                }
                BigDecimal totalEggsSale = sales.stream()
                                .filter(s -> {
                                        Integer parentId = s.getProduct().getCategory().getParent_id();
                                        return parentId != null && parentId.equals(ALL_EGGS_CATEGORY);
                                })
                                .map(Sale::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal eggs = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal eggCarton = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CARTON_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                // Ventas por categoría
                Map<String, BigDecimal> salesByCategory = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getCategory().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Ventas por producto
                Map<String, BigDecimal> salesByProduct = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Cantidad vendida por producto
                Map<String, BigDecimal> quantitiesByProduct = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getQuantity, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Gastos por categoría
                Map<String, BigDecimal> expensesByCategory = expenses.stream()
                                .collect(Collectors.groupingBy(
                                                e -> e.getCategory().getName(),
                                                Collectors.mapping(Expense::getPaid, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Construir DTO
                return new DailyReportDTO(
                                branchId,
                                start,
                                totalSales,
                                totalExpenses,
                                totalProfit,
                                totalSold,
                                totalBought,
                                gut,
                                waste,
                                slaughtered,
                                eggs,
                                eggCarton,
                                totalEggsSale,
                                salesByCategory,
                                salesByProduct,
                                quantitiesByProduct,
                                expensesByCategory);
        }

        public WeeklyReportDTO getWeeklyReport(Integer branchId, OffsetDateTime date, Boolean includeDays) {
                OffsetDateTime start = getWeekStart(date);
                OffsetDateTime end = getEndOfWeek(start);
                List<DailyReportDTO> dailyReports = new ArrayList<>();

                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateBetween(branchId, start, end);
                List<Expense> expenses = expenseRepository.findByBranchIdAndDateBetween(branchId, start, end);
                List<Sale> sales = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .toList();
                List<ProductReportDTO> productReports = new ArrayList<>();
                // Total ventas
                BigDecimal totalSales = tickets.stream()
                                .map(Ticket::getTotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Total gastos
                BigDecimal totalExpenses = expenses.stream()
                                .map(Expense::getPaid)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Ventas por categoría de producto
                Map<String, BigDecimal> salesByCategory = tickets.stream()
                                .flatMap(ticket -> ticket.getSales().stream())
                                .collect(Collectors.groupingBy(
                                                sale -> sale.getProduct().getCategory().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Ventas por producto
                Map<String, BigDecimal> salesByProduct = tickets.stream()
                                .flatMap(ticket -> ticket.getSales().stream())
                                .collect(Collectors.groupingBy(
                                                sale -> sale.getProduct().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Gastos por categoría de gastos
                Map<String, BigDecimal> expensesByCategory = expenses.stream()
                                .collect(Collectors.groupingBy(
                                                e -> e.getCategory().getName(),
                                                Collectors.mapping(Expense::getPaid, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                BigDecimal eggs = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal eggCarton = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CARTON_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalEggsSale = sales.stream()
                                .filter(s -> {
                                        Integer parentId = s.getProduct().getCategory().getParent_id();
                                        return parentId != null && parentId.equals(ALL_EGGS_CATEGORY);
                                })
                                .map(Sale::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalProfit = totalSales.subtract(totalExpenses);

                // Cantidades
                BigDecimal totalSold = sales.stream()
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalBought = expenses.stream()
                                .map(e -> e.getQuantity() == null ? BigDecimal.ZERO : e.getQuantity())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                // Cantidad vendida por producto
                Map<String, BigDecimal> quantitiesByProduct = sales.stream()
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getQuantity, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));
                productReports = getProductsReport(sales);
                if (includeDays) {
                        OffsetDateTime current = start;
                        while (!current.isAfter(end)) {
                                dailyReports.add(getDailyReport(branchId, current));
                                current = current.plusDays(1);
                        }
                }
                return new WeeklyReportDTO(
                                branchId,
                                start,
                                totalSales,
                                totalExpenses,
                                totalProfit,
                                totalSold,
                                totalBought,
                                null,
                                null,
                                eggs,
                                eggCarton,
                                totalEggsSale,
                                salesByCategory,
                                salesByProduct,
                                expensesByCategory,
                                dailyReports,
                                productReports);
        }

        public WeeklyReportDTO getWeeklyReportByCategory(Integer branchId, Integer categoryId, OffsetDateTime date,
                        Boolean includeDays) {
                OffsetDateTime start = getWeekStart(date);
                OffsetDateTime end = getEndOfWeek(start);
                BigDecimal gut = null;
                BigDecimal waste = null;
                // Tickets de la semana
                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateBetween(branchId, start, end);
                List<DailyReportDTO> dailyReports = new ArrayList<>();
                // Filtrar ventas SOLO de la categoría pedida
                List<Sale> salesInCategory = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .filter(s -> s.getProduct().getCategory().getId().equals(categoryId))
                                .toList();

                // Cantidad vendida por producto
                Map<String, BigDecimal> quantitiesByProduct = salesInCategory.stream()
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getQuantity, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Cantidades
                BigDecimal totalSold = salesInCategory.stream()
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                List<Expense> expenses = Collections.emptyList();
                if (categoryId == 1) {
                        expenses = expenseRepository.findByBranchIdAndDateBetween(branchId, start, end).stream()
                                        .filter(e -> e.getCategory() != null
                                                        && e.getCategory().getId().equals(categoryId))
                                        .toList();

                }
                BigDecimal totalBought = expenses.stream()
                                .map(e -> e.getQuantity() == null ? BigDecimal.ZERO : e.getQuantity())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (categoryId == 1) {
                        List<Sale> sales = tickets.stream()
                                        .flatMap(t -> t.getSales().stream())
                                        .filter(s -> s.getProduct().getCategory().getId().equals(GUT_CATEGORY))
                                        .toList();
                        gut = sales.stream()
                                        .filter(s -> s.getProduct().getCategory().getId().equals(GUT_CATEGORY))
                                        .map(Sale::getQuantity)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        waste = totalBought.subtract(totalSold).subtract(gut);
                }
                // Totales
                BigDecimal totalSales = salesInCategory.stream()
                                .map(Sale::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpenses = expenses.stream()
                                .map(Expense::getPaid)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalProfit = totalSales.subtract(totalExpenses);

                // Ventas por producto
                Map<String, BigDecimal> salesByCategory = salesInCategory.stream()
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Gastos por categoría (solo para pollo)
                Map<String, BigDecimal> expensesByCategory = expenses.stream()
                                .collect(Collectors.groupingBy(
                                                e -> e.getCategory().getName(),
                                                Collectors.mapping(Expense::getPaid, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                if (includeDays) {
                        OffsetDateTime current = start;
                        while (!current.isAfter(end)) {
                                dailyReports.add(getDailyReport(branchId, current));
                                current = current.plusDays(1);
                        }
                }
                return new WeeklyReportDTO(
                                branchId,
                                start,
                                totalSales,
                                totalExpenses,
                                totalProfit,
                                totalSold,
                                totalBought,
                                gut,
                                waste,
                                null,
                                null,
                                null,
                                salesByCategory,
                                quantitiesByProduct,
                                expensesByCategory,
                                dailyReports, null);
        }

        public MonthlyCategoryReportDTO getMonthlyReportByCategoryWithWeeks(Integer branchId, Integer categoryId,
                        int year,
                        int month) {
                ZoneId zone = ZoneId.of("America/Mexico_City");

                YearMonth ym = YearMonth.of(year, month);
                OffsetDateTime monthStart = ym.atDay(1).atStartOfDay(zone).toOffsetDateTime();
                OffsetDateTime start = getWeekStart(monthStart);
                LocalDate endOfMonth = ym.atEndOfMonth();
                LocalDate lastSaturday = endOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
                OffsetDateTime end = lastSaturday.atTime(23, 59, 59).atZone(zone).toOffsetDateTime();
                BigDecimal gut = null;
                BigDecimal waste = null;
                final Boolean INCLUDE_DAYS = false;
                // Tickets filtrados por sucursal y rango
                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateBetween(branchId, start, end);

                // Filtrar ventas SOLO de la categoría seleccionada
                List<Sale> salesInCategory = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .filter(s -> s.getProduct().getCategory().getId().equals(categoryId))
                                .toList();

                // Total vendido (cantidad) solo tiene sentido para pollo
                BigDecimal totalSold = salesInCategory.stream()
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Gastos (solo aplican si la categoría es pollo -> id = 1)
                List<Expense> expenses = Collections.emptyList();
                if (categoryId == 1) {
                        expenses = expenseRepository.findByBranchIdAndDateBetween(branchId, start, end).stream()
                                        .filter(e -> e.getCategory() != null
                                                        && e.getCategory().getId().equals(categoryId))
                                        .toList();
                }

                // Totales
                BigDecimal totalSales = salesInCategory.stream()
                                .map(Sale::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpenses = expenses.stream()
                                .map(Expense::getPaid)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalProfit = totalSales.subtract(totalExpenses);

                BigDecimal totalBought = expenses.stream()
                                .map(e -> e.getQuantity() == null ? BigDecimal.ZERO : e.getQuantity())
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (categoryId == 1) {
                        List<Sale> sales = tickets.stream()
                                        .flatMap(t -> t.getSales().stream())
                                        .filter(s -> s.getProduct().getCategory().getId().equals(GUT_CATEGORY))
                                        .toList();
                        gut = sales.stream()
                                        .filter(s -> s.getProduct().getCategory().getId().equals(GUT_CATEGORY))
                                        .map(Sale::getQuantity)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        waste = totalBought.subtract(gut).subtract(totalSold);
                }
                // Ventas por producto (subtotal vendido por producto en la categoría)
                Map<String, BigDecimal> salesByProduct = salesInCategory.stream()
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getSubtotal, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Cantidad vendida por producto
                Map<String, BigDecimal> quantitiesByProduct = salesInCategory.stream()
                                .collect(Collectors.groupingBy(
                                                s -> s.getProduct().getName(),
                                                Collectors.mapping(Sale::getQuantity, Collectors
                                                                .reducing(BigDecimal.ZERO, BigDecimal::add))));

                // Reportes semanales de esa categoría
                List<WeeklyReportDTO> weeklyReports = new ArrayList<>();
                OffsetDateTime weekStart = start;
                while (!weekStart.isAfter(end)) {
                        WeeklyReportDTO weekly = getWeeklyReportByCategory(branchId, categoryId, weekStart,
                                        INCLUDE_DAYS);
                        weeklyReports.add(weekly);
                        weekStart = weekStart.plusWeeks(1);
                }

                return new MonthlyCategoryReportDTO(
                                branchId,
                                ym,
                                categoryId,
                                totalSales,
                                totalExpenses,
                                totalProfit,
                                totalSold,
                                totalBought,
                                gut,
                                waste,
                                salesByProduct,
                                quantitiesByProduct,
                                weeklyReports);
        }

        public MonthlyReportDTO getMonthlyReport(Integer branchId, int year, int month) {
                YearMonth ym = YearMonth.of(year, month);
                final Boolean INCLUDE_DAYS = false;
                OffsetDateTime start = ym.atDay(1).atStartOfDay().atOffset(ZoneOffset.ofHours(-5));
                OffsetDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX).atOffset(ZoneOffset.ofHours(-5));

                BigDecimal totalSales = BigDecimal.ZERO;
                BigDecimal totalExpenses = BigDecimal.ZERO;
                Map<String, BigDecimal> salesByCategory = new HashMap<>();
                Map<String, BigDecimal> expensesByCategory = new HashMap<>();
                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateBetween(branchId, start, end);
                List<WeeklyReportDTO> weeklyReports = new ArrayList<>();
                List<ProductReportDTO> productReports = new ArrayList<>();
                OffsetDateTime weekStart = start;
                while (!weekStart.isAfter(end)) {
                        WeeklyReportDTO weekly = getWeeklyReport(branchId, weekStart, INCLUDE_DAYS);

                        totalSales = totalSales.add(weekly.getTotalSales());
                        totalExpenses = totalExpenses.add(weekly.getTotalExpenses());

                        // acumular desgloses
                        weekly.getSalesByCategory().forEach(
                                        (cat, val) -> salesByCategory.merge(cat, val, BigDecimal::add));
                        weekly.getExpensesByCategory().forEach(
                                        (cat, val) -> expensesByCategory.merge(cat, val, BigDecimal::add));

                        weeklyReports.add(weekly);
                        weekStart = weekStart.plusWeeks(1);
                }
                List<Sale> sales = tickets.stream()
                                .flatMap(t -> t.getSales().stream())
                                .toList();

                BigDecimal eggs = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalEggsSale = sales.stream()
                                .filter(s -> {
                                        Integer parentId = s.getProduct().getCategory().getParent_id();
                                        return parentId != null && parentId.equals(ALL_EGGS_CATEGORY);
                                })
                                .map(Sale::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal eggCarton = sales.stream()
                                .filter(s -> s.getProduct().getCategory().getId().equals(EGG_CARTON_CATEGORY))
                                .map(Sale::getQuantity)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalProfit = totalSales.subtract(totalExpenses);

                productReports = getProductsReport(sales);
                return new MonthlyReportDTO(
                                branchId,
                                ym.toString(),
                                totalSales,
                                totalExpenses,
                                totalProfit,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                eggs,
                                eggCarton,
                                totalEggsSale,
                                salesByCategory,
                                expensesByCategory,
                                weeklyReports,
                                productReports);
        }

}
