package com.kamar.issuemanagementsystem.user_management.utility.mappers;

import com.kamar.issuemanagementsystem.user_management.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;


/**
 * the user mapper contract.
 * @author kamar baraka.*/


public interface UserMapper {

    UserEntity dtoToEntity(UserRegistrationDTO registrationDTO);
    UserPresentationDTO userToPresentationDTO(UserEntity userEntity);
}
