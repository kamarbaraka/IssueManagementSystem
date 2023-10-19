package com.kamar.issuemanagementsystem.user.utility.mappers;

import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * the user mapper contract.
 * @author kamar baraka.*/


public interface UserMapper {

    User dtoToEntity(UserRegistrationDTO registrationDTO);
}
