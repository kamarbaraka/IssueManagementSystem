package com.kamar.issuemanagementsystem.user.utility.mappers;

import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.stereotype.Service;

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
}
