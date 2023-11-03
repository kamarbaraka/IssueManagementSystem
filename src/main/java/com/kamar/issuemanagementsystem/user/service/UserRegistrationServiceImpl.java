package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.rating.repository.RatingRepository;
import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.exceptions.UserExceptionService;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.UUID;

/**
 * the user registration service implementation.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final UserExceptionService userExceptionService;
    private final PasswordEncoder passwordEncoder;
    private final RatingRepository ratingRepository;
    private final DepartmentRepository departmentRepository;

    private void sendActivationEmail(String email, String token){

        /*set the email and message*/
        String subject = "TMS account activation";
        String message = "Use the token to activate your account  "+ token;

        /*send the activation message*/
        emailService.sendEmail(message, subject, email);

    }

    @Transactional
    @Override
    public void registerUser(UserRegistrationDTO registrationDTO) {

        /*convert the dto to user*/
        User user = userMapper.dtoToEntity(registrationDTO);
        /*encode password*/
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        /*get and set the rating*/
        Rating userRating = user.getRating();
        userRating.setRatingFor(user.getUsername());
        /*persist the rating and user*/
        ratingRepository.save(userRating);
        userRepository.save(user);
        /*send the activation message*/
        sendActivationEmail(user.getUsername(), user.getActivationToken());

    }

    @Transactional
    @Override
    public void activateUser(UserActivationDTO activationDTO) throws UserException {

        /*check if user exists*/
        User user = userRepository.findById(activationDTO.username())
                .orElseThrow(userExceptionService::userNotFound);

        /*check the activation token*/
        if (!user.getActivationToken().equals(activationDTO.token()))
            throw userExceptionService.invalidToken();

        /*activate the user*/
        user.setEnabled(true);
        userRepository.save(user);

        /*send email*/
        String subject = "activation successful";
        String message = "account activation successful. Your username is " + user.getUsername() +
                ". Keep this information safe.";
        emailService.sendEmail(message, subject, user.getUsername());

    }
}
