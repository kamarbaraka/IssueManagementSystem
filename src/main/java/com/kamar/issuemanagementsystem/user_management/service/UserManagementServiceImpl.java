package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserException;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the user management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserEntityRepository userEntityRepository;
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
        userEntityRepository.deleteUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        /*get the user by username*/
        return userEntityRepository.findById(username).orElseThrow();

    }

    @Override
    public void elevate(String username, UserAuthority authority) throws UserException {

        /*check if user is present*/
        UserEntity userEntity = userEntityRepository.findUserByUsername(username).orElseThrow(
                () -> new UserException("no user to elevate."));

        /*check if user already has the authority to elevate*/
        List<UserAuthority> authorities = userEntity.getAuthorities();
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
                userEntity.getAuthorities().remove(userAuthority);
            }
        }

        /*elevate user*/
        userEntity.getAuthorities().add(authority);
        /*update*/
        userEntityRepository.save(userEntity);

        /*notify*/
        elevationNotification(username, authority);
        
    }

    @Override
    public void downgrade(String username, String authority) throws UserException {


        /*check if the user exists*/
        UserEntity userEntity = userEntityRepository.findUserByUsername(username).orElseThrow(
                () -> new UserException("no user to downgrade.")
        );

        /*check if the user has the authority to downgrade*/
        UserAuthority theAuthority = userAuthorityUtility.getFor(authority);
        List<UserAuthority> authorities = userEntity.getAuthorities();

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
        userEntity.getAuthorities().remove(theAuthority);
        userEntityRepository.save(userEntity);

    }

    @Override
    public UserEntity getUserByUsername(String username) {

        /*get user by username*/
        return userEntityRepository.findUserByUsername(username).orElseThrow();
    }

    @Override
    public boolean checkUserByUsernameAndAuthority(String username, UserAuthority authority) {

        /*check*/
        return userEntityRepository.findUserByUsernameAndAuthoritiesContaining(username, authority).isPresent();
    }

    @Override
    public List<UserEntity> getAllUsers() {

        /*get all users*/
        return userEntityRepository.findAll();
    }

    @Override
    public List<UserEntity> getUsersByAuthority(UserAuthority authority) {

        /*get users by authority*/
        return userEntityRepository.findUserByAuthoritiesContaining(authority);
    }

    @Override
    public void createUser(UserDetails user) {
        
    }

    @Override
    public void updateUser(UserDetails user) {

        /*get the user*/
        userEntityRepository.findUserByUsername(user.getUsername()).ifPresent(foundUser -> {

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
        return userEntityRepository.findUserByUsername(username).isPresent();
    }
}
