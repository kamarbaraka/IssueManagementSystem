package com.kamar.issuemanagementsystem.user.utility.mappers;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * the user mapper implementation.
 * @author kamar baraka.*/

@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public User dtoToEntity(UserRegistrationDTO registrationDTO) {

        User user = new User();
        user.setUsername(registrationDTO.username());
        user.setPassword(registrationDTO.password());
        return user;
    }

    @Override
    public UserPresentationDTO userToPresentationDTO(User user) {

        /*map the authorities*/
        List<String> authorities = user.getAuthorities().stream().map(UserAuthority::getAuthority).toList();

        /*map user to presentation DTO*/
        return new UserPresentationDTO(
                user.getUsername(),
                authorities,
                user.getUserRating().getRate()
        );
    }
}
