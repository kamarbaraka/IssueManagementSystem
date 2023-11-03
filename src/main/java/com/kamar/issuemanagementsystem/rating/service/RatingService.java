package com.kamar.issuemanagementsystem.rating.service;

import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;

/**
 * the department rating service contract.
 * @author kamar baraka.*/

public interface RatingService {

    void rateUser(UserRatingDTO userRatingDTO) throws RatingException;
    void rateDepartment(DepartmentRatingDto departmentRatingDto) throws RatingException;
}
