package com.kamar.issuemanagementsystem.rating.data.dto;

import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;

/**
 * the user rating dto.
 * @author kamar baraka.*/


public record UserRatingDTO(

        String username,

        @Min(value = 0, message = "rating can't be negative")
        @Max(value = 5, message = "exceeds rating limit")
        int Rating
) implements  RatingDtoType {
}
