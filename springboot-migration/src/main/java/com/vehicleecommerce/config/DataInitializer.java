package com.vehicleecommerce.config;

import com.vehicleecommerce.model.Vehicle;
import com.vehicleecommerce.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (vehicleRepository.count() == 0) {
            initializeVehicles();
        }
    }

    private void initializeVehicles() {
        // Cars
        vehicleRepository.save(new Vehicle("Honda Civic", 22000.0, "car", "Honda Civic.jpg"));
        vehicleRepository.save(new Vehicle("Toyota Camry", 24000.0, "car", "Toyota Camry.jpg"));
        vehicleRepository.save(new Vehicle("BMW 3 Series", 35000.0, "car", "BMW 3.jpg"));
        vehicleRepository.save(new Vehicle("Tesla Model Y", 55000.0, "car", "Tesla Y.jpg"));
        vehicleRepository.save(new Vehicle("Tesla Model X", 80000.0, "car", "Tesla X.jpg"));
        vehicleRepository.save(new Vehicle("Tesla Roadster", 200000.0, "car", "Tesla Roadster.jpg"));
        vehicleRepository.save(new Vehicle("Ford Focus", 18000.0, "car", "Ford Focus.jpg"));
        vehicleRepository.save(new Vehicle("Ford Explorer", 32000.0, "car", "Ford Explorer.jpg"));
        vehicleRepository.save(new Vehicle("Chevrolet Malibu", 23000.0, "car", "Chevrolet Malibu.jpg"));
        vehicleRepository.save(new Vehicle("Chevrolet Impala", 28000.0, "car", "Chevrolet Impala.jpg"));
        vehicleRepository.save(new Vehicle("Nissan Maxima", 34000.0, "car", "Nissan Maxima.jpg"));
        vehicleRepository.save(new Vehicle("Acura TL", 30000.0, "car", "Acura TL.jpg"));
        vehicleRepository.save(new Vehicle("Dodge Charger", 33000.0, "car", "Dodge Charger.jpg"));
        vehicleRepository.save(new Vehicle("Subaru Impreza", 19000.0, "car", "Subaru Impreza.jpg"));
        vehicleRepository.save(new Vehicle("Honda CR-V", 26000.0, "car", "Honda CR-V.jpg"));
        vehicleRepository.save(new Vehicle("Toyota RAV4", 27000.0, "car", "Toyota RAV4.jpg"));
        vehicleRepository.save(new Vehicle("Toyota Highlander", 35000.0, "car", "Toyota Highlander.jpg"));

        // Motorcycles
        vehicleRepository.save(new Vehicle("Honda CBR1000RR SP", 16000.0, "motorcycle", "Honda CBR1000RR SP.jpg"));
        vehicleRepository.save(new Vehicle("Kawasaki Z900", 9000.0, "motorcycle", "Kawasaki Z900.jpg"));
        vehicleRepository.save(new Vehicle("Yamaha R1", 17000.0, "motorcycle", "Yamaha R1.jpg"));
        vehicleRepository.save(new Vehicle("Ducati Panigale V4", 22000.0, "motorcycle", "Ducati Panigale V4.jpg"));
        vehicleRepository.save(new Vehicle("Harley-Davidson Street Bob 114", 15000.0, "motorcycle", "Harley-Davidson Street Bob 114.jpg"));
        vehicleRepository.save(new Vehicle("Indian Chieftain Elite", 23000.0, "motorcycle", "Indian Chieftain Elite.jpg"));
        vehicleRepository.save(new Vehicle("BMW S1000RR", 16000.0, "motorcycle", "BMW S1000RR.jpg"));
        vehicleRepository.save(new Vehicle("KTM 1090 Adventure R", 13000.0, "motorcycle", "KTM 1090 Adventure R.jpg"));
        vehicleRepository.save(new Vehicle("Suzuki GSX-R1000R", 15000.0, "motorcycle", "Suzuki GSX-R1000R.jpg"));
        vehicleRepository.save(new Vehicle("Aprilia Tuono V4 1100 Factory ABS", 16000.0, "motorcycle", "Aprilla Tuono V4 1100 Factory ABS.jpg"));
        vehicleRepository.save(new Vehicle("Ducati Scrambler Desert Sled", 11000.0, "motorcycle", "Ducati Scrambler Desert Sled.jpg"));
        vehicleRepository.save(new Vehicle("Ducati Supersport S", 14000.0, "motorcycle", "Ducati Supersport S.jpg"));
        vehicleRepository.save(new Vehicle("Honda Rebel 500", 6000.0, "motorcycle", "Honda Rebel 500.jpg"));
        vehicleRepository.save(new Vehicle("Kawasaki Versys-X 300", 5500.0, "motorcycle", "Kawasaki Versys-X 300.jpg"));
        vehicleRepository.save(new Vehicle("Moto Guzzi V7 Stone", 8000.0, "motorcycle", "Moto Guzzi MXG-21 Flying Fortress.jpg"));
        vehicleRepository.save(new Vehicle("MV Agusta Brutale 800", 12000.0, "motorcycle", "MV Agusta Brutale 800.jpg"));
        vehicleRepository.save(new Vehicle("Triumph Street Triple", 10000.0, "motorcycle", "Triump Street Cut.jpg"));

        // Boats
        vehicleRepository.save(new Vehicle("Boston Whaler 270 Dauntless", 85000.0, "boat", "Boston Whaler 270 Dauntless boat.jpg"));
        vehicleRepository.save(new Vehicle("Bavaria Cruiser 46", 250000.0, "boat", "Bavaria Cruiser 46boat.jpg"));
        vehicleRepository.save(new Vehicle("Lagoon 380 Catamaran", 350000.0, "boat", "Lagoon 380 boat.jpg"));
        vehicleRepository.save(new Vehicle("Bass Boat Pro", 45000.0, "boat", "Bass Boats.jpg"));
        vehicleRepository.save(new Vehicle("Bowrider 22ft", 35000.0, "boat", "Bowriders.jpg"));
        vehicleRepository.save(new Vehicle("Cabin Cruiser 30ft", 120000.0, "boat", "Cabin Cruisers.jpg"));
        vehicleRepository.save(new Vehicle("Deck Boat 24ft", 55000.0, "boat", "Deck boats.jpg"));
        vehicleRepository.save(new Vehicle("Fishing Boat 26ft", 65000.0, "boat", "Fishing Boats.jpg"));
        vehicleRepository.save(new Vehicle("High Performance Speedboat", 95000.0, "boat", "High Performance Boats.jpg"));
        vehicleRepository.save(new Vehicle("Inflatable RIB 18ft", 25000.0, "boat", "Inflatable Boats.jpg"));
        vehicleRepository.save(new Vehicle("Personal Watercraft", 12000.0, "boat", "Personal Watercraft.jpg"));
        vehicleRepository.save(new Vehicle("Pontoon Boat 22ft", 40000.0, "boat", "Pontoon Boats.jpg"));
        vehicleRepository.save(new Vehicle("Recreational Boat 20ft", 30000.0, "boat", "Recreational Boats.jpg"));
        vehicleRepository.save(new Vehicle("Sailboat 32ft", 75000.0, "boat", "Sailboats.jpg"));
        vehicleRepository.save(new Vehicle("Towboat 24ft", 85000.0, "boat", "Towboats.jpg"));

        System.out.println("Sample vehicles data initialized successfully!");
    }
}
