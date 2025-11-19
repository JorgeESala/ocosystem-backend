package com.ocosur.ocosystem.util;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.model.Sale;
import com.ocosur.ocosystem.model.Ticket;

public class ReportUtils {

    private static final ZoneOffset MEXICO_ZONE = ZoneOffset.ofHours(-5);

    public static OffsetDateTime getStartOfDay(OffsetDateTime date) {
        return date
                .withOffsetSameInstant(MEXICO_ZONE)
                .toLocalDate()
                .atStartOfDay()
                .atOffset(MEXICO_ZONE);
    }

    public static OffsetDateTime getEndOfDay(OffsetDateTime date) {
        return getStartOfDay(date).plusDays(1).minusNanos(1);
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
                        s -> s.getProduct().getCategory().getName().toLowerCase(),
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
                        s -> s.getProduct().getCategory().getName().toLowerCase(),
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

    public static int getWeekOfYear(OffsetDateTime date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    public static OffsetDateTime getStartOfWeekSunday(OffsetDateTime date) {
        DayOfWeek dow = date.getDayOfWeek();
        int diff = dow.getValue() % 7; // domingo = 0
        return getStartOfDay(date.minusDays(diff));
    }

    // Asumiendo que getStartOfWeekSunday devuelve el domingo a las 00:00:00
    public static OffsetDateTime getEndOfWeekSaturday(OffsetDateTime startOfWeekSunday) {
        // 1. Ir al inicio del DOMINGO siguiente
        OffsetDateTime nextSunday = startOfWeekSunday.plusWeeks(1).minusNanos(1);

        // 2. Retroceder un nanosegundo para estar en el final absoluto del SÁBADO
        return nextSunday.minusNanos(1);
    }

    public static OffsetDateTime getStartOfMonth(OffsetDateTime date) {
        return date.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    public static OffsetDateTime getEndOfMonth(OffsetDateTime date) {
        return date.withDayOfMonth(date.toLocalDate().lengthOfMonth())
                .with(LocalTime.MAX);
    }

    public static int getWeekIndexOfMonth(OffsetDateTime date) {
        OffsetDateTime monthStart = date.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        long days = ChronoUnit.DAYS.between(monthStart, date.truncatedTo(ChronoUnit.DAYS));
        return (int) (days / 7) + 1; // Semana 1, 2, 3...
    }

    public static OffsetDateTime expandStart(OffsetDateTime date, String frequency) {
        switch (frequency) {
            case "weekly":
                return getStartOfWeekSunday(date); // domingo
            case "weekly_custom":
                return date;
            case "monthly":
                return date.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
            default:
                return getStartOfDay(date);
        }
    }

    public static OffsetDateTime expandEnd(OffsetDateTime startDate, OffsetDateTime endDate, String frequency) {
        switch (frequency) {
            case "weekly":
                return getEndOfWeekSaturday(getStartOfWeekSunday(endDate));
            case "weekly_custom":
                long daysDiff = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());

                long blockIndex = daysDiff / 7;

                OffsetDateTime blockStart = startDate.plusDays(blockIndex * 7);
                System.out.println("Block Start: " + getEndOfDay(blockStart.plusDays(6)));
                return getEndOfDay(blockStart.plusDays(6));
            case "monthly":
                OffsetDateTime startMonth = endDate.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                return startMonth.plusMonths(1).minusNanos(1);
            default:
                return getEndOfDay(endDate);
        }
    }

    public static List<OffsetDateTime> getStartOfYears(OffsetDateTime start, OffsetDateTime end) {
        List<OffsetDateTime> years = new ArrayList<>();

        // 1. Normalizar el inicio al primer día del año de inicio (Enero 1, 00:00:00)
        OffsetDateTime current = start
                .withMonth(1)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // 2. Normalizar el final al final del día
        OffsetDateTime normalizedEnd = getEndOfDay(end); // Usando tu utilidad existente

        // 3. Iterar año a año
        while (current.isBefore(normalizedEnd) || current.isEqual(normalizedEnd)) {
            years.add(current);

            // Avanzar al primer día del año siguiente
            current = current.plusYears(1)
                    .withMonth(1)
                    .withDayOfMonth(1);

            // Si el siguiente año ya sobrepasa el rango, terminamos.
            if (current.isAfter(normalizedEnd.plusYears(1)))
                break;
        }

        return years;
    }

    public static List<OffsetDateTime> getStartOfMonths(OffsetDateTime start, OffsetDateTime end) {
        List<OffsetDateTime> months = new ArrayList<>();

        // 1. Normalizar el inicio al primer día del mes de inicio (a las 00:00:00)
        OffsetDateTime current = start
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // 2. Normalizar el final al final del día
        OffsetDateTime normalizedEnd = getEndOfDay(end); // Usando tu utilidad existente

        // 3. Iterar mes a mes
        while (current.isBefore(normalizedEnd) || current.isEqual(normalizedEnd)) {
            months.add(current);

            // Avanzar al primer día del mes siguiente
            current = current.plusMonths(1)
                    .withDayOfMonth(1);

            // Si el siguiente mes ya sobrepasa el rango, terminamos.
            if (current.isAfter(normalizedEnd.plusDays(31)))
                break;
        }

        return months;
    }

    public static List<OffsetDateTime> getStartOfWeeks(OffsetDateTime start, OffsetDateTime end) {
        List<OffsetDateTime> weeks = new ArrayList<>();

        // 1. Normalizar el inicio al primer día de la semana que contiene 'start'
        OffsetDateTime current = getStartOfWeekSunday(start);
        // 2. Normalizar el final al final del día
        OffsetDateTime normalizedEnd = getEndOfDay(end);

        // 3. Iterar semana a semana
        while (current.isBefore(normalizedEnd) || current.isEqual(normalizedEnd)) {
            weeks.add(current);

            // Avanzar al primer día de la semana siguiente
            current = current.plusWeeks(1);

            // Si la siguiente semana ya sobrepasa el rango, terminamos.
            if (current.isAfter(normalizedEnd.plusDays(7)))
                break;
        }
        return weeks;
    }

    // Para generateReports (Acumulación)
    public static void accumulateMap(Map<String, BigDecimal> destination, Map<String, BigDecimal> source) {
        source.forEach((key, value) -> destination.merge(key, value, BigDecimal::add));
    }

    // Para generateReports (Segmentación de 7 días)
    // Simula la obtención de los puntos de inicio (start dates) de los segmentos de
    // 7 días
    public static List<OffsetDateTime> getStartOfSegments(OffsetDateTime start, OffsetDateTime end, int days) {
        List<OffsetDateTime> starts = new ArrayList<>();
        OffsetDateTime current = getStartOfDay(start);
        OffsetDateTime userEnd = getEndOfDay(end); // Aseguramos que la comparación sea correcta

        while (current.isBefore(userEnd) || current.isEqual(userEnd)) {
            starts.add(current);
            // Avanza al inicio del siguiente segmento (7 días después)
            current = current.plusDays(days);
        }
        return starts;
    }

    public static List<OffsetDateTime> getStartOfDays(OffsetDateTime start, OffsetDateTime end) {

        // 1. Normalizar el rango de fechas para asegurar la iteración correcta.
        // Se usa la fecha de inicio del día para la iteración.
        OffsetDateTime normalizedStart = getStartOfDay(start);
        OffsetDateTime normalizedEnd = getStartOfDay(end);

        List<OffsetDateTime> startOfDays = new ArrayList<>();
        OffsetDateTime current = normalizedStart;

        // 2. Iterar día a día. Usamos isBefore/isEqual para incluir el día final.
        // La condición de parada es el inicio del día siguiente al final del rango.
        while (current.isBefore(normalizedEnd) || current.isEqual(normalizedEnd)) {

            startOfDays.add(current);

            // 3. Avanzar al inicio del día siguiente.
            current = current.plusDays(1);
        }

        return startOfDays;
    }
}
