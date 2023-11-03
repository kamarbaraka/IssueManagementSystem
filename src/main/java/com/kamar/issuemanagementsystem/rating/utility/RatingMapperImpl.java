package com.kamar.issuemanagementsystem.rating.utility;

import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.Rating;
import org.springframework.stereotype.Service;

/**
 * implementation of the rating mapper contract.
 * @author kamar baraka.*/

@Service
public class RatingMapperImpl implements RatingMapper {
    @Override
    public Rating mapToRating(DepartmentRatingDto departmentRatingDto) {
        return null;
    }

    @Override
    public Rating mapToRating(UserRatingDTO userRatingDTO) {
        return null;
    }
}
