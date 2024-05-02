package com.cpst.apichatop.mapper;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cpst.apichatop.DTO.RentalDTO;
import com.cpst.apichatop.DTO.Requests.CreateRentalRequest;
import com.cpst.apichatop.DTO.Responses.RentalsResponse;
import com.cpst.apichatop.model.DBUser;
import com.cpst.apichatop.model.Rental;

@Component
public class RentalDTOMapper {
    public Rental toEntity(CreateRentalRequest request, DBUser owner) {
        Rental rental = new Rental();
        rental.setName(request.getName());
        rental.setDescription(request.getDescription());
        rental.setSurface(Float.valueOf(request.getSurface()));
        rental.setPrice(Float.valueOf(request.getPrice()));
        rental.setPicture(getUniquePictureName(request.getPicture()));
        rental.setUser(owner);

        return rental;
    }

    private String getUniquePictureName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        return "/api/rentalImages/" + UUID.randomUUID().toString() + extension;
    }

    public RentalDTO toDto(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setPicture(rental.getPicture());
        rentalDTO.setCreated_at(rental.getCreated_at());
        rentalDTO.setUpdated_at(rental.getUpdated_at());
        rentalDTO.setOwner_id(rental.getUser().getId());

        return rentalDTO;
    }

    public RentalsResponse toDto(Iterable<Rental> rentals) {
        return new RentalsResponse(StreamSupport.stream(rentals.spliterator(), false).map(rental -> this.toDto(rental))
                .collect(Collectors.toList()));
    }
}
