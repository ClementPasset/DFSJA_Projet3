package com.cpst.apichatop.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rentals")
public class Rental {

    public Rental(){
        
    }

    public Rental(String name, float surface, float price, String description, String picture, Long ownerId) {
        this.setName(name);
        this.setSurface(surface);
        this.setPrice(price);
        this.setDescription(description);
        this.setCreatedAt(LocalDate.now());
        this.setUpdatedAt(LocalDate.now());
        this.setOwnerId(ownerId);
        this.setPicture(picture);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private float surface;

    private float price;

    private String picture;

    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
