package com.cpst.apichatop.DTO.Responses;

import com.cpst.apichatop.DTO.RentalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentalsResponse {
    public Iterable<RentalDTO> rentals;
}
