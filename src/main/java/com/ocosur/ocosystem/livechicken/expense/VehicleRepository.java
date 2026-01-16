package com.ocosur.ocosystem.livechicken.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocosur.ocosystem.core.vehicle.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>  {
    
}
