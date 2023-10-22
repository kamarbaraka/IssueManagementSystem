package com.kamar.issuemanagementsystem.user.utility.mappers;

import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;


/**
 * the user mapper contract.
 * @author kamar baraka.*/


public interface UserMapper {

    User dtoToEntity(UserRegistrationDTO registrationDTO);
    UserPresentationDTO userToPresentationDTO(User user);
}
