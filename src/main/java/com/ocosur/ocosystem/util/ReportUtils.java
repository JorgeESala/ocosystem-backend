package com.ocosur.ocosystem.util;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.model.Sale;
import com.ocosur.ocosystem.model.Ticket;

public class ReportUtils {

    private static final ZoneId MEXICO_ZONE = ZoneId.of("UTC-5");

    public static OffsetDateTime getStartOfDay(OffsetDateTime date) {
        LocalDate localDate = date.toLocalDate();
        return localDate.atStartOfDay(MEXICO_ZONE).toOffsetDateTime();
    }

    public static OffsetDateTime getEndOfDay(OffsetDateTime date) {
        LocalDate localDate = date.toLocalDate();
        return localDate.plusDays(1).atStartOfDay(MEXICO_ZONE).minusNanos(1).toOffsetDateTime();
    }

    public static OffsetDateTime getStartOfWeek(OffsetDateTime date) {
        LocalDate localDate = date.toLocalDate();
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        LocalDate sunday = localDate.minusDays(dayOfWeek % 7);
        return sunday.atStartOfDay(MEXICO_ZONE).toOffsetDateTime();
    }

    public static OffsetDateTime getEndOfWeek(OffsetDateTime startOfWeek) {
        return startOfWeek.plusDays(7).minusNanos(1);
    }

    public static BigDecimal sumBigDecimals(Collection<BigDecimal> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumTicketsTotal(Collection<Ticket> tickets) {
        return tickets.stream()
                .map(Ticket::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumExpensesPaid(Collection<Expense> expenses) {
        return expenses.stream()
                .map(Expense::getPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumExpensesQuantity(Collection<Expense> expenses) {
        return expenses.stream()
                .map(e -> e.getQuantity() == null ? BigDecimal.ZERO : e.getQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumSalesQuantity(Collection<Sale> sales) {
        return sales.stream()
                .map(Sale::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumSalesSubtotal(Collection<Sale> sales) {
        return sales.stream()
                .map(Sale::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Map<String, BigDecimal> salesByCategory(Collection<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getCategory().getName(),
                        Collectors.mapping(Sale::getSubtotal, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    public static Map<String, BigDecimal> salesByProduct(Collection<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getName(),
                        Collectors.mapping(Sale::getSubtotal, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    public static Map<String, BigDecimal> quantitiesByProduct(Collection<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getName(),
                        Collectors.mapping(Sale::getQuantity, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }
    public static Map<String, BigDecimal> quantitiesByCategory(Collection<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getCategory().getName(),
                        Collectors.mapping(Sale::getQuantity, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    public static Map<String, BigDecimal> expensesByCategory(Collection<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().getName(),
                        Collectors.mapping(Expense::getPaid, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }


    public static List<Sale> filterByCategoryId(Collection<Sale> sales, Integer categoryId) {
        return sales.stream()
                .filter(s -> s.getProduct().getCategory().getId().equals(categoryId))
                .toList();
    }

    public static BigDecimal sumByCategoryId(Collection<Sale> sales, Integer categoryId) {
        return sales.stream()
                .filter(s -> s.getProduct().getCategory().getId().equals(categoryId))
                .map(Sale::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal sumByParentCategory(Collection<Sale> sales, Integer parentCategoryId) {
        return sales.stream()
                .filter(s -> {
                    Integer parentId = s.getProduct().getCategory().getParent_id();
                    return parentId != null && parentId.equals(parentCategoryId);
                })
                .map(Sale::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    
    public static BigDecimal calculateProfit(BigDecimal totalSales, BigDecimal totalExpenses) {
        return totalSales.subtract(totalExpenses);
    }

    public static BigDecimal calculateWaste(BigDecimal totalBought, BigDecimal gut, BigDecimal totalSold) {
        return totalBought.subtract(gut).subtract(totalSold);
    }
}
