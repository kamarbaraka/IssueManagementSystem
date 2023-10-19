package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.exceptions.UserExceptionService;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * implementation of the user management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;

    @Override
    public void deleteUserByUsername(String username) {

        /*delete user*/
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        /*get the user by username*/
        return userRepository.findUserByUsername(username).orElseThrow();

    }

    @Override
    public void elevate(String username, Authority authority) {
        
        userRepository.findUserByUsername(username).ifPresent(user -> {
            
            /*add the authority*/
            user.getAuthorities().add(authority);
        });
        
    }

    @Override
    public void downgrade(String username, Authority authority) {
        
        userRepository.findUserByUsername(username).ifPresent(user -> 
                /*downgrade*/user.getAuthorities().removeIf(filter -> user.getAuthorities().contains(authority)));

    }

    @Override
    public void createUser(UserDetails user) {
        
    }

    @Override
    public void updateUser(UserDetails user) {

        /*get the user*/
        userRepository.findUserByUsername(user.getUsername()).ifPresent(foundUser -> {

            /*update user*/
            foundUser.setUsername(user.getUsername());

        });

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }
}
