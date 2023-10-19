package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserManagementService extends UserDetailsManager {

    void deleteUserByUsername(String username);
    void elevate(String username, Authority authority);
    void downgrade(String username, Authority authority);
}
