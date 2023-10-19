package com.kamar.issuemanagementsystem.user.exceptions;

import org.springframework.stereotype.Service;

/**
 * the user exception service.
 * @author kamar baraka.*/

@Service
public class UserExceptionService {

    public UserException userNotFound() {

        /*return exception*/
        return new UserException("user not found");
    }

    public UserException invalidToken(){

        /*return the exception*/
        return new UserException("invalid activation token");
    }
}
