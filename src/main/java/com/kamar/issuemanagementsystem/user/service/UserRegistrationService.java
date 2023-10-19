package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;

/**
 * the user registration service.
 * @author kamar baraka.*/

public interface UserRegistrationService {

    void registerUser(UserRegistrationDTO registrationDTO);
    void activateUser(UserActivationDTO activationDTO) throws UserException;
}
