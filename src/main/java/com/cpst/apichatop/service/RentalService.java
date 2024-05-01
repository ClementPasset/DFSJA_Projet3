package com.cpst.apichatop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.repository.RentalRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalService {

    private RentalRepository rentalRepository;

    /**
     * retrieves all rentals
     * 
     * @return rentals list
     */
    public Iterable<Rental> getRentals() {
        return this.rentalRepository.findAll();
    }

    /**
     * Gets a rental by Id
     * 
     * @param id
     * @return The Rental corresponding to the id param
     */
    public Rental getRentalById(Long id) {
        return this.rentalRepository.findById(id).get();
    }

    /**
     * Create a new rental in database
     * 
     * @param rental The rental to create
     * @return The successfuly cerated Rental
     */
    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    /**
     * Method to updated an existing rental in database
     * 
     * @param rental
     * @param name
     * @param description
     * @param surface
     * @param price
     * @return The updated Rental
     */
    public Rental updateRental(Rental rental, String name, String description, Float surface, Float price) {
        rental.setName(name);
        rental.setSurface(Float.valueOf(surface));
        rental.setDescription(description);
        rental.setPrice(Float.valueOf(price));

        return rentalRepository.save(rental);
    }

    /**
     * Method to save a rental pictures in the static folder
     * 
     * @param rental
     * @param file
     * @throws IOException
     */
    public void saveRentalImage(Rental rental, MultipartFile file) throws IOException {
        String pictureLink = rental.getPicture();
        String fileName = pictureLink.substring(pictureLink.lastIndexOf("/"));
        Path path = Paths.get("./src/main/resources/static/rentalImages", fileName);

        Files.write(path, file.getBytes());
    }
}
