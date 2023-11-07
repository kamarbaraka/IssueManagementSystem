package com.kamar.issuemanagementsystem.rating.utility;

import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;

/**
 * the rating mapper contract.
 * @author kamar baraka.*/

public interface RatingMapper {

    UserRating mapToRating(UserRatingDTO userRatingDTO);
}
