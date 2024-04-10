package com.cpst.apichatop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.repository.RentalRepository;

@Service
public class RentalService {

    private RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public Iterable<Rental> getRentals() {
        return this.rentalRepository.findAll();
    }

    public Optional<Rental> getRentalById(Long id) {
        return this.rentalRepository.findById(id);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public Rental updateRental(Rental rental){
        return rentalRepository.save(rental);
    }
}
