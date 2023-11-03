package com.kamar.issuemanagementsystem.rating.service;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implementation of the rating service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public void rateUser(UserRatingDTO userRatingDTO) throws RatingException {

        /*get the user to rate*/
        User user = userRepository.findUserByUsername(userRatingDTO.username()).orElseThrow(
                () -> new RatingException("no such user to rate"));

        /*get the rating and update it*/
        Rating userRating = user.getRating();
        userRating.setNumberOfRatings(userRating.getNumberOfRatings() + 1);
        userRating.setTotalRates(userRating.getTotalRates() + userRatingDTO.Rating());

        /*apply rating to the user*/
        userRepository.save(user);

    }

    @Override
    public void rateDepartment(DepartmentRatingDto departmentRatingDto) throws RatingException {

        /*get the department to rate*/
        Department department = departmentRepository.findById(departmentRatingDto.departmentName()).orElseThrow(
                () -> new RatingException("no such department to rate")
        );

        /*get the rating and set it*/
        Rating departmentRating = department.getRating();
        departmentRating.setNumberOfRatings(departmentRating.getNumberOfRatings() + 1);
        departmentRating.setTotalRates(departmentRating.getTotalRates() + departmentRatingDto.rating());

        /*apply the rating to the department*/
        departmentRepository.save(department);
    }
}
