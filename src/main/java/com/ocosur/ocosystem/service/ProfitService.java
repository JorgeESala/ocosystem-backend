package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.BatchCostDetail;
import com.ocosur.ocosystem.dto.ProfitReport;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.repository.BatchSaleRepository;
import com.ocosur.ocosystem.repository.BranchRepository;
import com.ocosur.ocosystem.repository.ExpenseRepository;

@Service
public class ProfitService {
    
    @Autowired
    private BatchSaleRepository batchSaleRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private BranchRepository branchRepository;

    public ProfitService(BatchSaleRepository batchSaleRepository, ExpenseRepository expenseRepository) {
        this.batchSaleRepository = batchSaleRepository;
        this.expenseRepository = expenseRepository;
    }


    public ProfitReport calculateProfit(LocalDate start, LocalDate end, List<Long> branchIds) {

        // si mandan null o lista vacía → calcula todas las sucursales
        List<Long> branchesToUse = (branchIds == null || branchIds.isEmpty())
                ? branchRepository.findAllIds()
                : branchIds;

        // obtener ventas filtradas por varias sucursales
        List<BatchSale> sales = batchSaleRepository
                .findWithBatchBetweenDatesAndBranchIds(start, end, branchesToUse);

        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalChickenCostsProRated = BigDecimal.ZERO;
        Map<Long, BatchCostDetail> batchDetailMap = new LinkedHashMap<>();

        for (BatchSale bs : sales) {
            Batch b = bs.getBatch();
            if (b == null)
                continue;

            Long batchId = b.getId();

            // ingreso por venta
            BigDecimal saleTotal = bs.getSaleTotal();
            if (saleTotal == null) {
                BigDecimal kgTotal = bs.getKgTotal() != null ? bs.getKgTotal() : BigDecimal.ZERO;
                BigDecimal pricePerKg = b.getPricePerKg() != null ? b.getPricePerKg() : BigDecimal.ZERO;
                saleTotal = kgTotal.multiply(pricePerKg);
            }
            totalSales = totalSales.add(nullSafe(saleTotal));

            BigDecimal quantitySold = nullSafe(bs.getQuantitySold());

            // avgChickenWeight con fallback
            BigDecimal avgChickenWeight = b.getAvgChickenWeight();
            if (avgChickenWeight == null) {
                if (b.getKgTotal() != null && b.getChickenQuantity() != null
                        && b.getChickenQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    avgChickenWeight = b.getKgTotal().divide(
                            b.getChickenQuantity(), 6, RoundingMode.HALF_UP);
                } else {
                    avgChickenWeight = BigDecimal.ZERO;
                }
            }

            BigDecimal pricePerKg = nullSafe(b.getPricePerKg());

            // costo proporcional de este batch
            BigDecimal computedCostForThisSale = quantitySold.multiply(avgChickenWeight).multiply(pricePerKg);

            totalChickenCostsProRated = totalChickenCostsProRated.add(computedCostForThisSale);

            // kg vendidos
            BigDecimal kgSold = bs.getKgTotal() != null
                    ? bs.getKgTotal()
                    : quantitySold.multiply(avgChickenWeight);

            // batch detail
            BatchCostDetail existing = batchDetailMap.get(batchId);
            String branchName = (b.getBranch() != null) ? b.getBranch().getName() : "N/A";

            if (existing == null) {

                BigDecimal aspKg = kgSold.compareTo(BigDecimal.ZERO) > 0
                        ? saleTotal.divide(kgSold, 6, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                batchDetailMap.put(
                        batchId,
                        new BatchCostDetail(
                                batchId,
                                branchName,
                                safe(b.getPriceTotal()),
                                safe(b.getChickenQuantity()),
                                avgChickenWeight,
                                pricePerKg,
                                quantitySold,
                                kgSold,
                                computedCostForThisSale,
                                saleTotal,
                                aspKg));

            } else {

                BigDecimal newTotalSales = existing.totalSalesInRange().add(saleTotal);
                BigDecimal newKgSold = existing.kgSoldInRange().add(kgSold);

                BigDecimal newAsp = newKgSold.compareTo(BigDecimal.ZERO) > 0
                        ? newTotalSales.divide(newKgSold, 6, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                batchDetailMap.put(
                        batchId,
                        new BatchCostDetail(
                                existing.batchId(),
                                existing.branchName(),
                                existing.totalBatchCost(),
                                existing.chickenQuantity(),
                                existing.avgChickenWeight(),
                                existing.pricePerKg(),
                                existing.quantitySoldInRange().add(quantitySold),
                                newKgSold,
                                existing.computedCostForRange().add(computedCostForThisSale),
                                newTotalSales,
                                newAsp));
            }
        }

        // gastos de varias sucursales
        BigDecimal totalExpenses = expenseRepository
                .sumBetweenByBranches(start, end, branchesToUse);

        BigDecimal profit = totalSales
                .subtract(totalChickenCostsProRated)
                .subtract(nullSafe(totalExpenses));

        return new ProfitReport(
                start,
                end,
                totalSales,
                nullSafe(totalExpenses),
                totalChickenCostsProRated,
                profit,
                new ArrayList<>(batchDetailMap.values()));
    }

    private static BigDecimal nullSafe(BigDecimal b) {
        return b != null ? b : BigDecimal.ZERO;
    }

    private static BigDecimal safe(BigDecimal b) {
        return b != null ? b : BigDecimal.ZERO;
    }
}
