package com.ocosur.ocosystem.livechicken.supplier;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.supplier.dto.SupplierResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    public List<SupplierResponseDTO> getAll() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .map(supplier -> new SupplierResponseDTO(
                        supplier.getId(),
                        supplier.getName()
                ))
                .toList();
    }
}
