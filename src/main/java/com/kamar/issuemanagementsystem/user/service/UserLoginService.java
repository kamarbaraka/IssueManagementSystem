package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.entity.User;

/**
 * the user login service.
 * @author kamar baraka.*/

public interface UserLoginService {

    User loginUser(String username, String password);

}
