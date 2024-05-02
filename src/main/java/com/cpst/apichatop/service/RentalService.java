package com.cpst.apichatop.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cpst.apichatop.DTO.RentalDTO;
import com.cpst.apichatop.DTO.Requests.CreateRentalRequest;
import com.cpst.apichatop.DTO.Requests.UpdateRentalRequest;
import com.cpst.apichatop.DTO.Responses.MessageResponse;
import com.cpst.apichatop.DTO.Responses.RentalsResponse;
import com.cpst.apichatop.Exceptions.NotFoundException;
import com.cpst.apichatop.Exceptions.UnauthorizedException;
import com.cpst.apichatop.mapper.RentalDTOMapper;
import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.Rental;
import com.cpst.apichatop.repository.RentalRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalService {

    private RentalRepository rentalRepository;
    private RentalDTOMapper rentalDTOMapper;
    private DBUserService dbUserService;

    /**
     * retrieves all rentals
     * 
     * @return rentals list
     */
    public RentalsResponse getRentals() {
        return rentalDTOMapper.toDto(this.rentalRepository.findAll());
    }

    /**
     * Gets a rental by Id
     * 
     * @param id
     * @return The Rental corresponding to the id param
     */
    public Rental getRentalById(Long id) {

        Rental rental = rentalRepository.findById(id).get();
        if (rental != null) {
            return rental;
        } else {
            throw new NotFoundException("Rental not found.");
        }
    }

    public RentalDTO getRentalDTOById(Long id) {
        return rentalDTOMapper.toDto(this.getRentalById(id));
    }

    public MessageResponse createRental(CreateRentalRequest request, Principal principal) throws IOException {
        DBUser user = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(principal))
                .get();
        Rental rental = rentalDTOMapper.toEntity(request, user);

        rentalRepository.save(rental);
        this.saveRentalImage(rental, request.getPicture());

        return new MessageResponse("Rental has been created");
    }

    public MessageResponse updateRental(Long id, UpdateRentalRequest request, Principal principal) {
        DBUser user = dbUserService.findByEmail(dbUserService.getEmailFromAuthentication(principal))
                .get();
        Rental rentalToUpdate = this.getRentalById(id);
        if (rentalToUpdate == null) {
            throw new NotFoundException("Rental not found.");
        } else if (rentalToUpdate.getUser().getId() == user.getId()) {
            rentalToUpdate.setName(request.getName());
            rentalToUpdate.setDescription(request.getDescription());
            rentalToUpdate.setSurface(Float.valueOf(request.getSurface()));
            rentalToUpdate.setPrice(Float.valueOf(request.getPrice()));

            return new MessageResponse("Rental has been updated.");

        } else {
            throw new UnauthorizedException("The user is not the owner of the rental");
        }
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

    /**
     * Checks if the Rental exists
     * 
     * @param rental
     * @return true if the rental exists, false otherwise
     */
    public boolean rentalExists(Rental rental) {
        return rentalRepository.findById(rental.getId()) != null;
    }
}
