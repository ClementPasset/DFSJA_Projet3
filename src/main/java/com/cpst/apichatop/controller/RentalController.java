package com.cpst.apichatop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.model.RentalsResponse;
import com.cpst.apichatop.service.DBUserService;
import com.cpst.apichatop.service.RentalService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RentalController {

    RentalService rentalService;
    DBUserService dbUserService;

    public RentalController(RentalService rentalService, DBUserService dbUserService) {
        this.rentalService = rentalService;
        this.dbUserService = dbUserService;
    }

    @GetMapping("/rentals")
    public ResponseEntity<RentalsResponse> getRentals() {
        RentalsResponse rentals = new RentalsResponse(rentalService.getRentals());
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<Rental> getRental(@PathVariable("id") Long id) {
        Rental rental = rentalService.getRentalById(id).get();
        if (rental != null) {
            return ResponseEntity.ok(rental);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/rentals")
    public ResponseEntity<String> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture,
            Principal user) throws Exception {

        Long ownerId = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(user)).get().getId();

        String fileName = UUID.randomUUID().toString();
        String originalFileName = picture.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        Path path = Paths.get("./src/main/resources/static/rentalImages", fileName + "." + fileExtension);

        Files.write(path, picture.getBytes());

        Rental newRental = new Rental(name, Float.valueOf(surface), Float.valueOf(price), description,
                fileName + "." + fileExtension,
                ownerId);

        rentalService.createRental(newRental);

        return ResponseEntity.ok("Rental has been created.");
    }

    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") String surface,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            Principal user,
            Authentication auth) {

        Long userId = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(user)).get().getId();
        Optional<Rental> optionalRentalToUpdate = rentalService.getRentalById(id);

        if (optionalRentalToUpdate.isPresent()) {
            Rental rentalToUpdate = optionalRentalToUpdate.get();
            if (userId == rentalToUpdate.getOwner_id()) {
                rentalToUpdate.setName(name);
                rentalToUpdate.setSurface(Float.valueOf(surface));
                rentalToUpdate.setDescription(description);
                rentalToUpdate.setPrice(Float.valueOf(price));
                rentalService.updateRental(rentalToUpdate);
                return ResponseEntity.ok("The rental has been updated");
            } else {
                return ResponseEntity.internalServerError()
                        .body("The current user is not the owner of this rental.");
            }
        } else {
            return ResponseEntity.internalServerError().body("This rental doesn't exist.");
        }

    }

}
