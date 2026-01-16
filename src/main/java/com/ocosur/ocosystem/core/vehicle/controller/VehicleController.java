package com.ocosur.ocosystem.core.vehicle.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.core.vehicle.dto.VehicleResponseDTO;
import com.ocosur.ocosystem.core.vehicle.service.VehicleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public List<VehicleResponseDTO> getVehicles(){
        return vehicleService.getVehicles();
    }
    
}
