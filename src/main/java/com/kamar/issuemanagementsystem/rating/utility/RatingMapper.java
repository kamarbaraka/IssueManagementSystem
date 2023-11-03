package com.kamar.issuemanagementsystem.rating.utility;

import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.Rating;

/**
 * the rating mapper contract.
 * @author kamar baraka.*/

public interface RatingMapper {

    Rating mapToRating(DepartmentRatingDto departmentRatingDto);
    Rating mapToRating(UserRatingDTO userRatingDTO);
}
