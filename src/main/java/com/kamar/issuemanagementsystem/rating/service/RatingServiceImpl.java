package com.kamar.issuemanagementsystem.rating.service;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.data.dto.DepartmentRatingDto;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.DepartmentPerformanceRating;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.IntStream;

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
        UserRating userRating = user.getUserRating();
        userRating.setNumberOfRatings(userRating.getNumberOfRatings() + 1);
        userRating.setTotalRates(userRating.getTotalRates() + userRatingDTO.Rating());
        userRating.setRate(
                ((int) (userRating.getTotalRates() / userRating.getNumberOfRatings()))
        );

        /*apply rating to the user*/
        userRepository.save(user);

    }

    @Override
    public void rateDepartment(Department department) throws RatingException {

        /*get the members*/
        Collection<User> members = department.getMembers();

        /*set the rating*/
        department.getPerformanceRating().setNumberOfMembers(members.size());
        department.getPerformanceRating().setTotalMemberRating(members.parallelStream().map(
                user -> user.getUserRating().getRate()
        ).reduce(Integer::sum).orElseThrow(
                () -> new RatingException("error rating department")
        ));

        /*update the rating*/
        departmentRepository.save(department);

    }
}
