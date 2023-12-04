package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * implementation of the user management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CompanyProperties companyProperties;
    private final UserAuthorityUtility userAuthorityUtility;

    private void elevationNotification(String username, UserAuthority authority){

        /*construct email*/
        String subject = "Elevation Notice";
        String message = "Hello " + username + ", you have been elevated to an "+
                authority.getAuthority()+
                ". <br>"+ companyProperties.endTag();

        /*send email*/
        emailService.sendEmail(message, subject, username, null);
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
    public void elevate(String username, UserAuthority authority) throws UserException {

        /*check if user is present*/
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UserException("no user to elevate."));

        /*check if user already has the authority to elevate*/
        List<UserAuthority> authorities = user.getAuthorities();
        if (authorities.contains(authority)) {
            /*throw and return*/
            throw new UserException("user already has the authority");
        }

        /*filter for employee*/
        if (!authority.equals(userAuthorityUtility.getFor("user"))) {

            /*check if the user is a user*/
            UserAuthority userAuthority = userAuthorityUtility.getFor("user");
            if (authorities.contains(userAuthority)) {
                /*remove the user authority*/
                user.getAuthorities().remove(userAuthority);
            }
        }

        /*elevate user*/
        user.getAuthorities().add(authority);
        /*update*/
        userRepository.save(user);

        /*notify*/
        elevationNotification(username, authority);
        
    }

    @Override
    public void downgrade(String username, String authority) throws UserException {


        /*check if the user exists*/
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UserException("no user to downgrade.")
        );

        /*check if the user has the authority to downgrade*/
        UserAuthority theAuthority = userAuthorityUtility.getFor(authority);
        List<UserAuthority> authorities = user.getAuthorities();

        if (!authorities.contains(theAuthority)) {
            /*throw*/
            throw new UserException("no authority to downgrade");
        }

        /*check if user will have an authority after downgrade*/
        if (authorities.size() == 1) {
            /*throw*/
            throw new UserException("can't downgrade the user");
        }

        /*downgrade the user*/
        user.getAuthorities().remove(theAuthority);
        userRepository.save(user);

    }

    @Override
    public User getUserByUsername(String username) {

        /*get user by username*/
        return userRepository.findUserByUsername(username).orElseThrow();
    }

    @Override
    public boolean checkUserByUsernameAndAuthority(String username, UserAuthority authority) {

        /*check*/
        return userRepository.findUserByUsernameAndAuthoritiesContaining(username, authority).isPresent();
    }

    @Override
    public List<User> getAllUsers() {

        /*get all users*/
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByAuthority(UserAuthority authority) {

        /*get users by authority*/
        return userRepository.findUserByAuthoritiesContaining(authority);
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
