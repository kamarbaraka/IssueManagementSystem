package com.kamar.issuemanagementsystem.analysis.service;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import com.kamar.issuemanagementsystem.user_management.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the user analysis service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class UserAnalysisServiceImpl implements UserAnalysisService {

    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;
    private final UserAuthorityUtility userAuthorityUtility;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO bestPerformantEmployee() throws AnalysisException{

        /*get users by authority */
        List<UserEntity> usersByAuthority = userEntityRepository.findUserByAuthoritiesContaining(userAuthorityUtility.getFor("employee"));

        /*check if the list is empty*/
        if (usersByAuthority.isEmpty()) {
            return null;
        }

        /*get the most performant*/
        UserEntity mostPerformantEmployee = usersByAuthority.parallelStream().reduce(
                        (user, user2) -> user2.getUserRating().getRate() > user.getUserRating().getRate() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("error occurred"));

        /*map to dto and return*/
        return userMapper.userToPresentationDTO(mostPerformantEmployee);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO mostPerformantEmployee() throws AnalysisException {

        /*get the most performant employee*/
        List<UserEntity> usersByAuthority = userEntityRepository.findUserByAuthoritiesContaining(userAuthorityUtility.getFor("employee"));
        /*get the most performant*/
        UserEntity mostPerformantEmployee = usersByAuthority.parallelStream().reduce(
                (user, user2) -> user2.getUserRating().getTotalRates() > user.getUserRating().getTotalRates() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("error occurred!, try again"));

        /*map to dto and return*/
        return userMapper.userToPresentationDTO(mostPerformantEmployee);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO leastPerformantEmployee() throws AnalysisException{

        /*get the least performant*/
        List<UserEntity> usersBAuthorities = userEntityRepository.findUserByAuthoritiesContaining(userAuthorityUtility.getFor("employee"));

        /*assert the list is not empty*/
        assert !usersBAuthorities.isEmpty();

        /*get the list performant*/
        UserEntity leastPerformantEmployee = usersBAuthorities.parallelStream().reduce(
                        (user, user2) -> user2.getUserRating().getRate() < user.getUserRating().getRate() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("an error occurred"));

        /*map and return*/
        return userMapper.userToPresentationDTO(leastPerformantEmployee);
    }
}
