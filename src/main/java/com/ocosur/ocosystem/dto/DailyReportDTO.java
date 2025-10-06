package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyReportDTO {

    private Integer branchId;              // Sucursal
    private String date;                   // Fecha en formato YYYY-MM-DD

    // Totales
    private BigDecimal totalSales;         // Total de ventas
    private BigDecimal totalExpenses;      // Total de gastos
    private BigDecimal totalProfit;        // Ganancia = ventas - gastos
    private BigDecimal totalSold;          // Cantidad vendida (kilos/piezas)
    private BigDecimal totalBought;        // Cantidad comprada (kilos/piezas)
    private BigDecimal gut;                // Solo si aplica (ej. tripa)
    private BigDecimal waste;              // Solo si aplica (ej. merma)

    // Detalles desglosados
    private Map<String, BigDecimal> salesByCategory;       // Categoría → subtotal vendido
    private Map<String, BigDecimal> salesByProduct;        // Producto → subtotal vendido
    private Map<String, BigDecimal> quantitiesByProduct;   // Producto → cantidad vendida
    private Map<String, BigDecimal> expensesByCategory;    // Categoría de gasto → total gastado
}
