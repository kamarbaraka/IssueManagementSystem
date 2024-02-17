package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.rating.repository.UserRatingRepository;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user_management.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserException;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserExceptionService;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import com.kamar.issuemanagementsystem.user_management.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * the user registration service implementation.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserEntityRepository userEntityRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final UserExceptionService userExceptionService;
    private final PasswordEncoder passwordEncoder;
    private final UserRatingRepository userRatingRepository;
    private final CompanyProperties companyProperties;
    private final UserAuthorityRepository userAuthorityRepository;

    private void sendActivationEmail(String email, String token){

        /*set the email and message*/
        String subject = "TMS account activation";
        String message = "Use the token to activate your account  "+
                "<h3 style='color: blue;'>"+ token+ "<h3> <br>"+
                companyProperties.endTag();

        /*send the activation message*/
        emailService.sendEmail(message, subject, email, null);

    }

    @Transactional
    @Override
    public void registerUser(UserRegistrationDTO registrationDTO) throws UserException {


        /*check if user exists*/
        if ( userEntityRepository.existsById(registrationDTO.username())) {

            /*throw an exception*/
            throw new UserException("user exists or the email is not valid");
        }
        /*convert the dto to user*/
        UserEntity userEntity = userMapper.dtoToEntity(registrationDTO);
        /*encode password*/
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.password()));
        /*get and set the rating*/
        UserRating userRating = userEntity.getUserRating();
        /*set the roles/authorities*/
        UserAuthority userAuthority = userAuthorityRepository.findById(registrationDTO.role().toUpperCase()).orElseThrow(
                () -> new UserException("role doesn't exist."));
        userEntity.getAuthorities().add(userAuthority);

        /*persist the rating and user*/
        userRatingRepository.save(userRating);
        userEntityRepository.save(userEntity);
        /*send the activation message*/
        sendActivationEmail(userEntity.getUsername(), userEntity.getActivationToken());

    }

    @Transactional
    @Override
    public void activateUser(UserActivationDTO activationDTO) throws UserException {

        /*check if user exists*/
        UserEntity userEntity = userEntityRepository.findById(activationDTO.username())
                .orElseThrow(userExceptionService::userNotFound);

        /*check the activation token*/
        if (!userEntity.getActivationToken().equals(activationDTO.token())) {
            throw userExceptionService.invalidToken();
        }

        /*activate the user*/
        userEntity.setEnabled(true);
        userEntityRepository.save(userEntity);

        /*send email*/
        String subject = "activation successful";
        String message = "account activation successful. Your username is <h3 style='color: blue;'>"+
                userEntity.getUsername()+ ".<h3> <br>"+
                "Keep this information safe. <br>"+
                companyProperties.endTag();
        emailService.sendEmail(message, subject, userEntity.getUsername(), null);

    }
}
