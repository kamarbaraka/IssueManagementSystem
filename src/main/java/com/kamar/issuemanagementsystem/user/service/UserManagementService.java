package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

public interface UserManagementService extends UserDetailsManager {

    void deleteUserByUsername(String username);
    void elevate(String username, Authority authority);
    void downgrade(String username, Authority authority);
    User getUserByUsername(String username);
    boolean checkUserByUsernameAndAuthority(String username, Authority authority);
    List<User> getAllUsers();
    List<User> getUsersByAuthority(Authority authority);
}
