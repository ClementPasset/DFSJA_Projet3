package com.cpst.apichatop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.DTO.Requests.CreateRentalRequest;
import com.cpst.apichatop.DTO.Requests.UpdateRentalRequest;
import com.cpst.apichatop.DTO.Responses.RentalsResponse;
import com.cpst.apichatop.Exceptions.NotFoundException;
import com.cpst.apichatop.Exceptions.UnauthorizedException;
import com.cpst.apichatop.service.RentalService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@AllArgsConstructor
public class RentalController {

    private RentalService rentalService;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> getRentals() {

        return ResponseEntity.ok(rentalService.getRentals());

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRental(@PathVariable("id") Long id) {

        try {
            return ResponseEntity.ok(rentalService.getRentalDTOById(id));
        } catch (NotFoundException exception) {
            return ResponseEntity.notFound().build();
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(@RequestBody CreateRentalRequest request, Principal principal)
            throws Exception {

        try {
            return ResponseEntity.ok(rentalService.createRental(request, principal));
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable("id") Long id, @RequestBody UpdateRentalRequest request,
            Principal principal) {

        try {
            return ResponseEntity.ok(rentalService.updateRental(id, request, principal));
        } catch (UnauthorizedException exception) {
            return ResponseEntity.status(401).body(exception);
        } catch (NotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }

}
