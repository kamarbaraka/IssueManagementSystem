package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implementation of the user analysis service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserAnalysisServiceImpl implements UserAnalysisService {

    private final UserRepository userRepository;

    @Override
    public User mostPerformantEmployee() {

        /*get users by authority */
        return userRepository.findUsersByAuthorityOrderByTotalStarsDesc(Authority.EMPLOYEE).get(0);
    }

    @Override
    public User leastPerformantEmployee() {

        /*get the least performant*/
        return userRepository.findUsersByAuthorityOrderByTotalStarsAsc(Authority.EMPLOYEE).get(0);
    }
}
