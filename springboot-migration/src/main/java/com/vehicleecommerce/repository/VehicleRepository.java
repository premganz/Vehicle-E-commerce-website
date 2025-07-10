package com.vehicleecommerce.repository;

import com.vehicleecommerce.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Find vehicles by type
    Page<Vehicle> findByType(String type, Pageable pageable);
    
    // Find vehicles by type (case insensitive)
    Page<Vehicle> findByTypeIgnoreCase(String type, Pageable pageable);
    
    // Find vehicles by type with sorting
    Page<Vehicle> findByTypeOrderByNameAsc(String type, Pageable pageable);
    Page<Vehicle> findByTypeOrderByPriceAsc(String type, Pageable pageable);
    Page<Vehicle> findByTypeOrderByPriceDesc(String type, Pageable pageable);
    
    // Count vehicles by type
    long countByType(String type);
    
    // Search vehicles by name containing keyword
    Page<Vehicle> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Search vehicles by type and name
    Page<Vehicle> findByTypeAndNameContainingIgnoreCase(String type, String name, Pageable pageable);
    
    // Find vehicles by price range
    Page<Vehicle> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
    
    // Find vehicles by type and price range
    Page<Vehicle> findByTypeAndPriceBetween(String type, Double minPrice, Double maxPrice, Pageable pageable);
    
    // Get all distinct vehicle types
    @Query("SELECT DISTINCT v.type FROM Vehicle v")
    List<String> findDistinctTypes();
    
    // Custom query for advanced search
    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:type IS NULL OR v.type = :type) AND " +
           "(:minPrice IS NULL OR v.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR v.price <= :maxPrice) AND " +
           "(:name IS NULL OR LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Vehicle> findVehiclesWithFilters(
        @Param("type") String type,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("name") String name,
        Pageable pageable
    );
    
    // Alias method for findVehiclesWithFilters
    default Page<Vehicle> findWithFilters(String type, String name, Double minPrice, Double maxPrice, Pageable pageable) {
        return findVehiclesWithFilters(type, minPrice, maxPrice, name, pageable);
    }
}
