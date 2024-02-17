package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserException;

/**
 * the user registration service.
 * @author kamar baraka.*/

public interface UserRegistrationService  {

    void registerUser(UserRegistrationDTO registrationDTO) throws UserException;
    void activateUser(UserActivationDTO activationDTO) throws UserException;
}
