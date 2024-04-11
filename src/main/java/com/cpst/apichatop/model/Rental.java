package com.cpst.apichatop.model;

import java.time.LocalDate;

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
        this.setCreated_at(LocalDate.now());
        this.setUpdated_at(LocalDate.now());
        this.setOwner_id(ownerId);
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

    private Long owner_id;

    private LocalDate created_at;

    private LocalDate updated_at;
}
