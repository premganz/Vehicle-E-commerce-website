package com.vehicleecommerce.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvehicle")
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be non-negative")
    @Column(name = "price", nullable = false)
    private Double price;
    
    @NotBlank(message = "Type is required")
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "image")
    private String image;
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(String name, Double price, String type, String image) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    // Utility methods
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    public String getImageUrl() {
        return "/images/" + (image != null ? image : "default.jpg");
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
