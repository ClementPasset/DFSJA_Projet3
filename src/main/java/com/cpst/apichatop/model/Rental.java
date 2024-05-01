package com.cpst.apichatop.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental {

    public Rental(String name, float surface, float price, String description, String picture, DBUser user) {
        this.setName(name);
        this.setSurface(surface);
        this.setPrice(price);
        this.setDescription(description);
        this.setCreated_at(LocalDate.now());
        this.setUpdated_at(LocalDate.now());
        this.setUser(user);
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

    private LocalDate created_at;

    private LocalDate updated_at;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private DBUser user;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<Message> messages;
}
