package com.kamar.issuemanagementsystem.analysis.service;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the user analysis service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserAnalysisServiceImpl implements UserAnalysisService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO bestPerformantEmployee() throws AnalysisException{

        /*get users by authority */
        List<User> usersByAuthority = userRepository.findUsersByAuthorityOrderByCreatedOnAsc(Authority.EMPLOYEE);

        /*check if the list is empty*/
        if (usersByAuthority.isEmpty()) {
            return null;
        }

        /*get the most performant*/
        User mostPerformantEmployee = usersByAuthority.parallelStream().reduce(
                        (user, user2) -> user2.getRating().getRate() > user.getRating().getRate() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("error occurred"));

        /*map to dto and return*/
        return userMapper.userToPresentationDTO(mostPerformantEmployee);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO mostPerformantEmployee() throws AnalysisException {

        /*get the most performant employee*/
        List<User> usersByAuthority = userRepository.findUsersByAuthorityOrderByCreatedOnAsc(Authority.EMPLOYEE);
        /*get the most performant*/
        User mostPerformantEmployee = usersByAuthority.parallelStream().reduce(
                (user, user2) -> user2.getRating().getTotalRates() > user.getRating().getTotalRates() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("error occurred!, try again"));

        /*map to dto and return*/
        return userMapper.userToPresentationDTO(mostPerformantEmployee);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public UserPresentationDTO leastPerformantEmployee() throws AnalysisException{

        /*get the least performant*/
        List<User> usersBAuthorities = userRepository.findUsersByAuthorityOrderByCreatedOnAsc(Authority.EMPLOYEE);

        /*assert the list is not empty*/
        assert !usersBAuthorities.isEmpty();

        /*get the list performant*/
        User leastPerformantEmployee = usersBAuthorities.parallelStream().reduce(
                        (user, user2) -> user2.getRating().getRate() < user.getRating().getRate() ? user2 : user)
                .orElseThrow(() -> new AnalysisException("an error occurred"));

        /*map and return*/
        return userMapper.userToPresentationDTO(leastPerformantEmployee);
    }
}