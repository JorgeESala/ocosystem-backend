package com.ocosur.ocosystem.livechicken.supplier;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.supplier.dto.SupplierResponseDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/live-chicken/suppliers")
@AllArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping List<SupplierResponseDTO> getAll() {
        return supplierService.getAll();
    }

}
