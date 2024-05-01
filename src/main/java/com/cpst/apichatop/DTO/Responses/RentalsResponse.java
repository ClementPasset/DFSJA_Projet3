package com.cpst.apichatop.DTO.Responses;

import com.cpst.apichatop.DTO.RentalDTO;

import lombok.Data;

@Data
public class RentalsResponse {
    public Iterable<RentalDTO> rentals;

    public RentalsResponse(Iterable<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
