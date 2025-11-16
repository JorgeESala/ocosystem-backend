package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.ocosur.ocosystem.dto.DailyReportDTO;
import com.ocosur.ocosystem.dto.MonthlyCategoryReportDTO;
import com.ocosur.ocosystem.dto.MonthlyReportDTO;
import com.ocosur.ocosystem.dto.ProductReportDTO;
import com.ocosur.ocosystem.dto.ReportEntryDTO;
import com.ocosur.ocosystem.dto.WeeklyReportDTO;
import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.model.Product;
import com.ocosur.ocosystem.model.Sale;
import com.ocosur.ocosystem.model.Ticket;
import com.ocosur.ocosystem.repository.BranchRepository;
import com.ocosur.ocosystem.repository.ExpenseRepository;
import com.ocosur.ocosystem.repository.SaleRepository;
import com.ocosur.ocosystem.repository.TicketRepository;
import com.ocosur.ocosystem.util.ReportUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
        private final TicketRepository ticketRepository;
        private final ExpenseRepository expenseRepository;
        private final BranchRepository branchRepository;
        private final SaleRepository saleRepository;
        private static final Logger log = LoggerFactory.getLogger(ReportService.class);

        private final Integer GUT_CATEGORY = 5;
        private final Integer SLAUGHTERED_CHICKEN = 6;
        private final Integer EGG_CATEGORY = 7;
        private final Integer EGG_CARTON_CATEGORY = 8;
        private final Integer ALL_EGGS_CATEGORY = 9;

        private static final ZoneOffset MEXICO_ZONE = ZoneOffset.of("-05:00");

        private OffsetDateTime getEndOfWeek(OffsetDateTime start) {
                DayOfWeek dayOfWeek = start.atZoneSameInstant(MEXICO_ZONE).getDayOfWeek();
                int daysUntilSaturday = DayOfWeek.SATURDAY.getValue() - dayOfWeek.getValue();
                if (daysUntilSaturday < 0) {
                        daysUntilSaturday += 7;
                }

                return start.atZoneSameInstant(MEXICO_ZONE)
                                .plusDays(daysUntilSaturday)
                                .withHour(23)
                                .withMinute(59)
                                .withSecond(59)
                                .withNano(999_999_999)
                                .toOffsetDateTime();
        }

        private OffsetDateTime getWeekStart(OffsetDateTime dateTime) {
                LocalDate weekStartDate = dateTime.atZoneSameInstant(MEXICO_ZONE)
                                .toLocalDate()
                                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                return weekStartDate.atStartOfDay(MEXICO_ZONE).toOffsetDateTime();
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
                OffsetDateTime startLocal = ReportUtils.getStartOfDay(date);
                OffsetDateTime endLocal = startLocal.plusDays(1); // 👈 fin exclusivo

                OffsetDateTime start = startLocal.withOffsetSameInstant(ZoneOffset.UTC);
                OffsetDateTime end = endLocal.withOffsetSameInstant(ZoneOffset.UTC);

                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateGreaterThanEqualAndDateLessThan(branchId,
                                start, end);
                List<Sale> sales = tickets.stream().flatMap(t -> t.getSales().stream()).toList();
                List<Expense> expenses = expenseRepository.findByBranchIdAndDateGreaterThanEqualAndDateLessThan(
                                branchId, start,
                                end);

                // --- Totales ---
                BigDecimal totalSales = ReportUtils.sumTicketsTotal(tickets);
                BigDecimal totalExpenses = ReportUtils.sumExpensesPaid(expenses);
                BigDecimal totalProfit = ReportUtils.calculateProfit(totalSales, totalExpenses);
                BigDecimal totalSold = ReportUtils.sumSalesQuantity(sales);
                BigDecimal totalBought = ReportUtils.sumExpensesQuantity(expenses);

                BigDecimal gut = ReportUtils.sumByCategoryId(sales, GUT_CATEGORY);
                BigDecimal slaughtered = ReportUtils.sumByCategoryId(sales, SLAUGHTERED_CHICKEN);
                BigDecimal waste = ReportUtils.calculateWaste(totalBought, gut, totalSold);

                BigDecimal totalEggsSale = ReportUtils.sumByParentCategory(sales, ALL_EGGS_CATEGORY);
                BigDecimal eggs = ReportUtils.sumByCategoryId(sales, EGG_CATEGORY);
                BigDecimal eggCarton = ReportUtils.sumByCategoryId(sales, EGG_CARTON_CATEGORY);

                Map<String, BigDecimal> salesByCategory = ReportUtils.salesByCategory(sales);
                Map<String, BigDecimal> salesByProduct = ReportUtils.salesByProduct(sales);
                Map<String, BigDecimal> quantitiesByProduct = ReportUtils.quantitiesByProduct(sales);
                Map<String, BigDecimal> expensesByCategory = ReportUtils.expensesByCategory(expenses);

                // ✅ Normalización de fecha a -05 fijo
                LocalDate localDate = date.withOffsetSameInstant(MEXICO_ZONE).toLocalDate();
                OffsetDateTime normalized = localDate.atStartOfDay().atOffset(MEXICO_ZONE);

                return new DailyReportDTO(
                                branchId,
                                normalized,
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
                // 📅 1️⃣ Calcular semana local completa (-05)
                OffsetDateTime startLocal = ReportUtils.getStartOfWeek(date);
                OffsetDateTime endLocal = startLocal.plusDays(7); // 👈 fin exclusivo

                // 🌎 2️⃣ Convertir a UTC antes de consultar la BD
                OffsetDateTime startUtc = startLocal.withOffsetSameInstant(ZoneOffset.UTC);
                OffsetDateTime endUtc = endLocal.withOffsetSameInstant(ZoneOffset.UTC);

                // 🧮 3️⃣ Consultar usando >= start y < end
                List<Ticket> tickets = ticketRepository.findByBranchIdAndDateGreaterThanEqualAndDateLessThan(branchId,
                                startUtc, endUtc);
                List<Expense> expenses = expenseRepository
                                .findByBranchIdAndDateGreaterThanEqualAndDateLessThan(branchId, startUtc, endUtc);
                List<Sale> sales = tickets.stream().flatMap(t -> t.getSales().stream()).toList();

                // 📊 4️⃣ Totales principales
                BigDecimal totalSales = ReportUtils.sumTicketsTotal(tickets);
                BigDecimal totalExpenses = ReportUtils.sumExpensesPaid(expenses);
                BigDecimal totalProfit = ReportUtils.calculateProfit(totalSales, totalExpenses);
                BigDecimal totalSold = ReportUtils.sumSalesQuantity(sales);
                BigDecimal totalBought = ReportUtils.sumExpensesQuantity(expenses);

                // 🥚 5️⃣ Totales específicos
                BigDecimal eggs = ReportUtils.sumByCategoryId(sales, EGG_CATEGORY);
                BigDecimal eggCarton = ReportUtils.sumByCategoryId(sales, EGG_CARTON_CATEGORY);
                BigDecimal totalEggsSale = ReportUtils.sumByParentCategory(sales, ALL_EGGS_CATEGORY);

                // 📦 6️⃣ Agrupaciones
                Map<String, BigDecimal> salesByCategory = ReportUtils.salesByCategory(sales);
                Map<String, BigDecimal> salesByProduct = ReportUtils.salesByProduct(sales);
                Map<String, BigDecimal> quantitiesByProduct = ReportUtils.quantitiesByProduct(sales);
                Map<String, BigDecimal> expensesByCategory = ReportUtils.expensesByCategory(expenses);

                List<ProductReportDTO> productReports = getProductsReport(sales);

                // 📅 7️⃣ Reportes diarios (sin solaparse)
                List<DailyReportDTO> dailyReports = new ArrayList<>();
                if (includeDays) {
                        LocalDate current = startLocal.toLocalDate();
                        LocalDate endDate = endLocal.toLocalDate();

                        while (current.isBefore(endDate)) { // 👈 fin exclusivo
                                OffsetDateTime currentDate = current.atStartOfDay().atOffset(MEXICO_ZONE);
                                dailyReports.add(getDailyReport(branchId, currentDate));
                                current = current.plusDays(1);
                        }
                }

                // 🧾 8️⃣ DTO final
                return new WeeklyReportDTO(
                                branchId,
                                startLocal, // mantiene la zona -05 para el front
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

        public List<ReportEntryDTO> getReports(Integer branchId, OffsetDateTime startDate, OffsetDateTime endDate,
                        String frequency) {
                StopWatch globalWatch = new StopWatch("Report generation");
                globalWatch.start("Total");

                StopWatch sectionWatch = new StopWatch();
                sectionWatch.start("Database fetch");
                endDate = ReportUtils.getEndOfDay(endDate);
                // 🔹 1. Consultas a la base de datos
                List<Ticket> allTickets = ticketRepository.findByBranchIdAndDateBetween(branchId, startDate, endDate);
                List<Expense> allExpenses = expenseRepository.findByBranchIdAndDateBetween(branchId, startDate,
                                endDate);

                sectionWatch.stop();
                log.info("⏱ DB fetch took: {} ms", sectionWatch.getTotalTimeMillis());
                List<ReportEntryDTO> reports = new ArrayList<>();

                OffsetDateTime current = startDate;
                while (!current.isAfter(endDate)) {
                        OffsetDateTime periodStart = current;
                        OffsetDateTime adjustedStart = periodStart;
                        OffsetDateTime periodEnd;

                        switch (frequency.toLowerCase()) {
                                case "daily":
                                        periodEnd = ReportUtils.getEndOfDay(periodStart);
                                        break;
                                case "weekly":
                                        adjustedStart = ReportUtils.getStartOfWeek(periodStart);
                                        periodEnd = ReportUtils.getEndOfWeek(adjustedStart);
                                        break;
                                case "monthly":
                                        periodEnd = periodStart.plusMonths(1).minusNanos(1);
                                        break;
                                case "yearly":
                                        periodEnd = periodStart.plusYears(1).minusNanos(1);
                                        break;
                                default:
                                        throw new IllegalArgumentException("Frequency not supported: " + frequency);
                        }

                        final OffsetDateTime finalStart = adjustedStart;
                        final OffsetDateTime finalEnd = periodEnd;

                        sectionWatch = new StopWatch();
                        sectionWatch.start("Filtering");
                        // 🔹 2. Filtrado de datos
                        List<Ticket> tickets = allTickets.stream()
                                        .filter(t -> !t.getDate().isBefore(finalStart)
                                                        && !t.getDate().isAfter(finalEnd))
                                        .toList();

                        List<Expense> expenses = allExpenses.stream()
                                        .filter(e -> !e.getDate().isBefore(finalStart)
                                                        && !e.getDate().isAfter(finalEnd))
                                        .toList();

                        sectionWatch.stop();
                        log.info("⏱ Filtering for {} → {} took: {} ms", finalStart.toLocalDate(),
                                        finalEnd.toLocalDate(), sectionWatch.getTotalTimeMillis());

                        sectionWatch = new StopWatch();
                        sectionWatch.start("Calculations");

                        // 🔹 3. Cálculos y armado del DTO
                        List<Sale> sales = tickets.stream().flatMap(t -> t.getSales().stream()).toList();

                        BigDecimal totalSales = ReportUtils.sumTicketsTotal(tickets);
                        BigDecimal totalExpenses = ReportUtils.sumExpensesPaid(expenses);
                        BigDecimal totalProfit = ReportUtils.calculateProfit(totalSales, totalExpenses);
                        BigDecimal totalSold = ReportUtils.sumSalesQuantity(sales);
                        BigDecimal totalBought = ReportUtils.sumExpensesQuantity(expenses);
                        Map<String, BigDecimal> salesByProduct = ReportUtils.salesByProduct(sales);
                        Map<String, BigDecimal> quantitiesByProduct = ReportUtils.quantitiesByProduct(sales);
                        Map<String, BigDecimal> salesByCategory = ReportUtils.salesByCategory(sales);
                        Map<String, BigDecimal> quantitiesByCategory = ReportUtils.quantitiesByCategory(sales);

                        reports.add(ReportEntryDTO.builder()
                                        .branchId(branchId)
                                        .startDate(finalStart)
                                        .endDate(finalEnd)
                                        .frequency(frequency)
                                        .totalSales(totalSales)
                                        .totalExpenses(totalExpenses)
                                        .quantitiesByProduct(quantitiesByProduct)
                                        .quantitiesByCategory(quantitiesByCategory)
                                        .salesByProduct(salesByProduct)
                                        .salesByCategory(salesByCategory)
                                        .totalProfit(totalProfit)
                                        .totalSold(totalSold)
                                        .totalBought(totalBought)
                                        .build());

                        sectionWatch.stop();
                        log.info("⚙️ Calculations for {} took: {} ms", finalStart.toLocalDate(),
                                        sectionWatch.getTotalTimeMillis());

                        current = periodEnd.plusNanos(1);
                }

                globalWatch.stop();
                log.info("🏁 Total report generation took: {} ms", globalWatch.getTotalTimeMillis());

                return reports;
        }

        public List<ReportEntryDTO> getReportsByCustomWeeks(Integer branchId, OffsetDateTime startDate,
                        OffsetDateTime endDate) {

                // 🔹 Calcular cuántos días hay entre start y end
                long daysBetween = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;

                // 🔹 Calcular cuántas semanas completas de 7 días se necesitan
                long weeksNeeded = (long) Math.ceil(daysBetween / 7.0);

                // 🔹 Ajustar el endDate al final de la última semana completa
                OffsetDateTime extendedEnd = startDate.plusDays((weeksNeeded * 7) - 1);

                // 🔹 Incluir todo el último día de esa última semana
                extendedEnd = ReportUtils.getEndOfDay(extendedEnd);

                // 1️⃣ Consultar todos los datos del rango total extendido
                List<Ticket> allTickets = ticketRepository.findByBranchIdAndDateBetween(branchId, startDate,
                                extendedEnd);
                List<Expense> allExpenses = expenseRepository.findByBranchIdAndDateBetween(branchId, startDate,
                                extendedEnd);

                List<ReportEntryDTO> reports = new ArrayList<>();

                // 2️⃣ Dividir en semanas exactas de 7 días desde startDate
                OffsetDateTime currentStart = startDate;

                while (!currentStart.isAfter(extendedEnd)) {
                        OffsetDateTime currentEnd = currentStart.plusDays(6);

                        OffsetDateTime finalStart = currentStart;
                        OffsetDateTime finalEnd = currentEnd;

                        // 3️⃣ Filtrar solo los datos de esos 7 días
                        List<Ticket> tickets = allTickets.stream()
                                        .filter(t -> !t.getDate().isBefore(finalStart)
                                                        && !t.getDate().isAfter(finalEnd))
                                        .toList();

                        List<Expense> expenses = allExpenses.stream()
                                        .filter(e -> !e.getDate().isBefore(finalStart)
                                                        && !e.getDate().isAfter(finalEnd))
                                        .toList();

                        // 4️⃣ Calcular totales
                        List<Sale> sales = tickets.stream()
                                        .flatMap(t -> t.getSales().stream())
                                        .toList();

                        BigDecimal totalSales = ReportUtils.sumTicketsTotal(tickets);
                        BigDecimal totalExpenses = ReportUtils.sumExpensesPaid(expenses);
                        BigDecimal totalProfit = ReportUtils.calculateProfit(totalSales, totalExpenses);
                        BigDecimal totalSold = ReportUtils.sumSalesQuantity(sales);
                        BigDecimal totalBought = ReportUtils.sumExpensesQuantity(expenses);
                        Map<String, BigDecimal> salesByProduct = ReportUtils.salesByProduct(sales);
                        Map<String, BigDecimal> quantitiesByProduct = ReportUtils.quantitiesByProduct(sales);
                        Map<String, BigDecimal> salesByCategory = ReportUtils.salesByCategory(sales);
                        Map<String, BigDecimal> quantitiesByCategory = ReportUtils.quantitiesByCategory(sales);

                        // 5️⃣ Crear el DTO
                        reports.add(ReportEntryDTO.builder()
                                        .branchId(branchId)
                                        .startDate(finalStart)
                                        .endDate(finalEnd)
                                        .frequency("weekly_custom")
                                        .totalSales(totalSales)
                                        .totalExpenses(totalExpenses)
                                        .totalProfit(totalProfit)
                                        .totalSold(totalSold)
                                        .totalBought(totalBought)
                                        .salesByProduct(salesByProduct)
                                        .quantitiesByProduct(quantitiesByProduct)
                                        .salesByCategory(salesByCategory)
                                        .quantitiesByCategory(quantitiesByCategory)
                                        .build());

                        // 6️⃣ Avanzar 7 días
                        currentStart = currentStart.plusDays(7);
                }

                return reports;
        }

        public List<ReportEntryDTO> getReportsCustom(
                        List<Integer> branchIds,
                        OffsetDateTime startDate,
                        OffsetDateTime endDate,
                        String frequency,
                        String metric,
                        boolean compareWithPreviousYear,
                        List<String> categories) {

                List<ReportEntryDTO> combinedReports = new ArrayList<>();

                for (Integer branchId : branchIds) {
                        // Reportes actuales
                        List<ReportEntryDTO> currentReports = generateReports(branchId, startDate, endDate, frequency,
                                        metric, categories);

                        currentReports.forEach(r -> r.setYear(startDate.getYear()));
                        combinedReports.addAll(currentReports);

                        // Reportes del año anterior
                        if (compareWithPreviousYear) {
                                OffsetDateTime prevStart = startDate.minusYears(1);
                                OffsetDateTime prevEnd = endDate.minusYears(1);

                                List<ReportEntryDTO> previousReports = generateReports(branchId, prevStart, prevEnd,
                                                frequency, metric, categories);

                                previousReports.forEach(r -> r.setYear(startDate.getYear() - 1));
                                combinedReports.addAll(previousReports);
                        }
                }

                return combinedReports;
        }

        private List<ReportEntryDTO> generateReports(
                        Integer branchId,
                        OffsetDateTime startDate,
                        OffsetDateTime endDate,
                        String frequency,
                        String metric,
                        List<String> categories) {

                // 1️⃣ Normalizar fecha del usuario
                OffsetDateTime userStart = ReportUtils.getStartOfDay(startDate);
                OffsetDateTime userEnd = ReportUtils.getEndOfDay(endDate);

                // 2️⃣ Expandir rango real según frecuencia
                OffsetDateTime realStart = ReportUtils.expandStart(userStart, frequency);
                OffsetDateTime realEnd = ReportUtils.expandEnd(userEnd, frequency);
                // 3️⃣ Consultar datos ya expandidos
                List<Ticket> allTickets = ticketRepository
                                .findByBranchIdAndDateBetween(branchId, realStart, realEnd);

                List<Sale> allSales = saleRepository
                                .findByTicketBranchIdAndTicketDateBetween(branchId, realStart, realEnd);

                // 4️⃣ Normalizar categorías
                Set<String> lowerCats = (categories == null || categories.isEmpty())
                                ? Set.of()
                                : categories.stream().map(s -> s.toLowerCase()).collect(Collectors.toSet());

                // 5️⃣ Agrupar por fecha (México)
                Map<LocalDate, List<Ticket>> ticketsByDate = allTickets.stream()
                                .collect(Collectors.groupingBy(
                                                t -> t.getDate().withOffsetSameInstant(MEXICO_ZONE).toLocalDate()));

                Map<LocalDate, List<Sale>> salesByDate = allSales.stream()
                                .collect(Collectors.groupingBy(s -> s.getTicket().getDate()
                                                .withOffsetSameInstant(MEXICO_ZONE).toLocalDate()));

                // 6️⃣ Generar el rango expandido
                List<LocalDate> timeline = realStart.toLocalDate()
                                .datesUntil(realEnd.plusDays(1).toLocalDate())
                                .collect(Collectors.toList());

                List<ReportEntryDTO> reports = new ArrayList<>();

                // 7️⃣ Generar un entry por cada fecha real expandida
                for (LocalDate date : timeline) {

                        OffsetDateTime base = date.atStartOfDay().atOffset(MEXICO_ZONE);

                        OffsetDateTime rangeStart;
                        OffsetDateTime rangeEnd;

                        switch (frequency) {
                                case "weekly":
                                        rangeStart = ReportUtils.getStartOfWeekSunday(base);
                                        rangeEnd = ReportUtils.getEndOfWeekSaturday(rangeStart);
                                        break;

                                case "weekly_custom":
                                        rangeStart = base;
                                        rangeEnd = base.plusDays(6);
                                        break;

                                case "monthly":
                                        rangeStart = base.withDayOfMonth(1);
                                        rangeEnd = rangeStart.plusMonths(1).minusNanos(1);
                                        break;

                                default: // daily
                                        rangeStart = base;
                                        rangeEnd = base.plusDays(1).minusNanos(1);
                        }

                        // 8️⃣ Obtener datos del día
                        List<Sale> daySales = salesByDate.getOrDefault(date, List.of());
                        List<Ticket> dayTickets = ticketsByDate.getOrDefault(date, List.of());

                        // Filtrar categorías
                        List<Sale> filteredSales = lowerCats.isEmpty()
                                        ? daySales
                                        : daySales.stream()
                                                        .filter(s -> {
                                                                if (s.getProduct().getCategory() == null)
                                                                        return false;
                                                                return lowerCats.contains(s.getProduct().getCategory()
                                                                                .getName().toLowerCase());
                                                        })
                                                        .toList();

                        BigDecimal totalSales = ReportUtils.sumTicketsTotal(dayTickets);
                        BigDecimal totalSold = ReportUtils.sumSalesQuantity(filteredSales);

                        Map<String, BigDecimal> dataByCategory = Map.of();
                        if (!lowerCats.isEmpty()) {
                                dataByCategory = metric.equals("sales")
                                                ? ReportUtils.salesByCategory(filteredSales)
                                                : ReportUtils.quantitiesByCategory(filteredSales);
                        }

                        reports.add(
                                        ReportEntryDTO.builder()
                                                        .branchId(branchId)
                                                        .startDate(rangeStart)
                                                        .endDate(rangeEnd)
                                                        .frequency(frequency)
                                                        .totalSales(metric.equals("sales") ? totalSales : null)
                                                        .totalSold(metric.equals("quantity") ? totalSold : null)
                                                        .salesByCategory(metric.equals("sales") ? dataByCategory : null)
                                                        .quantitiesByCategory(metric.equals("quantity") ? dataByCategory
                                                                        : null)
                                                        .build());
                }

                return reports;
        }

        public List<Map<String, Object>> formatForChart(
                        List<ReportEntryDTO> reports,
                        String frequency,
                        String metric, // "sales" o "quantity"
                        boolean includeCategories,
                        List<String> categories) {
                Map<String, Map<String, BigDecimal>> chartMap = new LinkedHashMap<>();
                DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.forLanguageTag("es-MX"));
                List<Integer> branchIds = reports.stream()
                                .map(ReportEntryDTO::getBranchId)
                                .distinct()
                                .toList();

                Map<Integer, String> branchNames = branchRepository.findAllById(branchIds).stream()
                                .collect(Collectors.toMap(Branch::getId, Branch::getName));
                // Ordenar por fecha real antes de asignar labels
                reports = reports.stream()
                                .sorted(Comparator.comparing(ReportEntryDTO::getStartDate))
                                .toList();

                for (ReportEntryDTO r : reports) {
                        r.setBranchName(branchNames.getOrDefault(r.getBranchId(), "Sucursal " + r.getBranchId()));
                        // 📅 Etiqueta: Semana o Día
                        String label;

                        switch (frequency) {

                                case "weekly_custom":
                                        int weekIndex = ReportUtils.getWeekIndexOfMonth(r.getStartDate());
                                        String monthShort = r.getStartDate().getMonth()
                                                        .getDisplayName(TextStyle.SHORT, new Locale("es", "MX"));
                                        label = "S" + weekIndex + " " + monthShort;
                                        break;

                                case "weekly":
                                        int isoWeek = ReportUtils.getWeekOfYear(r.getStartDate()); // ya tienes esta
                                                                                                   // función
                                        label = "S" + isoWeek + " " + r.getStartDate().getYear();
                                        break;

                                case "monthly":
                                        String monthName = r.getStartDate().getMonth()
                                                        .getDisplayName(TextStyle.SHORT, new Locale("es", "MX"));
                                        label = monthName + " " + r.getStartDate().getYear();
                                        break;

                                default:
                                        label = r.getStartDate().format(dayFormatter);
                                        break;
                        }

                        chartMap.putIfAbsent(label, new LinkedHashMap<>());
                        Map<String, BigDecimal> row = chartMap.get(label);

                        int year = r.getStartDate().getYear();

                        // 💰 Seleccionar fuente de datos según métrica
                        Map<String, BigDecimal> source = metric.equals("sales") ? r.getSalesByCategory()
                                        : r.getQuantitiesByCategory();

                        // 🧮 Sin categorías → total por sucursal y año
                        if (!includeCategories || categories.isEmpty()) {
                                String key = r.getBranchName() + " " + year;
                                BigDecimal total = metric.equals("sales") ? r.getTotalSales() : r.getTotalSold();
                                row.put(key, row.getOrDefault(key, BigDecimal.ZERO).add(total));
                        } else {
                                // 🧩 Con categorías → agregar una línea por sucursal-categoría y año
                                for (String cat : categories) {
                                        BigDecimal val = source.getOrDefault(cat.toLowerCase(), BigDecimal.ZERO);
                                        String key = r.getBranchName() + " - " + cat + " " + year;
                                        row.put(key, row.getOrDefault(key, BigDecimal.ZERO).add(val));
                                }
                        }

                }

                // 🔹 Convertir a lista final ordenada
                return chartMap.entrySet().stream()
                                .map(entry -> {
                                        Map<String, Object> obj = new LinkedHashMap<>();
                                        obj.put("label", entry.getKey());
                                        // Convertir BigDecimal → double (para graficar sin problemas)
                                        entry.getValue().forEach((k, v) -> obj.put(k, v.doubleValue()));
                                        return obj;
                                })
                                .toList();
        }

}
