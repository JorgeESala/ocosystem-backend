package com.ocosur.ocosystem.core.vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.vehicle.dto.VehicleResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.VehicleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<VehicleResponseDTO> getVehicles() {
        return vehicleRepository.findAll().stream().map(vehicle -> VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .name(vehicle.getName())
                .build()).toList();
    }
}
