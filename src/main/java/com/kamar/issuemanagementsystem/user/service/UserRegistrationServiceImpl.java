package com.kamar.issuemanagementsystem.user.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.rating.repository.UserRatingRepository;
import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.exceptions.UserExceptionService;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.service.invoker.UrlArgumentResolver;

/**
 * the user registration service implementation.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
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
        if ( userRepository.existsById(registrationDTO.username())) {

            /*throw an exception*/
            throw new UserException("user exists or the email is not valid");
        }
        /*convert the dto to user*/
        User user = userMapper.dtoToEntity(registrationDTO);
        /*encode password*/
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        /*get and set the rating*/
        UserRating userRating = user.getUserRating();
        /*set the roles/authorities*/
        UserAuthority userAuthority = userAuthorityRepository.findById(registrationDTO.role().toUpperCase()).orElseThrow(
                () -> new UserException("role doesn't exist."));
        user.getAuthorities().add(userAuthority);

        /*persist the rating and user*/
        userRatingRepository.save(userRating);
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
        if (!user.getActivationToken().equals(activationDTO.token())) {
            throw userExceptionService.invalidToken();
        }

        /*activate the user*/
        user.setEnabled(true);
        userRepository.save(user);

        /*send email*/
        String subject = "activation successful";
        String message = "account activation successful. Your username is <h3 style='color: blue;'>"+
                user.getUsername()+ ".<h3> <br>"+
                "Keep this information safe. <br>"+
                companyProperties.endTag();
        emailService.sendEmail(message, subject, user.getUsername(), null);

    }
}
