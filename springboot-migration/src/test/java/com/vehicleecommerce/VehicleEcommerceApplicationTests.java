package com.vehicleecommerce;

import com.vehicleecommerce.model.Vehicle;
import com.vehicleecommerce.repository.VehicleRepository;
import com.vehicleecommerce.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class VehicleEcommerceApplicationTests {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void contextLoads() {
        // Verify that the Spring context loads successfully
        assertThat(vehicleService).isNotNull();
        assertThat(vehicleRepository).isNotNull();
    }

    @Test
    void testVehicleCreation() {
        Vehicle vehicle = new Vehicle("Test Car", 25000.0, "car", "test.jpg");
        Vehicle saved = vehicleRepository.save(vehicle);
        
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Car");
        assertThat(saved.getPrice()).isEqualTo(25000.0);
        assertThat(saved.getType()).isEqualTo("car");
    }
}
