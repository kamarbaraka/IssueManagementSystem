package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the user management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;

    private void elevationNotification(String username, Authority authority){

        /*construct email*/
        String subject = "Elevation Notice";
        String message = "Hello " + username + ", you have been elevated to an " + authority.getAuthority();

        /*send email*/
        emailService.sendEmail(message, subject, username);
    }

    @Override
    public void deleteUserByUsername(String username) {

        /*delete user*/
        userRepository.deleteUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        /*get the user by username*/
        return userRepository.findById(username).orElseThrow();

    }

    @Override
    public void elevate(String username, Authority authority) {
        
        userRepository.findUserByUsername(username).ifPresent(user -> {
            /*add the authority*/
            user.setAuthority(authority);
            /*update*/
            userRepository.save(user);
        });

        /*notify*/
        elevationNotification(username, authority);
        
    }

    @Override
    public void downgrade(String username, Authority authority) {
        
        userRepository.findUserByUsername(username).ifPresent(user -> 
                /*downgrade*/user.setAuthority(authority));

    }

    @Override
    public User getUserByUsername(String username) {

        /*get user by username*/
        return userRepository.findUserByUsername(username).orElseThrow();
    }

    @Override
    public boolean checkUserByUsernameAndAuthority(String username, Authority authority) {

        /*check*/
        return userRepository.findUserByUsernameAndAuthority(username, authority).isPresent();
    }

    @Override
    public List<User> getAllUsers() {

        /*get all users*/
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByAuthority(Authority authority) {

        /*get users by authority*/
        return userRepository.findUsersByAuthority(authority);
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
