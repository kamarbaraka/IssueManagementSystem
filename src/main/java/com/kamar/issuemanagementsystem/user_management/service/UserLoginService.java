package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;

/**
 * the user login service.
 * @author kamar baraka.*/

public interface UserLoginService {

    UserEntity loginUser(String username, String password);

}
