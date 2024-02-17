package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

public interface UserManagementService extends UserDetailsManager {

    void deleteUserByUsername(String username);
    void elevate(String username, UserAuthority authority) throws UserException;
    void downgrade(String username, String  authority) throws UserException;
    UserEntity getUserByUsername(String username);
    boolean checkUserByUsernameAndAuthority(String username, UserAuthority authority);
    List<UserEntity> getAllUsers();
    List<UserEntity> getUsersByAuthority(UserAuthority authority);

}
