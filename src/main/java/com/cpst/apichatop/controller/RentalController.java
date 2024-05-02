package com.cpst.apichatop.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.DTO.RentalDTO;
import com.cpst.apichatop.DTO.Requests.CreateRentalRequest;
import com.cpst.apichatop.DTO.Responses.MessageResponse;
import com.cpst.apichatop.DTO.Responses.RentalsResponse;
import com.cpst.apichatop.Exceptions.NotFoundException;
import com.cpst.apichatop.Exceptions.UnauthorizedException;
import com.cpst.apichatop.mapper.RentalDTOMapper;
import com.cpst.apichatop.service.DBUserService;
import com.cpst.apichatop.service.RentalService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
public class RentalController {

    private RentalService rentalService;
    private DBUserService dbUserService;
    private RentalDTOMapper rentalDTOMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> getRentals() {
        RentalsResponse rentals = new RentalsResponse(rentalDTOMapper.toDto(rentalService.getRentals()));
        return ResponseEntity.ok(rentals);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable("id") Long id) {
        RentalDTO rentalDto = rentalDTOMapper.toDto(rentalService.getRentalById(id));
        if (rentalDto != null) {
            return ResponseEntity.ok(rentalDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(
            @RequestBody CreateRentalRequest request,
            Principal user) throws Exception {

        DBUser owner = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(user)).get();

        Rental newRental = rentalDTOMapper.toEntity(request, owner);

        try {
            rentalService.createRental(newRental);

            rentalService.saveRentalImage(newRental, request.getPicture());

            return ResponseEntity.ok(new MessageResponse("Rental has been created."));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            Principal user,
            Authentication auth) {

        DBUser dbUser = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(user)).get();
        Rental rentalToUpdate = rentalService.getRentalById(id);

        try {
            rentalService.updateRental(rentalToUpdate, name, description, Float.valueOf(surface),
                    Float.valueOf(price), dbUser);
            return ResponseEntity.ok(new MessageResponse("The rental has been updated"));
        } catch (UnauthorizedException exception) {
            return ResponseEntity.status(401).body(exception);
        } catch (NotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception);
        }

    }

}
