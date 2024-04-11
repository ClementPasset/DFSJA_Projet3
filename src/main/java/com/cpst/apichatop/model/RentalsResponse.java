package com.cpst.apichatop.model;

import lombok.Data;

@Data
public class RentalsResponse {
    public Iterable<Rental> rentals;

    public RentalsResponse(Iterable<Rental> rentals) {
        this.rentals = rentals;
    }
}
