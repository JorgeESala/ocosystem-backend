package com.ocosur.ocosystem.livechicken.supplier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SupplierResponseDTO {
    private Long id;
    private String name;
}
