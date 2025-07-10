package com.vehicleecommerce.service;

import com.vehicleecommerce.model.Vehicle;
import com.vehicleecommerce.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Page<Vehicle> getVehiclesByType(String type, Pageable pageable) {
        return vehicleRepository.findByTypeIgnoreCase(type, pageable);
    }

    public Page<Vehicle> searchVehicles(String query, Pageable pageable) {
        return vehicleRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Page<Vehicle> getVehiclesByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return vehicleRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Page<Vehicle> getVehiclesWithFilters(String type, String name, Double minPrice, Double maxPrice, Pageable pageable) {
        return vehicleRepository.findWithFilters(type, name, minPrice, maxPrice, pageable);
    }

    public List<String> getDistinctVehicleTypes() {
        return vehicleRepository.findDistinctTypes();
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
