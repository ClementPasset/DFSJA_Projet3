package com.cpst.apichatop.repository;

import org.springframework.data.repository.CrudRepository;

import com.cpst.apichatop.model.Rental;

public interface RentalRepository extends CrudRepository<Rental, Long> {
}