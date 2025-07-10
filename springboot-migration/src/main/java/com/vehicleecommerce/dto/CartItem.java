package com.vehicleecommerce.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItem {
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    private String vehicleName;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    private String vehicleImage;
    
    // Constructors
    public CartItem() {}
    
    public CartItem(Long vehicleId, String vehicleName, Double price, Integer quantity, String vehicleImage) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.price = price;
        this.quantity = quantity;
        this.vehicleImage = vehicleImage;
    }
    
    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getVehicleName() {
        return vehicleName;
    }
    
    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getVehicleImage() {
        return vehicleImage;
    }
    
    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
    
    // Calculated properties
    public Double getSubtotal() {
        return price * quantity;
    }
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    public String getFormattedSubtotal() {
        return String.format("$%.2f", getSubtotal());
    }
}
