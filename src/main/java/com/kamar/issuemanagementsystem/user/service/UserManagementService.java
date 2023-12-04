package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

public interface UserManagementService extends UserDetailsManager {

    void deleteUserByUsername(String username);
    void elevate(String username, UserAuthority authority) throws UserException;
    void downgrade(String username, String  authority) throws UserException;
    User getUserByUsername(String username);
    boolean checkUserByUsernameAndAuthority(String username, UserAuthority authority);
    List<User> getAllUsers();
    List<User> getUsersByAuthority(UserAuthority authority);

}
