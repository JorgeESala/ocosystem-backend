package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;

import com.ocosur.ocosystem.model.Product;

import lombok.Data;
@Data
public class ProductReportDTO {
    Product product;
    BigDecimal quantitySold;
    BigDecimal totalSales; 
}
