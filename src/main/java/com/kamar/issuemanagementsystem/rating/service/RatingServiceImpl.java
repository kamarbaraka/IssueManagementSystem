package com.kamar.issuemanagementsystem.rating.service;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * implementation of the rating service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final DepartmentRepository departmentRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public void rateUser(UserRatingDTO userRatingDTO) throws RatingException {

        /*get the user to rate*/
        UserEntity userEntity = userEntityRepository.findUserByUsername(userRatingDTO.username()).orElseThrow(
                () -> new RatingException("no such user to rate"));

        /*get the rating and update it*/
        UserRating userRating = userEntity.getUserRating();
        userRating.setNumberOfRatings(userRating.getNumberOfRatings() + 1);
        userRating.setTotalRates(userRating.getTotalRates() + userRatingDTO.Rating());
        userRating.setRate(
                ((int) (userRating.getTotalRates() / userRating.getNumberOfRatings()))
        );

        /*apply rating to the user*/
        userEntityRepository.save(userEntity);

    }

    @Override
    public void rateDepartment(Department department) throws RatingException {

        /*get the members*/
        Collection<UserEntity> members = department.getMembers();

        /*set the rating*/
        department.getPerformanceRating().setNumberOfMembers(members.size());
        department.getPerformanceRating().setTotalMemberRating(members.stream().map(
                user -> user.getUserRating().getRate()
        ).reduce(Integer::sum).orElseThrow(
                () -> new RatingException("error rating department")
        ));

        /*update the rating*/
        departmentRepository.save(department);

    }
}
